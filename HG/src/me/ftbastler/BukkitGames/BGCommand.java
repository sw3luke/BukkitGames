package me.ftbastler.BukkitGames;

import java.util.logging.Logger;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BGCommand implements CommandExecutor {
	Logger log = Logger.getLogger("Minecraft");
	private BGMain plugin;

	public BGCommand(BGMain plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			this.log.info("[BukkitGames] This command can only be accessed by players!");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("start")) {
			if (plugin.hasPerm(p, "bg.admin.start")
					|| plugin.hasPerm(p, "bg.admin.*")) {
				if (this.plugin.DENY_LOGIN.booleanValue())
					BGChat.printPlayerChat(p, "The game has already begun!");
				else
					this.plugin.startgame();
			} else {
				BGChat.printPlayerChat(p, "You are not allowed to do this.");
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("gamemaker")) {
			if (plugin.hasPerm(p, "bg.admin.gamemaker")
					|| plugin.hasPerm(p, "bg.admin.*")) {
				if (p.getGameMode() == GameMode.CREATIVE) {
					p.setGameMode(GameMode.SURVIVAL);
					BGVanish.makeVisible(p);

					BGChat.printPlayerChat(p,
							"§2You are no longer a GameMaker.");
				} else {
					p.setGameMode(GameMode.CREATIVE);
					BGVanish.makeVanished(p);

					BGChat.printPlayerChat(p, "§2You are now a GameMaker.");
				}
			} else
				BGChat.printPlayerChat(p, "You are not allowed to do this.");

			return true;
		}

		if (cmd.getName().equalsIgnoreCase("help")) {
			BGChat.printHelpChat(p);
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("chest")) {
			if (plugin.hasPerm(p, "bg.admin.chest")
					|| plugin.hasPerm(p, "bg.admin.*")) {
				this.plugin.spawnChest(p.getLocation());
				return true;
			}
			BGChat.printPlayerChat(p, "You are not allowed to do this.");
			return false;
		}

		if (cmd.getName().equalsIgnoreCase("rchest")) {
			if (plugin.hasPerm(p, "bg.admin.rchest")
					|| plugin.hasPerm(p, "bg.admin.*")) {
				this.plugin.spawnChest();
				return true;
			}
			BGChat.printPlayerChat(p, "You are not allowed to do this.");
			return false;
		}

		if (cmd.getName().equalsIgnoreCase("kitinfo")) {
			if (args.length != 1) {
				BGChat.printPlayerChat(p,
						"Must include a kit name! (/kitinfo [kitName])");
				return true;
			}
			BGChat.printKitInfo(p, args[0]);
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("kit")) {
			if (this.plugin.DENY_LOGIN.booleanValue()) {
				if (args.length != 1) {
					BGChat.printKitChat(p);
					return true;
				}
				BGChat.printPlayerChat(p, "The game has already started!");
				return true;
			}
			if (args.length != 1) {
				BGChat.printKitChat(p);
				return true;
			}
			BGKit.setKit(p, args[0]);
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (this.plugin.DENY_LOGIN.booleanValue()
					& !(plugin.hasPerm(p, "bg.admin.spawn") || plugin.hasPerm(
							p, "bg.admin.*"))) {
				BGChat.printPlayerChat(p, "The game has already started!");
				return true;
			} else {
				p.teleport(plugin.getSpawn());
				BGChat.printPlayerChat(p, "Teleported to the spawn location.");
				return true;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("coin")) {
			
			if (!plugin.ADV_REW) {
				BGChat.printPlayerChat(p, "Advanced Reward System disabled!");
				return true;
			}
			
			int points = plugin.getPoints(p.getName());
			int coins = (int) (points/plugin.P_F_C);
			
			String fsatz;
			
			if (plugin.reward.rewardKits.contains("all")) {
				fsatz = "With 1 Coin you can use all VIP Kits!";
			}else {
				fsatz = "With 1 Coin you can use the ";
				for (String kit : plugin.reward.rewardKits) {
					fsatz = fsatz + kit + " ";
				}
				fsatz = fsatz + "VIP Kits!";
			}
			
			if (args.length == 0) {
				BGChat.printPlayerChat(p, fsatz+
										"\nFor "+ plugin.P_F_C +"  Points you get 1 Coin!"+
										"\nYou get 1 Point for each time you win a game"+
										"\nCOINS: "+ coins +
										"\nPOINTS: "+ points +
										"\nType /coin use when you want to use a Coin!"+
										"\nType /coin send points <player> <amount> to send other players points!"+
										"\nType /coin send coins <player> <amount> to send other players coins");
				return true;
			}else if (args.length > 4) {
				
				BGChat.printPlayerChat(p, "Too much arrguments! Try /coin");
				return true;
			}else if (args[0].equalsIgnoreCase("use")) {
				
				if (p.hasPermission("bg.kit.*")) {
					
					BGChat.printPlayerChat(p, "You don't need to use Coins you can use all Kits!");
				}else if (plugin.reward.playerNames.contains(p.getName())) {
					
					BGChat.printPlayerChat(p, "You have already used a coin this round!");
					return true;
				}else if(coins >= 1) {
					plugin.reward.coinUse(p.getName());
					plugin.reward.takePoints(p.getName(), plugin.P_F_C);
					BGChat.printPlayerChat(p, "Coin used!");
					if(plugin.ADV_CHAT_SYSTEM) {
						BGChat.updateChat(p);
					}
					return true;
				}else {
					
					BGChat.printPlayerChat(p, "Too less Coins! try /coin for infos!");
					return true;
				}
			}else if (args[0].equalsIgnoreCase("send")) {
				
				if (args.length < 4) {
					
					BGChat.printPlayerChat(p, "Too less arrguments! Try /coin");
					return true;
				}
				if (args[1].equalsIgnoreCase("points")) {
				
					if (plugin.getPlayerID(args[2]) == null) {
					
						BGChat.printPlayerChat(p, "Player was never on this server!");
						return true;
					}else {
					
						int zahl;
						try{
						
							zahl = Integer.parseInt(args[3]);
						}catch (Exception e) {
						
							BGChat.printPlayerChat(p, "Only Integers are possible!");
							return true;
						}
						
						if (zahl < 0) {
							
							BGChat.printPlayerChat(p, "Only positive Integers are possible!");
							return true;
						}
					
						if (zahl > points) {
						
							BGChat.printPlayerChat(p, "You don't have enough points!");
							return true;
						}else {
							
							plugin.reward.sendPoints(p.getName(), args[2], zahl);
							
							if (plugin.getServer().getPlayer(args[2]) == null) {
								
								BGChat.printPlayerChat(p, "You have send " + zahl + " Points to " + args[2] + "!");
								return true;
							}else {
								
								BGChat.printPlayerChat(p, "You have send " + zahl + " Points to " + args[2] + "!");
								BGChat.printPlayerChat(plugin.getServer().getPlayer(args[2]), "You have received " + zahl +" Points from " + p.getName()+ "!");
								return true;
							}
						
						}
					}
				}
				if (args[1].equalsIgnoreCase("coins")) {
					
					if (plugin.getPlayerID(args[2]) == null) {
						
						BGChat.printPlayerChat(p, "Player was never on this server!");
						return true;
					}else {
					
						int zahl;
						try{
						
							zahl = Integer.parseInt(args[3]);
						}catch (Exception e) {
						
							BGChat.printPlayerChat(p, "Only Integers are possible!");
							return true;
						}
					
						if(zahl < 0) {
							
							BGChat.printPlayerChat(p, "Only positive Integers are possible!");
							return true;
						}
						
						if (zahl * plugin.P_F_C > points) {
						
							BGChat.printPlayerChat(p, "You don't have enough Coins!");
							return true;
						}else {
							
							plugin.reward.sendCoins(p.getName(), args[2], zahl);
							
							if (plugin.getServer().getPlayer(args[2]) == null) {
								
								BGChat.printPlayerChat(p, "You have send " + zahl + " Coins to " + args[2] + "!");
								return true;
							}else {
								
								BGChat.printPlayerChat(p, "You have send " + zahl + " Coins to " + args[2] + "!");
								BGChat.printPlayerChat(plugin.getServer().getPlayer(args[2]), "You have received " + zahl +" Coins from " + p.getName()+ "!");
								return true;
							}
						
						}
					}
				}
			}else if(args[0].equalsIgnoreCase("give")) {
					
					if (p.hasPermission("bg.admin.give")) {	
						if (args.length < 4) {
						
							BGChat.printPlayerChat(p, "Too less arrguments!");
							return true;
						}
						if (plugin.getPlayerID(args[2]) == null) {
						
							BGChat.printPlayerChat(p, "Player was never on this server!");
							return true;
						}
					
						if (args[1].equalsIgnoreCase("points")) {
						
							int zahl;
						
							try {
							
								zahl = Integer.parseInt(args[3]);
							}catch (Exception e) {
							
								BGChat.printPlayerChat(p, "Only Integers are possible!");
								return true;
							}
							
							if (zahl < 0) {
								
								BGChat.printPlayerChat(p, "Only positive Integers are possible!");
							}
							
							plugin.reward.givePoints(args[2], zahl);
							BGChat.printPlayerChat(p, "You gave " + zahl + "Points to " + args[2] + "!");
							if (plugin.getServer().getPlayer(args[2]) == null)
								return true;
							
							BGChat.printPlayerChat(plugin.getServer().getPlayer(args[2]), "You have received " + zahl + " Points!");
								
							return true;
						}
						
						if (args[1].equalsIgnoreCase("coins")) {
							
							int zahl;
							
							try {
								
								zahl = Integer.parseInt(args[3]);
							}catch (Exception e) {
								
								BGChat.printPlayerChat(p, "Only Integers are possible!");
								return true;
							}
							
							if (zahl < 0) {
								
								BGChat.printPlayerChat(p, "Only positive Integers are possible!");
							}
							
							plugin.reward.givePoints(args[2], zahl * plugin.P_F_C);
							BGChat.printPlayerChat(p, "You gave " + zahl + "Coins to " + args[2] + "!");
							
							if(plugin.getServer().getPlayer(args[2]) == null)
								return true;
							
							BGChat.printPlayerChat(plugin.getServer().getPlayer(args[2]), "You have received " + zahl + " Coins!");
							
							return true;
						}
					}else {
						
						BGChat.printPlayerChat(p, "You don't have enough permissions!");
						return true;
					}
			}else if(args[0].equalsIgnoreCase("take")) {
					
					if (p.hasPermission("bg.admin.take")) {	
						if (args.length < 4) {
						
							BGChat.printPlayerChat(p, "Too less arrguments!");
							return true;
						}
						if (plugin.getPlayerID(args[2]) == null) {
						
							BGChat.printPlayerChat(p, "Player was never on this server!");
							return true;
						}
					
						if (args[1].equalsIgnoreCase("points")) {
						
							int zahl;
						
							try {
							
								zahl = Integer.parseInt(args[3]);
							}catch (Exception e) {
							
								BGChat.printPlayerChat(p, "Only Integers are possible!");
								return true;
							}
							
							if (zahl < 0) {
								
								BGChat.printPlayerChat(p, "Only positive Integers are possible!");
							}
							
							plugin.reward.takePoints(args[2], zahl);
							BGChat.printPlayerChat(p, "You took " + zahl + " Points from " + args[2] + "!");
							if (plugin.getServer().getPlayer(args[2]) == null)
								return true;
							
							BGChat.printPlayerChat(plugin.getServer().getPlayer(args[2]), "You lost " + zahl + " Points!");
								
							return true;
						}
						
						if (args[1].equalsIgnoreCase("coins")) {
							
							int zahl;
							
							try {
								
								zahl = Integer.parseInt(args[3]);
							}catch (Exception e) {
								
								BGChat.printPlayerChat(p, "Only Integers are possible!");
								return true;
							}
							
							if (zahl < 0) {
								
								BGChat.printPlayerChat(p, "Only positive Integers are possible!");
							}
							
							plugin.reward.takePoints(args[2], zahl * plugin.P_F_C);
							BGChat.printPlayerChat(p, "You took " + zahl + " Coins from " + args[2] + "!");
							
							if(plugin.getServer().getPlayer(args[2]) == null)
								return true;
							
							BGChat.printPlayerChat(plugin.getServer().getPlayer(args[2]), "You lost " + zahl + " Coins!");
							
							return true;
						}
					}else {
						
						BGChat.printPlayerChat(p, "You don't have enough permissions!");
						return true;
					}
				}else if(args[0].equalsIgnoreCase("stats")) {
					
					if(p.hasPermission("bg.admin.stats")) {
						
						if (args.length < 2) {
							
							BGChat.printPlayerChat(p, "To less arrguments!");
						}
						
						if (plugin.getPlayerID(args[1]) == null) {
							
							BGChat.printPlayerChat(p, "Player was never on this server!");
							return true;
						}
						
						int points1 = plugin.getPoints(args[1]);
						int coins1 = (int) points1/plugin.P_F_C;
						
						BGChat.printPlayerChat(p, "This are the stats of " + args[1] + ":"+
						"\nPOINTS: " + points1 +
						"\nCOINS: " + coins1);
					}else {
						
						BGChat.printPlayerChat(p, "You don't have enough permissions!");
						return true;
					}
				}
		}
		
		if (cmd.getName().equalsIgnoreCase("fbattle")) {
			
			if(p.hasPermission("bg.admin.fbattle")) {
				
				if(plugin.DENY_LOGIN) {
					if(plugin.END_GAME_M) {
						BGChat.printInfoChat("Final battle ahead. Teleporting everybody to spawn in 1 minute!");
						plugin.END_GAME_A = false;
						plugin.cooldown.fbattleCooldown();
						
						return true;
					}else {
						BGChat.printPlayerChat(p, "You can not start the Final Battle because it will start automaticly soon!");
						
						return true;
					}
				}else{
					
					BGChat.printPlayerChat(p, "The games is not started yet!");
					
					return true;
				}
				
			}else {
				
				BGChat.printPlayerChat(p, "You don't have enough Permissions!");
				
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("bgcheckversion")) {
			
			if(p.hasPermission("bg.admin.check")) {
				
				Updater updater = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_DOWNLOAD, false);
				
				boolean update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				
				if (update) {
					
					String newversion = updater.getLatestVersionString();
					long size = updater.getFileSize();
					
					BGChat.printPlayerChat(p, "The BukkitGames Update is available: " + newversion + "(" + size + "bytes)\n"+
											"Type /bgdownload to download the update! (remember to regenerate all config files");
				}else {
					
					BGChat.printPlayerChat(p, "There is no Update for The BukkitGames available!");
				}
			}else {
				
				BGChat.printPlayerChat(p, "You don't have enough Permissions!");
				
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("bgversion")) {
			
			if(p.hasPermission("bg.admin.version")) {
				
				BGChat.printPlayerChat(p, "Current Version of The BukkitGames: " + plugin.getDescription().getVersion());
				
				return true;
			}else {
				
				BGChat.printPlayerChat(p, "You don't have enough Permissions!");
				
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("bgdownload")) {
			
			if(p.hasPermission("bg.admin.download")) {
				
				Updater updater = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_DOWNLOAD, false);
				
				boolean update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				
				if(update) {
					
					BGChat.printPlayerChat(p, "Starting download a new Version of The BukkitGames");
					
					Updater download = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
					
					if(download.getResult() == Updater.UpdateResult.SUCCESS)
						BGChat.printPlayerChat(p, "Download completed! Let the plugin regenerate all config files!");
					else
						BGChat.printPlayerChat(p, "Ooops! Something went wrong look at your console to see a better error log!");
				}else {
					
					BGChat.printPlayerChat(p, "There is no Update available to download!");
				}
				
				return true;
			}else {
				
				BGChat.printPlayerChat(p, "You don't have enough Permissions!");
				
				return true;
			}
		}
			return true;
	}
}
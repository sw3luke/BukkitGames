package me.ftbastler.BukkitGames;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BGCommand implements CommandExecutor {
	Logger log = BGMain.getPluginLogger();
	private BGMain plugin;

	public BGCommand(BGMain plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			this.log.info("This command can only be accessed by players!");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("start")) {
			if (p.hasPermission("bg.admin.start")
					|| p.hasPermission("bg.admin.*")) {
				if (this.plugin.DENY_LOGIN.booleanValue())
					BGChat.printPlayerChat(p, "The game has already begun!");
				else
					this.plugin.startgame();
			} else {
				BGChat.printPlayerChat(p, "You are not allowed to do this.");
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("help")) {
			BGChat.printHelpChat(p);
			return true;
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
				BGChat.printPlayerChat(p, "The game has already began!");
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
					& !(p.hasPermission("bg.admin.spawn") || p.hasPermission("bg.admin.*"))) {
				BGChat.printPlayerChat(p, "The game has already began!");
				return true;
			} else {
				p.teleport(plugin.getSpawn());
				BGChat.printPlayerChat(p, "Teleported to the spawn location.");
				return true;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("coin")) {
			
			if (!plugin.REW) {
				BGChat.printPlayerChat(p, "Reward System disabled!");
				return true;
			}
			
			int coins = plugin.getCoins(plugin.getPlayerID(p.getName()));
			
			if (args.length == 0) {
				BGChat.printPlayerChat(p,
										"\nYou get 5 Coins for each time you win a game"+
										"\nYou get 1 Coin for killing a player (only close combat)"+		
										"\nCOINS: "+ coins +
										"\nType /coin buy [kitName] when you want to buy a Kit!"+
										"\nType /coin send <player> <amount> to send other players coins");
				return true;
			}else if (args.length > 3) {
				
				BGChat.printPlayerChat(p, "Too much arrguments! Try /coin");
				return true;
			}else if (args[0].equalsIgnoreCase("buy")) {
				
				if(args.length < 2) {
					BGChat.printPlayerChat(p, "Too few arrguments! Try /coin");
					return true;
				}
				
				String kitName = args[1];
				
				if (p.hasPermission("bg.kit.*")) {
					
					BGChat.printPlayerChat(p, "You don't need to use Coins you can use all Kits!");
				}else if (plugin.reward.BOUGHT_KITS.containsKey(p.getName())) {
					
					BGChat.printPlayerChat(p, "You have already bought a kit this round!");
					return true;
				}else if (BGKit.isKit(kitName.toLowerCase()) == false) {
					
					BGChat.printPlayerChat(p, "This Kit does not exits!");
					return true;
				}else if(coins >= BGKit.getCoins(kitName.toLowerCase())) {
					plugin.reward.coinUse(p.getName(), kitName.toLowerCase());
					plugin.reward.takeCoins(p.getName(), BGKit.getCoins(kitName.toLowerCase()));
					BGChat.printPlayerChat(p, "You bought the "+kitName+" Kit!");
					if(plugin.ADV_CHAT_SYSTEM) {
						BGChat.updateChat(p);
					}
					return true;
				}else {
					
					BGChat.printPlayerChat(p, "Too few Coins! try /coin for infos!");
					return true;
				}
			}else if (args[0].equalsIgnoreCase("send")) {
				
				if (args.length < 3) {
					
					BGChat.printPlayerChat(p, "Too few arrguments! Try /coin");
					return true;
				}
				if (plugin.getPlayerID(args[1]) == null) {
						
					BGChat.printPlayerChat(p, "Player was never on this server!");
					return true;
				}else {
					
					int zahl;
					try{
						
						zahl = Integer.parseInt(args[2]);
					}catch (Exception e) {
						
							BGChat.printPlayerChat(p, "Only Integers are possible!");
							return true;
					}
					
					if(zahl < 0) {
							
						BGChat.printPlayerChat(p, "Only positive Integers are possible!");
						return true;
					}
						
					if (zahl > coins) {
						
						BGChat.printPlayerChat(p, "You don't have enough Coins!");
						return true;
					}else {
							
						plugin.reward.sendCoins(p.getName(), args[1], zahl);
							
							if (plugin.getServer().getPlayer(args[1]) == null) {
								
								BGChat.printPlayerChat(p, "You have send " + zahl + " Coins to " + args[1] + "!");
								return true;
							}else {
								
								BGChat.printPlayerChat(p, "You have send " + zahl + " Coins to " + args[1] + "!");
								BGChat.printPlayerChat(plugin.getServer().getPlayer(args[1]), "You have received " + zahl +" Coins from " + p.getName()+ "!");
								return true;
							}
						
						}
					}
				
			}else if(args[0].equalsIgnoreCase("give")) {
					
					if (p.hasPermission("bg.admin.give")) {	
						if (args.length < 3) {
						
							BGChat.printPlayerChat(p, "Too few arrguments!");
							return true;
						}
						if (plugin.getPlayerID(args[1]) == null) {
						
							BGChat.printPlayerChat(p, "Player was never on this server!");
							return true;
						}
						
							
						int zahl;
							
						try {
								
							zahl = Integer.parseInt(args[2]);
						}catch (Exception e) {
								
							BGChat.printPlayerChat(p, "Only Integers are possible!");
							return true;
						}
							
						if (zahl < 0) {
								
							BGChat.printPlayerChat(p, "Only positive Integers are possible!");
						}
							
							plugin.reward.giveCoins(args[1], zahl);
							BGChat.printPlayerChat(p, "You gave " + zahl + "Coins to " + args[1] + "!");
							
							if(plugin.getServer().getPlayer(args[2]) == null)
								return true;
							
							BGChat.printPlayerChat(plugin.getServer().getPlayer(args[1]), "You have received " + zahl + " Coins!");
							
							return true;
						
					}else {
						
						BGChat.printPlayerChat(p, "You don't have enough permissions!");
						return true;
					}
			}else if(args[0].equalsIgnoreCase("take")) {
					
					if (p.hasPermission("bg.admin.take")) {	
						if (args.length < 3) {
						
							BGChat.printPlayerChat(p, "Too less arrguments!");
							return true;
						}
						if (plugin.getPlayerID(args[1]) == null) {
						
							BGChat.printPlayerChat(p, "Player was never on this server!");
							return true;
						}
							
							int zahl;
							
							try {
								
								zahl = Integer.parseInt(args[2]);
							}catch (Exception e) {
								
								BGChat.printPlayerChat(p, "Only Integers are possible!");
								return true;
							}
							
							if (zahl < 0) {
								
								BGChat.printPlayerChat(p, "Only positive Integers are possible!");
							}
							
							plugin.reward.takeCoins(args[1], zahl);
							BGChat.printPlayerChat(p, "You took " + zahl + " Coins from " + args[1] + "!");
							
							if(plugin.getServer().getPlayer(args[1]) == null)
								return true;
							
							BGChat.printPlayerChat(plugin.getServer().getPlayer(args[1]), "You lost " + zahl + " Coins!");
							
							return true;
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
						
						int coins1 = plugin.getCoins(plugin.getPlayerID(args[1]));
						
						BGChat.printPlayerChat(p, "This are the stats of " + args[1] + ":"+
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
						BGChat.printPlayerChat(p, "You can not start the final battle because it will start soon!");
						return true;
					}
				}else{
					
					BGChat.printPlayerChat(p, "§cThe game has not started yet!");
					
					return true;
				}
				
			}else {
				
				BGChat.printPlayerChat(p, "You don't have enough Permissions!");
				
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("bgversion")) {
			if(p.hasPermission("bg.admin.check")) {
				Updater updater = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_DOWNLOAD, false);
				boolean update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				if (update) {
					String newversion = updater.getLatestVersionString();
					BGChat.printPlayerChat(p, "§aUpdate available: " + newversion + " §r/bgdownload");
				} else {
					BGChat.printPlayerChat(p, "§7Current version of The BukkitGames: " + plugin.getDescription().getVersion());
				}
			} else {
				BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("bgdownload")) {
			if(p.hasPermission("bg.admin.download")) {
				Updater updater = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_DOWNLOAD, false);
				boolean update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				
				if(update) {
					BGChat.printPlayerChat(p, "§7Downloading new version...");
					Updater download = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
					
					if(download.getResult() == Updater.UpdateResult.SUCCESS)
						BGChat.printPlayerChat(p, "§aDownload complete! §7Regenerate all config files!");
					else
						BGChat.printPlayerChat(p, "§cOops! Something went wrong. See console error log!");
				}else {
					
					BGChat.printPlayerChat(p, "§7There is no update available to download!");
				}
				
				return true;
			}else {
				
				BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
				
				return true;
			}
		}
			return true;
	}
}
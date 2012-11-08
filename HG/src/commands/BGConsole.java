package commands;

import java.util.logging.Logger;

import main.BGMain;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import utilities.BGChat;
import utilities.BGKit;
import utilities.Updater;

public class BGConsole implements CommandExecutor {
	Logger log = BGMain.getPluginLogger();
	private BGMain plugin;

	public BGConsole(BGMain plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p;
		if(sender instanceof Player) {
			p = (Player) sender;
		}else {
			p = null;
		}
		if (cmd.getName().equalsIgnoreCase("start")) {
			if (sender.hasPermission("bg.admin.start")
					|| sender.hasPermission("bg.admin.*")) {
				if (this.plugin.DENY_LOGIN.booleanValue())			
					if (p != null) 
						BGChat.printPlayerChat(p, "The game has already begun!");
					else
						sender.sendMessage("The game has already begun!");
				else
					this.plugin.startgame();
			} else {
				BGChat.printPlayerChat(p, "§cYou are not allowed to do this.");
			}
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("fbattle")) {
			
			if(sender.hasPermission("bg.admin.fbattle")) {
				
				if(plugin.DENY_LOGIN) {
					if(plugin.END_GAME_M) {
						if(p != null)
							BGChat.printInfoChat("Final battle ahead. Teleporting everybody to spawn in 1 minute!");
						else
							sender.sendMessage("Final battle ahead. Teleporting everybody to spawn in 1 minute!");
						plugin.END_GAME_A = false;
						plugin.cooldown.fbattleCooldown();
						
						return true;
					}else {
						if(p != null)
							BGChat.printPlayerChat(p, "You can not start the final battle because it will start soon!");
						else
							sender.sendMessage("You can not start the final battle because it will start soon!");
						return true;
					}
				}else{
					
					if(p != null)
						BGChat.printPlayerChat(p, "The game has not started yet!");
					else
						sender.sendMessage("The game has not started yet!");
					
					return true;
				}
				
			}else {
				
				BGChat.printPlayerChat(p, "§cYou don't have enough Permissions!");
				
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("bgversion")) {
			if(sender.hasPermission("bg.admin.check")) {
				Updater updater = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_DOWNLOAD, false);
				boolean update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				if (update) {
					String newversion = updater.getLatestVersionString();
					if(p != null)
						BGChat.printPlayerChat(p, "§6Update available: " + newversion + " §r/bgdownload");
					else
						sender.sendMessage("§6Update available: " + newversion + " §r/bgdownload");
				} else {
					if(p != null)
						BGChat.printPlayerChat(p, "§7Current version of The BukkitGames: " + plugin.getDescription().getVersion());
					else
						sender.sendMessage("§7Current version of The BukkitGames: " + plugin.getDescription().getVersion());
				}
			} else {
				BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("bgdownload")) {
			if(sender.hasPermission("bg.admin.download")) {
				Updater updater = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_DOWNLOAD, false);
				boolean update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				
				if(update) {
					if(p != null)
						BGChat.printPlayerChat(p, "§7Downloading new version...");
					else
						sender.sendMessage("§7Downloading new version...");
					Updater download = new Updater(plugin, "bukkitgames", plugin.getPFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
					
					if(download.getResult() == Updater.UpdateResult.SUCCESS) {
						if(p != null)
							BGChat.printPlayerChat(p, "§6Download complete! §7Regenerate all config files!");
						else
							sender.sendMessage("§6Download complete! §7Regenerate all config files!");
					} else {
						if(p != null)
							BGChat.printPlayerChat(p, "§cOops! Something went wrong. See console error log!");
						else
							sender.sendMessage("§cOops! Something went wrong. See console error log!");
					}
				}else {
					
					if(p != null)
						BGChat.printPlayerChat(p, "§7There is no update available to download!");
					else
						sender.sendMessage("§7There is no update available to download!");
				}
				
				return true;
			}else {
				
				BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
				
				return true;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("coin")) {
			
			if (!plugin.REW) {
				if(p == null) {
					sender.sendMessage("§eReward System disabled!");
				}else {
					BGChat.printPlayerChat(p, "§eReward System disabled!");
				}
				return true;
			}
			
			int coins = 0;
			
			if( p == null && ((args.length < 1) || (args.length > 0 && (!args[0].equalsIgnoreCase("give") && !args[0].equalsIgnoreCase("take"))))) {
				
				sender.sendMessage("§cThis command can only be accessed by players!");
				return true;
			}else if( p != null){
			
				coins = plugin.getCoins(plugin.getPlayerID(p.getName()));
			
			}
			
			if (args.length == 0) {
				BGChat.printPlayerChat(p,
										"§eYou get "+plugin.COINS_FOR_WIN+" Coins for each time you win a game"+
										'\n'+"§eYou get "+plugin.COINS_FOR_KILL+" Coin for killing a player (only close combat)"+		
										'\n'+"§eCOINS: "+ coins +
										'\n'+"§eType /coin buy [kitName] when you want to buy a Kit!"+
										'\n'+"§eType /coin send <player> <amount> to send other players coins");
				return true;
			}else if (args.length > 3) {
				
				BGChat.printPlayerChat(p, "§eToo much arrguments! Try /coin");
				return true;
			}else if (args[0].equalsIgnoreCase("buy")) {
				
				if(plugin.isSpectator(p)) {
					
					BGChat.printPlayerChat(p, "§eYou can not buy a kit because you are a spectator!");
					return true;
				}
				
				if(args.length < 2) {
					BGChat.printPlayerChat(p, "§eToo few arrguments! Try /coin");
					return true;
				}
				
				String kitName = args[1];
				
				if (p.hasPermission("bg.kit.*")) {
					
					BGChat.printPlayerChat(p, "§eYou don't need to use Coins you can use all Kits!");
				}else if (plugin.REW && plugin.reward.BOUGHT_KITS.containsKey(p.getName())) {
					
					BGChat.printPlayerChat(p, "§eYou have already bought a kit this round!");
					return true;					
				}else if (BGKit.isKit(kitName.toLowerCase()) == false) {
					
					BGChat.printPlayerChat(p, "§eThis Kit does not exits!");
					return true;
				}else if(BGKit.getCoins(kitName.toLowerCase()) == 0) {
					
					BGChat.printPlayerChat(p, "§eThis Kit can not be bought!");
					return true;
				}else if(coins >= BGKit.getCoins(kitName.toLowerCase())) {
					plugin.reward.coinUse(p.getName(), kitName.toLowerCase());
					plugin.reward.takeCoins(p.getName(), BGKit.getCoins(kitName.toLowerCase()));
					BGChat.printPlayerChat(p, "§eYou bought the "+kitName+" Kit!");
					if(plugin.ADV_CHAT_SYSTEM) {
						BGChat.updateChat(p);
					}
					return true;
				}else {
					
					BGChat.printPlayerChat(p, "§eToo few Coins! try /coin for infos!");
					return true;
				}
			}else if (args[0].equalsIgnoreCase("send")) {
				
				if (args.length < 3) {
					
					BGChat.printPlayerChat(p, "§eToo few arrguments! Try /coin");
					return true;
				}
				if (plugin.getPlayerID(args[1]) == null) {
						
					BGChat.printPlayerChat(p, "§ePlayer was never on this server!");
					return true;
				}else {
					
					int zahl;
					try{
						
						zahl = Integer.parseInt(args[2]);
					}catch (Exception e) {
						
							BGChat.printPlayerChat(p, "§eOnly Integers are possible!");
							return true;
					}
					
					if(zahl < 0) {
							
						BGChat.printPlayerChat(p, "§eOnly positive Integers are possible!");
						return true;
					}
						
					if (zahl > coins) {
						
						BGChat.printPlayerChat(p, "§eYou don't have enough Coins!");
						return true;
					}else {
							
						plugin.reward.sendCoins(p.getName(), args[1], zahl);
							
							if (plugin.getServer().getPlayer(args[1]) == null) {
								
								BGChat.printPlayerChat(p, "§eYou have send " + zahl + " Coins to " + args[1] + "!");
								return true;
							}else {
								
								BGChat.printPlayerChat(p, "§eYou have send " + zahl + " Coins to " + args[1] + "!");
								BGChat.printPlayerChat(plugin.getServer().getPlayer(args[1]), "§3You have received " + zahl +" Coins from " + p.getName()+ "!");
								return true;
							}
						
						}
					}
				
			}else if(args[0].equalsIgnoreCase("give")) {
					
					if (sender.hasPermission("bg.admin.give")) {	
						if (args.length < 3) {
							if(p == null) {
								sender.sendMessage("§eToo few arrguments!");
							}else {
								BGChat.printPlayerChat(p, "§eToo few arrguments!");
							}
							return true;
						}
						if (plugin.getPlayerID(args[1]) == null) {
						
							if(p == null) {
								sender.sendMessage("§ePlayer was never on this server!");
							}else {
								BGChat.printPlayerChat(p, "§ePlayer was never on this server!");
							}
							return true;
						}
						
							
						int zahl;
							
						try {
								
							zahl = Integer.parseInt(args[2]);
						}catch (Exception e) {
							
							if(p == null) {
								sender.sendMessage("§eOnly Integers are possible!");
							}else {
								BGChat.printPlayerChat(p, "§eOnly Integers are possible!");
							}
							return true;
						}
							
						if (zahl < 0) {
							
							if(p == null) {
								sender.sendMessage("§eOnly positive Integers are possible!");
							}else {
								BGChat.printPlayerChat(p, "§eOnly positive Integers are possible!");
							}
						}
							
							plugin.reward.giveCoins(args[1], zahl);
							if(p == null) {
								sender.sendMessage("§eYou gave " + zahl + " Coins to " + args[1] + "!");
							}else {
								BGChat.printPlayerChat(p, "§eYou gave " + zahl + " Coins to " + args[1] + "!");
							}
							
							if(plugin.getServer().getPlayer(args[2]) == null)
								return true;
							
							BGChat.printPlayerChat(plugin.getServer().getPlayer(args[1]), "§eYou have received " + zahl + " Coins!");
							
							return true;
						
					}else {
						
						BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
						return true;
					}
			}else if(args[0].equalsIgnoreCase("take")) {
					
					if (sender.hasPermission("bg.admin.take")) {	
						if (args.length < 3) {
						
							if(p == null) {
								sender.sendMessage("§eToo few arrguments!");
							}else {
								BGChat.printPlayerChat(p, "§eToo few arrguments!");
							}
							return true;
						}
						if (plugin.getPlayerID(args[1]) == null) {
						
							if(p == null) {
								sender.sendMessage("§ePlayer was never on this server!");
							}else {
								BGChat.printPlayerChat(p, "§ePlayer was never on this server!");
							}
							return true;
						}
							
							int zahl;
							
							try {
								
								zahl = Integer.parseInt(args[2]);
							}catch (Exception e) {
								
								if(p == null) {
									sender.sendMessage("§eOnly Integers are possible!");
								}else {
									BGChat.printPlayerChat(p, "§eOnly Integers are possible!");
								}
								return true;
							}
							
							if (zahl < 0) {
								
								if(p == null) {
									sender.sendMessage("§eOnly positive Integers are possible!");
								}else {
									BGChat.printPlayerChat(p, "§eOnly positive Integers are possible!");
								}
							}
							
							plugin.reward.takeCoins(args[1], zahl);
							if(p == null) {
								sender.sendMessage("§eYou took " + zahl + " Coins from " + args[1] + "!");
							}else {
								BGChat.printPlayerChat(p, "§eYou took " + zahl + " Coins from " + args[1] + "!");
							}
							
							if(plugin.getServer().getPlayer(args[1]) == null)
								return true;
							
							BGChat.printPlayerChat(plugin.getServer().getPlayer(args[1]), "§eYou lost " + zahl + " Coins!");
							
							return true;
					}else {
						
						BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
						return true;
					}
				}else if(args[0].equalsIgnoreCase("stats")) {
					
					if(p.hasPermission("bg.admin.stats")) {
						
						if (args.length < 2) {
							
							BGChat.printPlayerChat(p, "§eTo less arrguments!");
						}
						
						if (plugin.getPlayerID(args[1]) == null) {
							
							BGChat.printPlayerChat(p, "§ePlayer was never on this server!");
							return true;
						}
						
						int coins1 = plugin.getCoins(plugin.getPlayerID(args[1]));
						
						BGChat.printPlayerChat(p, "§eThis are the stats of " + args[1] + ":"+
						"\n§eCOINS: " + coins1);
					}else {
						
						BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
						return true;
					}
				}
		}
		
			return true;
	}
}
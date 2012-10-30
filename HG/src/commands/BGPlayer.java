package commands;

import java.util.logging.Logger;

import main.BGMain;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import utilities.BGChat;
import utilities.BGKit;
import utilities.BGTeam;

public class BGPlayer implements CommandExecutor{

	Logger log = BGMain.getPluginLogger();
	private BGMain plugin;
	
	public BGPlayer(BGMain plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			this.log.info("This command can only be accessed by players!");
			return true;
		}
		Player p = (Player) sender;
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
				}else if (plugin.REW && plugin.reward.BOUGHT_KITS.containsKey(p.getName())) {
					
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
		
		if(cmd.getName().equalsIgnoreCase("team")) {
			
			if(!plugin.TEAM) {
				
				BGChat.printPlayerChat(p, "Team function is disabled!");
				return true;
			}
			
			if(!p.hasPermission("bg.team")) {
				
				BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
				return true;
			}
			
			if(args.length > 2) {
				BGChat.printPlayerChat(p, "Too much arrguments! Try /team");
				return true;
			}
			
			if (args.length == 0) {
				
				BGChat.printPlayerChat(p, "Type /team add <player> to add a player to your team!" + '\n'+
											"Type /team remove <player> to remove a player from your team!" + '\n'+
											"Type /team list to get a list of all players in your team");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("add")) {
				
				if(args.length < 2) {
					
					BGChat.printPlayerChat(p, "Too few arrguments! Try /team");
					return true;
				}
				
				if(plugin.getServer().getPlayer(args[1]) == null) {
					
					BGChat.printPlayerChat(p, "This player is not online!");
				}
				
				BGTeam.addMember(p, args[1]);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				
				if(args.length < 2) {
					
					BGChat.printPlayerChat(p, "Too few arrguments! Try /team");
					return true;
				}
				
				if(!BGTeam.isInTeam(p, args[1])) {
					
					BGChat.printPlayerChat(p, "This player is not in your team!");
					return true;
				}
				
				BGTeam.removeMember(p, args[1]);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("list")) {
				
				if(args.length < 1) {
					
					BGChat.printPlayerChat(p, "Too few arrguments! Try /team");
					return true;
				}
				
				if(args.length > 1) {
					
					BGChat.printPlayerChat(p, "Too much arrguments! Try /team");
					return true;
				}
				
				String text = "Your Team:";
				for(String t : BGTeam.getTeamList(p)) {
					
					text += '\n' + "- " + t;
				}
				
				BGChat.printPlayerChat(p, text);
				return true;
			}
		}
		
		return true;
	}
}

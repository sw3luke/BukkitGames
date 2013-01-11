package commands;

import java.util.logging.Logger;

import main.BGMain;

import org.bukkit.Location;
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
			this.log.info("§cThis command can only be accessed by players!");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("help")) {
			BGChat.printHelpChat(p);
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("gamemaker")) {
			
			if(p.hasPermission("bg.admin.gamemaker")) {
				
				if(plugin.isGameMaker(p)) {
					
					plugin.remGameMaker(p);
					BGChat.printPlayerChat(p, "§eYou are no longer a gamemaker!");
					return true;
				}else {
					
					plugin.addGameMaker(p);
					return true;
				}
			}else {
				
				BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
				return true;
			}
		}

		if (cmd.getName().equalsIgnoreCase("kitinfo")) {
			if (args.length != 1) {
				BGChat.printPlayerChat(p,
						"§eMust include a kit name! (/kitinfo [kitName])");
				return true;
			}
			BGChat.printKitInfo(p, args[0]);
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("kit")) {
			if (this.plugin.DENY_LOGIN.booleanValue()) {
				BGChat.printPlayerChat(p, "§eThe game has already began!");
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
					& !p.hasPermission("bg.admin.spawn")
					& !(plugin.isGameMaker(p) || plugin.isSpectator(p))) {
				BGChat.printPlayerChat(p, "§eThe game has already began!");
				return true;
			} else {
				p.teleport(plugin.getSpawn());
				BGChat.printPlayerChat(p, "§eTeleported to the spawn location.");
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("team")) {
			
			if(!plugin.TEAM) {
				
				BGChat.printPlayerChat(p, "§eTeam function is disabled!");
				return true;
			}
			
			if(!p.hasPermission("bg.team")) {
				
				BGChat.printPlayerChat(p, "§cYou don't have enough permissions!");
				return true;
			}
			
			if(args.length > 2) {
				BGChat.printPlayerChat(p, "§eToo much arrguments! Try /team");
				return true;
			}
			
			if (args.length == 0) {
				
				BGChat.printPlayerChat(p, "§eType /team add <player> to add a player to your team!" + '\n'+
											"§eType /team remove <player> to remove a player from your team!" + '\n'+
											"§eType /team list to get a list of all players in your team");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("add")) {
				
				if(args.length < 2) {
					
					BGChat.printPlayerChat(p, "§eToo few arrguments! Try /team");
					return true;
				}
				
				
				
				if(plugin.getServer().getPlayer(args[1]) == null) {
					
					BGChat.printPlayerChat(p, "§eThis player is not online!");
					return true;
				}
				
				Player player = plugin.getServer().getPlayer(args[1]);
				
				if(BGTeam.isInTeam(p, player.getName())){
					
					BGChat.printPlayerChat(p, "§eThis player is already in your team!");
					return true;
				}
					
				BGTeam.addMember(p, player.getName());
				BGChat.printPlayerChat(p, "§eYou added "+player.getName()+" to your team!");
				
				return true;
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				
				if(args.length < 2) {
					
					BGChat.printPlayerChat(p, "§eToo few arrguments! Try /team");
					return true;
				}
				
				if(!BGTeam.isInTeam(p, args[1])) {
					
					BGChat.printPlayerChat(p, "§eThis player is not in your team!");
					return true;
				}
				
				BGTeam.removeMember(p, args[1]);
				BGChat.printPlayerChat(p, "§eYou removed "+args[1]+" from your team!" );
				
				return true;
			}
			
			if (args[0].equalsIgnoreCase("list")) {
				
				if(args.length < 1) {
					
					BGChat.printPlayerChat(p, "§eToo few arrguments! Try /team");
					return true;
				}
				
				if(args.length > 1) {
					
					BGChat.printPlayerChat(p, "§eToo much arrguments! Try /team");
					return true;
				}
				
				String text = "§eYour Team:";
				
				if(BGTeam.getTeamList(p) == null) {
					
					text += '\n' + "§e- No Player in your Team!";
					BGChat.printPlayerChat(p, text);
					return true;
				}
				
				for(String t : BGTeam.getTeamList(p)) {
					
					text += '\n' + "§e- " + t;
				}
				
				BGChat.printPlayerChat(p, text);
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("teleport")) {
			
			if(plugin.isGameMaker(p) || plugin.isSpectator(p)) {
				
				if(args.length > 2) {
					
					BGChat.printPlayerChat(p, "§eToo much arrguments! Try /teleport");
					return true;
				}
				
				if(args.length == 0) {
					
					BGChat.printPlayerChat(p, "§eTeleport Menu:"+
											'\n' + "§eType /teleport <player> to teleport to an other player"+
											'\n' + "§eType /teleport <x> <z> to teleport to this cords");
					return true;
				}
				
				if(args.length == 1) {
					
					if(plugin.getServer().getPlayer(args[0]) == null) {
						
						BGChat.printPlayerChat(p, "§eThis player is not online!");
						return true;
					}
					
					Player target = plugin.getServer().getPlayer(args[0]);
					BGChat.printPlayerChat(p, "§eTeleport to "+ target.getName());
					p.teleport(target);
					return true;
				}
				
				if(args.length == 2) {
					
					int x = 0;
					int z = 0;
					
					try{
						
						x = Integer.parseInt(args[0]);
						z = Integer.parseInt(args[1]);
					}catch(NumberFormatException e) {
						
						BGChat.printPlayerChat(p, "§eOnly integers are possible!");
						return true;
					}
					
					Location loc = new Location(plugin.getServer().getWorlds().get(0), x, plugin.getServer().getWorlds().get(0).getHighestBlockYAt(x, z)+1.5, z);
					
					if(!plugin.inBorder(loc)) {
						
						BGChat.printPlayerChat(p, "§eThis cords are not in the worldborder!");
						return true;
					}
					BGChat.printPlayerChat(p, "§eTeleport to X: "+x+" Z: "+z);
					p.teleport(loc);
					return true;
				}
			}else {
				
				BGChat.printPlayerChat(p, "§eOnly Spectators and Gamemakers can use this!");
				return true;
			}
		}
		
		return true;
	}
}

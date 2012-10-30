package commands;

import java.util.logging.Logger;

import main.BGMain;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import utilities.BGChat;
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
				BGChat.printPlayerChat(p, "You are not allowed to do this.");
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
						BGChat.printPlayerChat(p, "§cThe game has not started yet!");
					else
						sender.sendMessage("§cThe game has not started yet!");
					
					return true;
				}
				
			}else {
				
				BGChat.printPlayerChat(p, "You don't have enough Permissions!");
				
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
						BGChat.printPlayerChat(p, "§aUpdate available: " + newversion + " §r/bgdownload");
					else
						sender.sendMessage("§aUpdate available: " + newversion + " §r/bgdownload");
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
							BGChat.printPlayerChat(p, "§aDownload complete! §7Regenerate all config files!");
						else
							sender.sendMessage("§aDownload complete! §7Regenerate all config files!");
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
		
			return true;
	}
}
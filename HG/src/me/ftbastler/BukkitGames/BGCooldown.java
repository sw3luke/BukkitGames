package me.ftbastler.BukkitGames;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BGCooldown extends JavaPlugin {
	
	private BGMain plugin;
	
	public BGCooldown(BGMain plugin) {
		
		this.plugin = plugin;
		
		BGFiles.abconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "abilities.yml"));
	}
	
	public void monkCooldown(final Player player) {
		
		TimerTask action = new TimerTask() {
			
			public void run() {
				
				plugin.listener.monkList.remove(player);
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(action, BGFiles.abconf.getInt("AB.13.Cooldown") * 1000);
	}
	
	public void thiefCooldown(final Player player) {
		
		TimerTask action = new TimerTask() {
			
			public void run() {
				
				plugin.listener.thiefList.remove(player);
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(action, BGFiles.abconf.getInt("AB.15.Cooldown") * 1000);
	}
	
	public void ghostCooldown(final Player player) {
		
		TimerTask action = new TimerTask() {
			
			public void run() {
				
				plugin.listener.ghostList.remove(player);
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(action, BGFiles.abconf.getInt("AB.16.Cooldown") * 1000);
	}
	
	public void showPlayerCooldown(final Player player, final Player[] players) {
		
		TimerTask action = new TimerTask() {
			
			public void run() {
				
				for (Player p : players) {
					if (p.getName().equals(player)) {
						continue;
					}
					p.showPlayer(player);
				}
				BGChat.printPlayerChat(player, BGFiles.abconf.getString("AB.16.visible"));
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(action, BGFiles.abconf.getInt("AB.16.Duration") * 1000);
	}
	
	public void viperCooldown(final Player player) {
		
		TimerTask action = new TimerTask() {
			
			public void run() {
				
				plugin.listener.viperList.remove(player);
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(action, BGFiles.abconf.getInt("AB.19.Duration") * 1000);
	}
	
	public void thorCooldown(final Player player) {
		
		TimerTask action = new TimerTask() {
			
			public void run(){
				
				plugin.listener.thorList.remove(player);
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(action, BGFiles.abconf.getInt("AB.11.Cooldown") * 1000);
	}
	
	public void fbattleCooldown() {
		
		TimerTask action = new TimerTask() {
			
			public void run() {
				
				BGChat.printInfoChat("Final battle! Teleported everybody to spawn.");
				plugin.endgame();
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(action, 60000);
	}
}

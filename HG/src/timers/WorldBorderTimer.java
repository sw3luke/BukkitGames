package timers;

import java.util.HashMap;
import java.util.Random;

import main.BGMain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import utilities.BGChat;
import utilities.enums.BorderType;

public class WorldBorderTimer {

	private static Integer shed_id = null;
	private static HashMap<Player, Location> locations = new HashMap<Player, Location>();

	public WorldBorderTimer() {
		shed_id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BGMain.instance, new Runnable() {
			
			@Override
			public void run() {
				if (BGMain.DENY_CHECK_WORLDBORDER)
					return;
				
				Random r = new Random();
				for(Player p : BGMain.getPlayers()) {						
					if (!BGMain.inBorder(p.getLocation(), BorderType.STOP)) {
						p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, (byte) 1);
						BGChat.printPlayerChat(p, "§c§l" + BGMain.instance.getConfig().getString("MESSAGE.WORLD_BORDER"));
						
						if(BGMain.isGameMaker(p) || BGMain.isSpectator(p) || BGMain.DENY_DAMAGE_PLAYER) {
							if(p.isInsideVehicle())
								p.getVehicle().eject();
							p.teleport(locations.containsKey(p) ? locations.get(p) : BGMain.getSpawn());
							locations.put(p, p.getLocation());
							continue;
						}
						
						p.damage(r.nextBoolean() ? 4 : 3);
						continue;
					}
					
					if(!BGMain.inBorder(p.getLocation(), BorderType.WARN) && BGMain.DENY_LOGIN) {
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, (byte) 1);
						BGChat.printPlayerChat(p, "§c§o" + "You are coming close to the world-border!");
					}
					
					if(BGMain.isGameMaker(p) || BGMain.isSpectator(p) || BGMain.DENY_DAMAGE_PLAYER)
						locations.put(p, p.getLocation());
				}
			}
			
		}, 0, 20*2);
	}
	
	public static void cancel() {
		if(shed_id != null) {
			Bukkit.getServer().getScheduler().cancelTask(shed_id);
			shed_id = null;
		}
	}
	
}

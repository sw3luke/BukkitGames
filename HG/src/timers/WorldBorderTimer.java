package timers;

import java.util.HashMap;
import java.util.Random;

import main.BGMain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import utilities.BGChat;

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
					if (!BGMain.inBorder(p.getLocation())) {
						BGChat.printPlayerChat(p, "§c" + BGMain.instance.getConfig().getString("MESSAGE.WORLD_BORDER"));
						
						if(BGMain.isGameMaker(p) || BGMain.isSpectator(p)) {
							p.teleport(locations.containsKey(p) ? locations.get(p) : BGMain.getSpawn());
							locations.put(p, p.getLocation());
							continue;
						}
						
						p.damage(r.nextBoolean() ? 2 : 1);
					}
					
					if(BGMain.isGameMaker(p) || BGMain.isSpectator(p))
						locations.put(p, p.getLocation());
				}
			}
			
		}, 0, 20*5);
	}
	
	public static void cancel() {
		if(shed_id != null) {
			Bukkit.getServer().getScheduler().cancelTask(shed_id);
			shed_id = null;
		}
	}
	
}

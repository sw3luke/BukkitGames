package timers;

import main.BGMain;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;

import utilities.BGChat;
import utilities.BGFBattle;

public class EndGameTimer {

	private static Integer shed_id = null;

	public EndGameTimer() {
		shed_id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BGMain.instance, new Runnable() {
			
			@Override
			public void run() {
				World w = Bukkit.getWorlds().get(0);
				w.setDifficulty(Difficulty.HARD);
				w.strikeLightning(BGMain.spawn.add(0.0D, 100.0D, 0.0D));
				BGChat.printInfoChat("Final battle! Teleported everybody to spawn.");
				BGMain.log.info("Game phase: 4 - Final");
				BGFBattle.teleportGamers(BGMain.getGamers());
			}
			
		}, 0, 20*60);
	}
	
	public static void cancel() {
		if(shed_id != null) {
			Bukkit.getServer().getScheduler().cancelTask(shed_id);
			shed_id = null;
		}
	}
	
}

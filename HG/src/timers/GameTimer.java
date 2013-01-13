package timers;

import main.BGMain;

import org.bukkit.Bukkit;

import utilities.BGChat;
import utilities.BGFBattle;
import utilities.BGFeast;
import utilities.BGVanish;

public class GameTimer {
	
	private static Integer shed_id = null;

	public GameTimer() {
		shed_id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BGMain.instance, new Runnable() {
			
			@Override
			public void run() {
				BGMain.GAME_RUNNING_TIME++;

				BGMain.checkwinner();
				BGVanish.updateVanished();

				if ((BGMain.GAME_RUNNING_TIME % 5 != 0)
						& (BGMain.GAME_RUNNING_TIME % 10 != 0)) {
					if(BGMain.SHOW_TIPS) {	
						BGChat.printTipChat();
					}
					if(BGMain.ADV_CHAT_SYSTEM && !BGMain.SHOW_TIPS) {
						BGChat.updateChat();
					}
				}

				if(BGMain.FEAST) {
					if (BGMain.GAME_RUNNING_TIME == BGMain.FEAST_SPAWN_TIME - 3)
						BGFeast.announceFeast(3);
					if (BGMain.GAME_RUNNING_TIME == BGMain.FEAST_SPAWN_TIME - 2)
						BGFeast.announceFeast(2);
					if (BGMain.GAME_RUNNING_TIME == BGMain.FEAST_SPAWN_TIME - 1)
						BGFeast.announceFeast(1);
					
					if (BGMain.GAME_RUNNING_TIME == BGMain.FEAST_SPAWN_TIME)
						BGFeast.spawnFeast();
				}

				if (BGMain.GAME_RUNNING_TIME == (BGMain.END_GAME_TIME - 1)) {
					if(BGMain.END_GAME) {
						BGChat.printInfoChat("Final battle ahead. Teleporting everybody to spawn in 1 minute!");
						
						BGMain.END_GAME = false;
						
						BGFBattle.createBattle();
						new EndGameTimer();
					}			
				}


				if (BGMain.GAME_RUNNING_TIME.intValue() == BGMain.MAX_GAME_RUNNING_TIME
						.intValue() - 1) {
					BGChat.printInfoChat("Final battle! 1 minute left.");
				}

				if (BGMain.GAME_RUNNING_TIME.intValue() >= BGMain.MAX_GAME_RUNNING_TIME
						.intValue())
					Bukkit.getServer().shutdown();
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
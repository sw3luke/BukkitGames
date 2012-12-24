package utilities;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import main.BGMain;

public class BGFBattle {

	@SuppressWarnings("unused")
	private static BGMain plugin;
	private static Block mainBlock;
	private static Logger log = BGMain.getPluginLogger();
	private static ArrayList<Location> fbblocks = new ArrayList<Location>();
	private static Boolean spawned = false;
	
	public BGFBattle(BGMain plugin) {
		BGFBattle.plugin = plugin;
	}
	
	public static void createBattle() {
		mainBlock = BGMain.getRandomLocation().subtract(0, 1, 0).getBlock();
		Location loc = mainBlock.getLocation();
		spawned = true;
		
		//-2: new layer; -1: new row; 0: Air
		//1: Mossy Cobblestone 2: GOLD_BLOCK
		//3: Glowstone 4: Stone
		Integer[] co = {
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 2, 2, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 2, 2, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2,
				
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2,
				
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2,
				
				3, 2, 3, 2, 3, 2, 3, 2, 3, 2, -1,
				2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1,
				3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1,
				2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1,
				3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1,
				2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1,
				3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1,
				2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1,
				3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1,
				2, 3, 2, 3, 2, 3, 2, 3, 2, 3, -2,
				
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2,
				
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 3, 3, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 3, 3, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2
		};
		
		for (Integer i: co) {
			
			Material m = Material.AIR;
			switch(i) {
			case 0:
				m = Material.AIR;
				break;
			case 1:
				m = Material.MOSSY_COBBLESTONE;
				break;
			case 2:
				m = Material.GOLD_BLOCK;
				break;
			case 3:
				m = Material.GLOWSTONE;
				break;
			case 4:
				m = Material.STONE;
				break;
			case -1:
				break;
			case -2:
				break;
			default:
				log.warning("Illegal integer found while creating BGFBattle: " + i.toString());
				break;
			}
			
			if(i == -1) {
				loc.add(0, 0, 1);
				loc.subtract(10, 0, 0);
			} else if(i == -2) {
				loc.add(0, 1, 0);
				loc.subtract(10, 0, 9);
			} else {
				loc.getBlock().setType(m);
				fbblocks.add(loc.getBlock().getLocation());
				loc.add(1, 0, 0);
			}
		}
	}
	
	public static void teleportGamers(Player[] players) {
		for(Player p : players) {
			Random r = new Random();
			Location loc = mainBlock.getLocation().add(r.nextInt(8)+1, 1.5, r.nextInt(8)+1);
			p.teleport(loc);
		}
	}
	
	public static boolean isBattleBlock(Block block) {
		if(!spawned)
			return false;
		
		return fbblocks.contains(block.getLocation());
	}
	
	public static Block getMainBlock() {
		return mainBlock;
	}
}

package me.ftbastler.BukkitGames;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BGCornucopia {

	private static BGMain plugin;
	private static Block mainBlock;
	private static Integer radius = 10;
	private static Logger log = Logger.getLogger("Minecraft");
	
	public BGCornucopia(BGMain plugin) {
		BGCornucopia.plugin = plugin;
	}
	
	public static void createCorn() {
		BGCornucopia.mainBlock = BGCornucopia.getCornSpawnBlock();
		mainBlock.setType(Material.DIAMOND_BLOCK);
		removeAbove(mainBlock);
		
		createFloor(Material.GOLD_BLOCK);
	}
	
	private static Block getCornSpawnBlock() {
		Location loc = plugin.getSpawn();
		loc.subtract(0, 2.5, 0);
		Block b = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		return b;
	}
	
	private static void createFloor(Material m) {
		Location loc = mainBlock.getLocation();
		Integer r = radius;
	               
		log.info("[BukkitGames] Generating the cornucopia.");	    
	    
	    for (double x = -r; x <= r; x++) {
	        for (double z = -r; z <= r; z++) {
	        	Location l = new Location(Bukkit.getServer().getWorld("world"), loc.getX() + x, loc.getY(), loc.getZ() + z);
	        	if(l.distance(loc) <= r && l.getBlock().getType() != Material.DIAMOND_BLOCK) {
	        		removeAbove(l.getBlock());
	        		l.getBlock().setType(m);
	        	}
	        }
	    }
	}
	
	public static Boolean isCornucopiaBlock(Block b) {		
		if(!plugin.CORNUCOPIA)
			return false;

		if(b.getLocation().distance(mainBlock.getLocation()) <= radius + 3)
			return true;

		return false;
	}
	
	
	public static void removeAbove(Block block) {
		Location loc = block.getLocation();
		loc.setY(loc.getY()+1);
		Block newBlock = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		while(newBlock.getType() != Material.AIR) {
			newBlock.setType(Material.AIR);
			loc.setY(loc.getY()+1);
			newBlock = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		}
	}
	
	
	public static void spawnChests() {
		List<String> items = BGFiles.cornconf.getStringList("ITEMS");
		for(String item : items) {
			String[] oneitem = item.split(",");
			Random r = new Random();
			String itemid = oneitem[0];
			Integer minamount = Integer.parseInt(oneitem[1]);
			Integer maxamount = Integer.parseInt(oneitem[2]);
			Integer amount = 0;
			Boolean force = Boolean.parseBoolean(oneitem[3]);
			Boolean spawn = force;
			Integer id = null;
			Short durability = null;
			
			if(!force)
				spawn = r.nextBoolean();
			
			if(!spawn)
				continue;
			
			if (item.contains(":")) {
				String[] it = itemid.split(":");
				id = Integer.parseInt(it[0]);
				durability = Short.parseShort(it[1]);
			} else {
				id = Integer.parseInt(itemid);
			}
			
			ItemStack i = new ItemStack(id, 1);
			
			if(durability != null)
				i.setDurability(durability);
			
			if(oneitem.length == 6)
				i.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem[4])), Integer.parseInt(oneitem[5]));
						
			Integer ra = radius;
			Location c = mainBlock.getLocation();
			c.add(-(ra/2) + r.nextInt(ra), 1, -(ra/2) + r.nextInt(ra));
			
			if(maxamount == minamount)
				amount = maxamount;
			else
				amount = minamount + r.nextInt(maxamount - minamount + 1);
			
			if(plugin.CORNUCOPIA_CHESTS) {
				while(c.getBlock().getType() == Material.CHEST) {
					c = mainBlock.getLocation();
					c.add(-8 + r.nextInt(16), 1, -8 + r.nextInt(16));
				}
				
				while(amount > 0) {
					c.getBlock().setType(Material.CHEST);
					Chest chest = (Chest) c.getBlock().getState();
					chest.getInventory().addItem(i);
					chest.update();
					amount--;
				}
			} else {
				while(amount > 0) {
					Bukkit.getServer().getWorld("world").dropItemNaturally(c, i).setPickupDelay(20 * 5);
					amount--;
				}
			}
		}
	}
 }

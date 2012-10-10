package me.ftbastler.BukkitGames;

import java.text.DecimalFormat;
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

public class BGFeast {

	private static BGMain plugin;
	private static Block mainBlock = null;
	private static Integer radius = 8;
	private static Logger log = BGMain.getPluginLogger();
	private static Boolean spawned = false;
	
	public BGFeast(BGMain plugin) {	
		BGFeast.plugin = plugin;
	}
	
	public static void announceFeast(Integer time) {
		if(mainBlock == null) {
			do {
				mainBlock = BGMain.getRandomLocation().subtract(0, 1, 0).getBlock();
			} while (!plugin.inBorder(mainBlock.getLocation()));
			mainBlock.setType(Material.NETHERRACK);
			removeAbove(mainBlock);
			createFeast(Material.SOUL_SAND);
			spawned = true;
		}
		
		String s = "";
		if(time > 1)
			s = "s";
		
		DecimalFormat df = new DecimalFormat("##.#");
		BGChat.printInfoChat("Feast will spawn in " + time + " minute" + s + " at X: " + df.format(mainBlock.getLocation().getX()) + " | Y: " + df.format(mainBlock.getLocation().getY()) + " | Z: " + df.format(mainBlock.getLocation().getZ()));
	}
	
	public static void spawnFeast() {
		if(mainBlock == null)
			announceFeast(0);
		
		DecimalFormat df = new DecimalFormat("##.#");
		BGChat.printInfoChat("Feast spawned at X: " + df.format(mainBlock.getLocation().getX()) + " | Y: " + df.format(mainBlock.getLocation().getY()) + " | Z: " + df.format(mainBlock.getLocation().getZ()));
		
		List<String> items = BGFiles.feastconf.getStringList("ITEMS");
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
			
			if(plugin.FEAST_CHESTS) {
				while(c.getBlock().getType() == Material.CHEST) {
					c = mainBlock.getLocation();
					c.add(-radius + r.nextInt(16), 1, -radius + r.nextInt(16));
				}
				
				c.getBlock().setType(Material.CHEST);
				Chest chest = (Chest) c.getBlock().getState();
				
				while(amount > 0) {
					Integer slot = r.nextInt(27);
					if(chest.getInventory().getItem(slot) != null)
						i.setAmount(i.getAmount() + 1);
					chest.getInventory().setItem(slot, i);
					amount--;
				}
				
				chest.update();
			} else {
				while(amount > 0) {
					Bukkit.getServer().getWorld("world").dropItemNaturally(c, i).setPickupDelay(20 * 5);
					amount--;
				}
			}
		}
	}
	
	public static Boolean isFeastBlock(Block b) {		
		if(!plugin.FEAST || !spawned)
			return false;

		if(b.getLocation().distance(mainBlock.getLocation()) <= radius + 3)
			return true;

		return false;
	}
	
	private static void createFeast(Material m) {
		Location loc = mainBlock.getLocation();
		Integer r = radius;
	               
		log.info("Generating feast.");	    
	    
	    for (double x = -r; x <= r; x++) {
	        for (double z = -r; z <= r; z++) {
	        	Location l = new Location(Bukkit.getServer().getWorld("world"), loc.getX() + x, loc.getY(), loc.getZ() + z);
	        	if(l.distance(loc) <= r && l.getBlock().getType() != Material.NETHERRACK) {
	        		removeAbove(l.getBlock());
	        		l.getBlock().setType(m);
	        	}
	        }
	    }
	}
	
	public static void removeAbove(Block block) {
		Location loc = block.getLocation();
		loc.setY(loc.getY()+1);
		Block newBlock = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		while(loc.getY() < Bukkit.getServer().getWorld("world").getMaxHeight()) {
			newBlock.setType(Material.AIR);
			loc.setY(loc.getY()+1);
			newBlock = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		}
	}
	
}

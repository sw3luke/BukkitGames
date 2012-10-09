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
	private static Logger log = BGMain.getPluginLogger();
	private static Chest[] chests = new Chest[8];
	
	public BGCornucopia(BGMain plugin) {
		BGCornucopia.plugin = plugin;
	}
	
	public static void createCorn() {
		BGCornucopia.mainBlock = BGCornucopia.getCornSpawnBlock();
		mainBlock.setType(Material.DIAMOND_BLOCK);
		removeAbove(mainBlock);
		
		createFloor(Material.GOLD_BLOCK);
		
		if(plugin.CORNUCOPIA_CHESTS)
			createPile(Material.DIAMOND_BLOCK);
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
	               
		log.info("Generating the cornucopia.");	    
	    
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
	
	private static void createPile(Material m) {
		
		Location loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		
		Block block = loc.getBlock();
		block.setType(m);
		for(int i = 1; i < 4; i++) {
			loc.setY(loc.getY() +1);
			Bukkit.getServer().getWorld("world").getBlockAt(loc).setType(m);
		}
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setX(loc.getX()+1);
		Bukkit.getServer().getWorld("world").getBlockAt(loc).setType(m);
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setX(loc.getX()-1);
		Bukkit.getServer().getWorld("world").getBlockAt(loc).setType(m);
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setZ(loc.getZ()+1);
		Bukkit.getServer().getWorld("world").getBlockAt(loc).setType(m);
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setZ(loc.getZ()-1);
		Bukkit.getServer().getWorld("world").getBlockAt(loc).setType(m);
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
		while(loc.getY() < 256) {
			newBlock.setType(Material.AIR);
			loc.setY(loc.getY()+1);
			newBlock = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		}
	}
	
	public static void spawnChests() {
		Location loc;
		Block block;
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setX(loc.getX()+1);
		block = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		block.setType(Material.CHEST);
		chests[0] = (Chest) block.getState();
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setX(loc.getX()+1);
		loc.setZ(loc.getZ()+1);
		block = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		block.setType(Material.CHEST);
		chests[1] = (Chest) block.getState();
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setX(loc.getX()+1);
		loc.setZ(loc.getZ()-1);
		block = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		block.setType(Material.CHEST);
		chests[2] = (Chest) block.getState();
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setX(loc.getX()-1);
		block = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		block.setType(Material.CHEST);
		chests[3] = (Chest) block.getState();
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setX(loc.getX()-1);
		loc.setZ(loc.getZ()+1);
		block = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		block.setType(Material.CHEST);
		chests[4] = (Chest) block.getState();
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setX(loc.getX()-1);
		loc.setZ(loc.getZ()-1);
		block = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		block.setType(Material.CHEST);
		chests[5] = (Chest) block.getState();
		
		
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setZ(loc.getZ()+1);
		block = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		block.setType(Material.CHEST);
		chests[6] = (Chest) block.getState();
		
		loc = mainBlock.getLocation();
		loc.setY(loc.getY()+1);
		loc.setZ(loc.getZ()-1);
		block = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		block.setType(Material.CHEST);
		chests[7] = (Chest) block.getState();
	}
	
	public static void spawnItems() {
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
				spawnChests();
				
				Chest chest = chests[r.nextInt(8)];
				
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
 }

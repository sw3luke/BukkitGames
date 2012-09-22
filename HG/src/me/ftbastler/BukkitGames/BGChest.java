package me.ftbastler.BukkitGames;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BGChest {

	
	private BGMain plugin;
	
	public BGChest(BGMain plugin){
		
		this.plugin = plugin;
	}
	
	public void spawnChest() {
		
		Location loc;
		do{
			
			loc = BGMain.getRandomLocation();
		}while(!plugin.inBorder(loc));
		
		spawnChest(loc);
	}
	
	public void spawnChest(Location l) {
		l.getBlock().setType(Material.CHEST);
		Chest c = (Chest) l.getBlock().getState();
				
		List<String> items = BGFiles.chestconf.getStringList("ITEMS");
		for(String item : items) {
			Random r = new Random();
			String[] oneitem = item.split(",");
			if(Boolean.parseBoolean(oneitem[3]) || r.nextBoolean()) {
				ItemStack i;
				
				Integer id = Integer.valueOf(Integer.parseInt(oneitem[0]));
				Integer minAmount = Integer.valueOf(Integer.parseInt(oneitem[1]));
				Integer maxAmount = Integer.valueOf(Integer.parseInt(oneitem[2]));
				
				Integer amount = r.nextInt((maxAmount+1)-minAmount) + minAmount;
				
				i = new ItemStack(id.intValue(), amount.intValue());
				
				if(oneitem.length == 6) {
					i.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem[4])),
							Integer.parseInt(oneitem[5]));
				}
				
				c.getInventory().addItem(new ItemStack[] { i });
			}
		}
		
		c.update(true);
		DecimalFormat df = new DecimalFormat("##.#");
		BGChat.printInfoChat("Chest spawned at X: " + df.format(l.getX())
				+ " | Y: " + df.format(l.getY()) + " | Z: "
				+ df.format(l.getZ()));
	}
	
	public void spawnTable() {
		
		Location loc;
		
		do{
			
			loc = BGMain.getRandomLocation();
		}while(!plugin.inBorder(loc));
		
		spawnTable(loc);
	}
	
	public void spawnTable(Location l) {
		l.getBlock().setType(Material.ENCHANTMENT_TABLE);
		DecimalFormat df = new DecimalFormat("##.#");
		BGChat.printInfoChat("Enchantment Table spawned at X: "
				+ df.format(l.getX()) + " | Y: " + df.format(l.getY())
				+ " | Z: " + df.format(l.getZ()));

	}
}

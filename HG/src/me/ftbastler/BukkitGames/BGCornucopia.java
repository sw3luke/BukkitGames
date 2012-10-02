package me.ftbastler.BukkitGames;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BGCornucopia {

	private BGMain plugin;
	
	private final Block mainBlock;
	
	public BGCornucopia(BGMain plugin) {
		
		this.plugin = plugin;
		
		mainBlock= plugin.getServer().getWorld("world").getBlockAt(plugin.getCornSpawn());
	}
	
	public void createCorn() {
		
		mainBlock.setType(Material.GOLD_BLOCK);
		removeAbove(mainBlock);
		
		createKreuz();
	}
	
	public void createBigFloor(){
		
		Location loc = mainBlock.getLocation();
		loc.setZ(loc.getZ()+1);
		Block nextBlock;
		for(int i=1; i<=3; i++){
			
			loc.setX(loc.getX()+1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		loc.setZ(loc.getZ()+1);
		for(int i=1; i<=3; i++){
			
			loc.setX(loc.getX()-1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		loc.setZ(mainBlock.getLocation().getZ()-1);
		for(int i=1; i<=3; i++){
			
			loc.setX(loc.getX()+1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		loc.setZ(loc.getZ()-1);
		for(int i=1; i<=3; i++){
			
			loc.setX(loc.getX()-1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		
		loc = mainBlock.getLocation();
		loc.setX(loc.getX()+1);
		for(int i=1; i<=3; i++){
			
			loc.setZ(loc.getZ()+1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		loc.setX(loc.getX()+1);
		for(int i=1; i<=3; i++){
			
			loc.setZ(loc.getZ()-1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		loc.setX(loc.getX()-1);
		for(int i=1; i<=3; i++){
			
			loc.setZ(loc.getZ()+1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		loc.setX(loc.getX()-1);
		for(int i=1; i<=3; i++){
			
			loc.setZ(loc.getZ()-1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
	}
	
	public void createKreuz() {
		
		Location loc = mainBlock.getLocation();
		Block nextBlock;
		for(int i=1; i<=3; i++){
			
			loc.setX(loc.getX()+1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		for(int i=1; i<=3; i++){
			
			loc.setX(loc.getX()-1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		for(int i=1; i<=3; i++){
			
			loc.setZ(loc.getZ()+1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
		
		loc = mainBlock.getLocation();
		for(int i=1; i<=3; i++){
			
			loc.setZ(loc.getZ()-1); 
			nextBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
			
			nextBlock.setType(Material.GOLD_BLOCK);
			removeAbove(nextBlock);
		}
	}
	
	public void removeAbove(Block block) {
		
		Location loc = block.getLocation();
		
		loc.setY(loc.getY()+1);
		
		Block newBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
		
		while(newBlock.getType() != Material.AIR) {
			
			newBlock.setType(Material.AIR);
			
			loc.setY(loc.getY()+1);
			
			newBlock = plugin.getServer().getWorld("world").getBlockAt(loc);
		}
	}
}

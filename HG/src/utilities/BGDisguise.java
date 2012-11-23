package utilities;

import java.util.ArrayList;

import main.BGMain;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

public class BGDisguise extends JavaPlugin{

	
	private BGMain plugin;
	
	public DisguiseCraftAPI dcapi;
	
	public ArrayList<Player> disList = new ArrayList<Player>();
	
	public BGDisguise(BGMain plugin) {
		
		this.plugin = plugin;
		dcapi = DisguiseCraft.getAPI();
	}
	
	public void disguise(Player player, DisguiseType mob) {
		
		if (!dcapi.isDisguised(player)) {
			Disguise dis = new Disguise(dcapi.newEntityID(), mob);
			dcapi.disguisePlayer(player, dis);
			BGChat.printPlayerChat(player, BGFiles.abconf.getString("AB.17.disguise"));
			hidePlayer(player);
			disList.add(player);
		}else {
			Disguise dis = dcapi.getDisguise(player);
			dis.setType(mob);
			dcapi.changePlayerDisguise(player, dis);
			hidePlayer(player);
		}
	}
	
	public void unDisguise(Player player) {
		
		if (dcapi.isDisguised(player)) {
			dcapi.undisguisePlayer(player);
			BGChat.printPlayerChat(player, BGFiles.abconf.getString("AB.17.undisguise"));
			showPlayer(player);
			disList.remove(player);
		}
	}
	
	public void hidePlayer(Player player) {
		
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			if (p.getName().equals(player.getName())) {
				continue;
			}
			p.hidePlayer(player);
		}
	}
	
	public void showPlayer(Player player) {
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			p.showPlayer(player);
		}
	}
	
public static DisguiseType getDisguiseType(EntityType entity) {
		
		if (entity == EntityType.BLAZE) {
			
			return DisguiseType.Blaze;
		}
		if (entity == EntityType.CAVE_SPIDER) {
			
			return DisguiseType.CaveSpider;
		}
		if (entity == EntityType.CHICKEN) {
			
			return DisguiseType.Chicken;
		}
		if (entity == EntityType.COW) {
			
			return DisguiseType.Cow;
		}
		if (entity == EntityType.CREEPER) {
			
			return DisguiseType.Creeper;
		}
		if (entity == EntityType.ENDER_DRAGON) {
			
			return DisguiseType.EnderDragon;
		}
		if (entity == EntityType.ENDERMAN) {
			
			return DisguiseType.Enderman;
		}
		if (entity == EntityType.GHAST) {
			
			return DisguiseType.Ghast;
		}
		if (entity == EntityType.GIANT) {
			
			return DisguiseType.Giant;
		}
		if (entity == EntityType.IRON_GOLEM) {
			
			return DisguiseType.IronGolem;
		}
		if (entity == EntityType.MAGMA_CUBE) {
			
			return DisguiseType.MagmaCube;
		}
		if (entity == EntityType.MUSHROOM_COW) {
			
			return DisguiseType.MushroomCow;
		}
		if (entity == EntityType.OCELOT) {
			
			return DisguiseType.Ocelot;
		}
		if (entity == EntityType.PIG) {
			
			return DisguiseType.Pig;
		}
		if (entity == EntityType.PIG_ZOMBIE) {
			
			return DisguiseType.PigZombie;
		}
		if (entity == EntityType.SHEEP) {
			
			return DisguiseType.Sheep;
		}
		if (entity == EntityType.SILVERFISH) {
			
			return DisguiseType.Silverfish;
		}
		if (entity == EntityType.SKELETON) {
			
			return DisguiseType.Skeleton;
		}
		if (entity == EntityType.SLIME) {
			
			return DisguiseType.Slime;
		}
		if (entity == EntityType.SNOWMAN) {
			
			return DisguiseType.Snowman;
		}
		if (entity == EntityType.SPIDER) {
			
			return DisguiseType.Spider;
		}
		if (entity == EntityType.SQUID) {
			
			return DisguiseType.Squid;
		}
		if (entity == EntityType.VILLAGER) {
			
			return DisguiseType.Villager;
		}
		if (entity == EntityType.WOLF) {
			
			return DisguiseType.Wolf;
		}
		if (entity == EntityType.ZOMBIE) {
			
			return DisguiseType.Zombie;
		}
		if (entity == EntityType.WITCH) {
			
			return DisguiseType.Witch;
		}
		if (entity == EntityType.BAT) {
			
			return DisguiseType.Bat;
		}
		if (entity == EntityType.WITHER) {
			
			return DisguiseType.Wither;
		}
		return null;
	}
}

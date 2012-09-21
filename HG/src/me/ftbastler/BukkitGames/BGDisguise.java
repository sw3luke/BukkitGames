package me.ftbastler.BukkitGames;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import pgDev.bukkit.DisguiseCraft.Disguise;
import pgDev.bukkit.DisguiseCraft.Disguise.MobType;
import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class BGDisguise extends JavaPlugin{

	
	private BGMain plugin;
	
	public DisguiseCraftAPI dcapi;
	
	public ArrayList<Player> disList = new ArrayList<Player>();
	
	public BGDisguise(BGMain plugin) {
		
		this.plugin = plugin;
		
		dcapi = DisguiseCraft.getAPI();
	}
	
	public void disguise(Player player, MobType mob) {
		
		if (!dcapi.isDisguised(player)) {
			Disguise dis = new Disguise(dcapi.newEntityID(), mob);
			dcapi.disguisePlayer(player, dis);
			BGChat.printPlayerChat(player, BGFiles.abconf.getString("AB.17.disguise"));
			hidePlayer(player);
			disList.add(player);
			updateDisguise(player);
		}else {
			Disguise dis = dcapi.getDisguise(player);
			dis.setMob(mob);
			dcapi.changePlayerDisguise(player, dis);
			hidePlayer(player);
			updateDisguise(player);
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
	
	public void updateDisguise(final Player player) {
		
		TimerTask action = new TimerTask() {
			
			public void run() {
				
				if(disList.contains(player)) {
					
					if(!BGKit.hasAbility(player, 17)) {
						
						unDisguise(player);
						return;
					}
					
					Disguise dis = dcapi.getDisguise(player);
					dis.setMob(dis.mob);
					dcapi.changePlayerDisguise(player, dis);
					hidePlayer(player);
					updateDisguise(player);
				}
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(action, 10000);
	}
	
public static MobType getMobType(EntityType entity) {
		
		if (entity == EntityType.BLAZE) {
			
			return MobType.Blaze;
		}
		if (entity == EntityType.CAVE_SPIDER) {
			
			return MobType.CaveSpider;
		}
		if (entity == EntityType.CHICKEN) {
			
			return MobType.Chicken;
		}
		if (entity == EntityType.COW) {
			
			return MobType.Cow;
		}
		if (entity == EntityType.CREEPER) {
			
			return MobType.Creeper;
		}
		if (entity == EntityType.ENDER_DRAGON) {
			
			return MobType.EnderDragon;
		}
		if (entity == EntityType.ENDERMAN) {
			
			return MobType.Enderman;
		}
		if (entity == EntityType.GHAST) {
			
			return MobType.Ghast;
		}
		if (entity == EntityType.GIANT) {
			
			return MobType.Giant;
		}
		if (entity == EntityType.IRON_GOLEM) {
			
			return MobType.IronGolem;
		}
		if (entity == EntityType.MAGMA_CUBE) {
			
			return MobType.MagmaCube;
		}
		if (entity == EntityType.MUSHROOM_COW) {
			
			return MobType.MushroomCow;
		}
		if (entity == EntityType.OCELOT) {
			
			return MobType.Ocelot;
		}
		if (entity == EntityType.PIG) {
			
			return MobType.Pig;
		}
		if (entity == EntityType.PIG_ZOMBIE) {
			
			return MobType.PigZombie;
		}
		if (entity == EntityType.SHEEP) {
			
			return MobType.Sheep;
		}
		if (entity == EntityType.SILVERFISH) {
			
			return MobType.Silverfish;
		}
		if (entity == EntityType.SKELETON) {
			
			return MobType.Skeleton;
		}
		if (entity == EntityType.SLIME) {
			
			return MobType.Slime;
		}
		if (entity == EntityType.SNOWMAN) {
			
			return MobType.Snowman;
		}
		if (entity == EntityType.SPIDER) {
			
			return MobType.Spider;
		}
		if (entity == EntityType.SQUID) {
			
			return MobType.Squid;
		}
		if (entity == EntityType.VILLAGER) {
			
			return MobType.Villager;
		}
		if (entity == EntityType.WOLF) {
			
			return MobType.Wolf;
		}
		if (entity == EntityType.ZOMBIE) {
			
			return MobType.Zombie;
		}
		return null;
	}
}

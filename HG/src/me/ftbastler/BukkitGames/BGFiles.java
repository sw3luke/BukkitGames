package me.ftbastler.BukkitGames;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BGFiles {
	
	@SuppressWarnings("unused")
	private static BGMain plugin;
	
	public static FileConfiguration abconf;
	public static FileConfiguration bookconf;
	public static FileConfiguration config;
	public static FileConfiguration dsign;
	public static FileConfiguration kitconf;
	public static FileConfiguration messageconf;
	public static FileConfiguration rewardconf;

	public BGFiles(BGMain main) {
		
		plugin = main;
	}
	
	public void loadFiles() throws Exception {
		
		//Create files if not exist
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		File kitFile = new File(plugin.getDataFolder(), "kit.yml");
		File leaderboardFile = new File(plugin.getDataFolder(), "leaderboard.yml");
		File rewardFile = new File(plugin.getDataFolder(), "reward.yml");
		File deathSignFile = new File(plugin.getDataFolder(), "deathsign.yml");
		File abilitiesFile = new File(plugin.getDataFolder(), "abilities.yml");
		File bookFile = new File(plugin.getDataFolder(), "book.yml");
		File messagesFile = new File(plugin.getDataFolder(), "messages.yml");

		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("config.yml"), configFile);
			plugin.log.info("[BukkitGames] 'config.yml' didn't exist. Created it.");
		}
		if (!kitFile.exists()) {
			kitFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("kit.yml"), kitFile);
			plugin.log.info("[BukkitGames] 'kit.yml' didn't exist. Created it.");
		}
		if (!leaderboardFile.exists()) {
			leaderboardFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("leaderboard.yml"), leaderboardFile);
			plugin.log.info("[BukkitGames] 'leaderboard.yml' didn't exist. Created it.");
		}
		if (!rewardFile.exists()) {
			rewardFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("reward.yml"), rewardFile);
			plugin.log.info("[BukkitGames] 'reward.yml' didn't exist. Created it.");
		}
		if (!deathSignFile.exists()) {
			deathSignFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("deathsign.yml"), deathSignFile);
			plugin.log.info("[BukkitGames] 'deathsign.yml' didn't exist. Created it.");
		}
		if (!abilitiesFile.exists()) {
			abilitiesFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("abilities.yml"), abilitiesFile);
			plugin.log.info("[BukkitGames] 'abilities.yml' didn't exist. Created it.");
		}
		if (!bookFile.exists()) {
			bookFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("book.yml"), abilitiesFile);
			plugin.log.info("[BukkitGames] 'book.yml' didn't exist. Created it.");
		}
		if (!messagesFile.exists()) {
			messagesFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("messages.yml"), abilitiesFile);
			plugin.log.info("[BukkitGames] 'messages.yml' didn't exist. Created it.");
		}
		
		
		//Save files in variables
		abconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "abilities.yml"));
		
		bookconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "book.yml"));
		
		config = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "config.yml"));
		
		dsign = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "deathsign.yml"));
		
		kitconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "kit.yml"));
		
		messageconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "messages.yml"));
		
		rewardconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "reward.yml"));
	}
}

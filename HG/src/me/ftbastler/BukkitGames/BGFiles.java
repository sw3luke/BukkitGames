package me.ftbastler.BukkitGames;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BGFiles {
	
	private static BGMain plugin;
	
	Logger log = Logger.getLogger("Minecraft");
	
	static FileConfiguration abconf;
	static FileConfiguration bookconf;
	static FileConfiguration config;
	static FileConfiguration dsign;
	static FileConfiguration kitconf;
	static FileConfiguration messageconf;
	static FileConfiguration rewardconf;
	static FileConfiguration chestconf;
	static FileConfiguration cornconf;
	static FileConfiguration feastconf;

	public BGFiles(BGMain main) {
		
		plugin = main;
		
		try{
			loadFiles();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadFiles() throws Exception {
		
		//Create files if not exist
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		File kitFile = new File(plugin.getDataFolder(), "kit.yml");
		File leaderboardFile = new File(plugin.getDataFolder(), "leaderboard.yml");
		File rewardFile = new File(plugin.getDataFolder(), "reward.yml");
		File deathSignFile = new File(plugin.getDataFolder(), "deathsign.yml");
		File abilitiesFile = new File(plugin.getDataFolder(), "abilities.yml");
		File bookFile = new File(plugin.getDataFolder(), "book.yml");
		File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
		File chestFile = new File(plugin.getDataFolder(), "chest.yml");
		File cornFile = new File(plugin.getDataFolder(), "cornucopia.yml");
		File feastFile = new File(plugin.getDataFolder(), "feast.yml");

		Integer creation = 0;
		
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("config.yml"), configFile);
			creation++;
		}
		if (!kitFile.exists()) {
			kitFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("kit.yml"), kitFile);
			creation++;
		}
		if (!leaderboardFile.exists()) {
			leaderboardFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("leaderboard.yml"), leaderboardFile);
			creation++;
		}
		if (!rewardFile.exists()) {
			rewardFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("reward.yml"), rewardFile);
			creation++;
		}
		if (!deathSignFile.exists()) {
			deathSignFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("deathsign.yml"), deathSignFile);
			creation++;
		}
		if (!abilitiesFile.exists()) {
			abilitiesFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("abilities.yml"), abilitiesFile);
			creation++;
		}
		if (!bookFile.exists()) {
			bookFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("book.yml"), bookFile);
			creation++;
		}
		if (!messagesFile.exists()) {
			messagesFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("messages.yml"), messagesFile);
			creation++;
		}
		if(!chestFile.exists()) {
			chestFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("chest.yml"), chestFile);
			creation++;
		}
		if(!cornFile.exists()) {
			cornFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("cornucopia.yml"), cornFile);
			creation++;
		}
		if(!feastFile.exists()) {
			feastFile.getParentFile().mkdirs();
			plugin.copy(plugin.getResource("feast.yml"), feastFile);
			creation++;
		}
		
		if(creation > 0)
			log.info("[BukkitGames] Created " + creation + " files.");
		
		
		//Save files in variables
		log.info("[BukkitGames] Loading abilities.yml");
		abconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "abilities.yml"));
		log.info("[BukkitGames] Loading book.yml");
		bookconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "book.yml"));
		log.info("[BukkitGames] Loading config.yml");
		config = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "config.yml"));
		log.info("[BukkitGames] Loading deathsign.yml");
		dsign = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "deathsign.yml"));
		log.info("[BukkitGames] Loading kit.yml");
		kitconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "kit.yml"));
		log.info("[BukkitGames] Loading messages.yml");
		messageconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "messages.yml"));
		log.info("[BukkitGames] Loading reward.yml");
		rewardconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "reward.yml"));
		log.info("[BukkitGames] Loading chest.yml");
		chestconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "chest.yml"));
		log.info("[BukkitGames] Loading cornucopia.yml");
		cornconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "cornucopia.yml"));
		log.info("[BukkitGames] Loading feast.yml");
		feastconf = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "feast.yml"));
	}
}

package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import commands.BGConsole;
import commands.BGPlayer;

import utilities.BGChat;
import utilities.BGCooldown;
import utilities.BGCornucopia;
import utilities.BGDisguise;
import utilities.BGFBattle;
import utilities.BGFeast;
import utilities.BGFiles;
import utilities.BGKit;
import utilities.BGReward;
import utilities.BGSign;
import utilities.BGVanish;
import utilities.Border;
import utilities.Metrics;

import events.BGListener;

public class BGMain extends JavaPlugin {
	Logger log;
	
	public BGChat chat;
	public BGConsole consolecmd;
	public BGPlayer player;
	public BGCooldown cooldown;
	public BGDisguise dis;
	public BGKit kit;
	public BGListener listener;
	public BGReward reward;
	public BGSign sign;
	public BGVanish vanish;
	public Border border;
	public BGFiles files;
	public BGCornucopia cornucopia;
	public BGFeast feasts;
	private static BGMain plugin;

	public String HELP_MESSAGE = null;
	public String SERVER_FULL_MSG = "";
	public String WORLD_BORDER_MSG = "";
	public String GAME_IN_PROGRESS_MSG = "";
	public String KIT_BUY_WEB = "";
	public String NEW_WINNER = "";
	public String MOTD_PROGRESS_MSG = "";
	public String MOTD_COUNTDOWN_MSG = "";
	public String NO_KIT_MSG = "";
	public String SERVER_TITLE = null;
	public Boolean ADV_CHAT_SYSTEM = true;
	public static Integer COUNTDOWN_SECONDS = Integer.valueOf(30);
	public Integer FINAL_COUNTDOWN_SECONDS = Integer.valueOf(20);
	public Integer MAX_GAME_RUNNING_TIME = Integer.valueOf(30);
	public Integer MINIMUM_PLAYERS = Integer.valueOf(1);
	public final Integer WINNER_PLAYERS = Integer.valueOf(1);
	public Integer END_GAME_TIME = Integer.valueOf(25);
	public final String WORLD_TEMPOARY_NAME = "world";
	public Boolean REGEN_WORLD = false;
	public Boolean RANDOM_START = false;
	public Boolean SHOW_TIPS = true;
	public Boolean DENY_CHECK_WORLDBORDER = Boolean.valueOf(false);
	public Boolean DENY_LOGIN = false;
	public Boolean DENY_BLOCKPLACE = false;
	public Boolean DENY_BLOCKBREAK = false;
	public Boolean DENY_ITEMPICKUP = false;
	public Boolean DENY_ITEMDROP = false;
	public Boolean DENY_DAMAGE_PLAYER = false;
	public Boolean DENY_DAMAGE_ENTITY = false;
	public Boolean DENY_SHOOT_BOW = false;
	public Boolean QUIT_MSG = false;
	public Boolean DEATH_MSG = false;
	public Boolean COMPASS = true;
	public Boolean AUTO_COMPASS = false;
	public Boolean ADV_ABI = false;
	public Boolean SIMP_REW = false;
	public Boolean REW = false;
	public Boolean DEATH_SIGNS = true;
	public Boolean DEATH_SG_PROTECTED = true;
	public Boolean END_GAME = true;
	public Boolean DEFAULT_KIT = false;
	public Boolean CORNUCOPIA = true;
	public Boolean CORNUCOPIA_CHESTS = false;
	public Boolean TEAM = true;
	public Boolean GEN_MAPS = false;
	public Boolean ITEM_MENU = true;
	public Boolean CORNUCOPIA_ITEMS = true;
	public Boolean CORNUCOPIA_PROTECTED = true;
	public Boolean FEAST = true;
	public Boolean FEAST_CHESTS = false;
	public Boolean FEAST_PROTECTED = true;
	public Boolean SPECTATOR_SYSTEM = false;
	Boolean SQL_DSC = false;
	public Location spawn;
	public String STOP_CMD = "";
	public String LAST_WINNER = "";
	public ArrayList<Player> spectators = new ArrayList<Player>();
	public ArrayList<Player> gamemakers = new ArrayList<Player>();
	public static Integer COUNTDOWN = Integer.valueOf(0);
	public Integer FINAL_COUNTDOWN = Integer.valueOf(0);
	public Integer GAME_RUNNING_TIME = Integer.valueOf(0);
	HashMap<World, ArrayList<Border>> BORDERS = new HashMap<World, ArrayList<Border>>();
	public static Integer WORLDRADIUS = Integer.valueOf(250);
	public Boolean SQL_USE = false;
	public Integer FEAST_SPAWN_TIME = 30;
	public Integer COINS_FOR_KILL = 1;
	public Integer COINS_FOR_WIN = 5;

	public Integer SQL_GAMEID = null;
	public String SQL_HOST = null;
	public String SQL_PORT = null;
	public String SQL_USER = null;
	public String SQL_PASS = null;
	public String SQL_DATA = null;
	public Connection con = null;

	final Timer timer1 = new Timer();
	TimerTask task1 = new TimerTask() {
		public void run() {

			if (BGMain.COUNTDOWN.intValue() > 0) {
				if (BGMain.COUNTDOWN >= 10 & BGMain.COUNTDOWN % 10 == 0) {
					BGChat.printTimeChat("The game will start in "
							+ BGMain.this.TIME(BGMain.COUNTDOWN) + ".");
					for (Player pl : getGamers()) {
						pl.setHealth(20);
						pl.setFoodLevel(20);
						pl.setExp(0);
						pl.setRemainingAir(20);
					}
				} else if (BGMain.COUNTDOWN < 10) {
					BGChat.printTimeChat("The game will start in "
							+ BGMain.this.TIME(BGMain.COUNTDOWN) + ".");
					for (Player pl : getGamers()) {
						pl.playSound(pl.getLocation(), Sound.CLICK, 1.0F, (byte) 1);
					}
				}

				BGMain.COUNTDOWN--;
			} else if (BGMain.this.getGamers().length < BGMain.this.MINIMUM_PLAYERS
					.intValue()) {
				BGChat.printTimeChat("There are too few players on, restarting countdown.");
				BGMain.COUNTDOWN = BGMain.COUNTDOWN_SECONDS;
			} else {
				BGMain.this.startgame();
			}
		}
	};

	
	final Timer timer3 = new Timer();
	TimerTask task3 = new TimerTask() {
		public void run() {
			if (BGMain.this.FINAL_COUNTDOWN > 0) {
				if (BGMain.this.FINAL_COUNTDOWN >= 10
						& BGMain.this.FINAL_COUNTDOWN % 10 == 0) {
					BGChat.printTimeChat("Invincibility wears off in "
							+ BGMain.this.TIME(BGMain.this.FINAL_COUNTDOWN)
							+ ".");
				} else if (BGMain.this.FINAL_COUNTDOWN < 10) {
					BGChat.printTimeChat("Invincibility wears off in "
							+ BGMain.this.TIME(BGMain.this.FINAL_COUNTDOWN)
							+ ".");
					for (Player pl : getGamers()) {
						pl.playSound(pl.getLocation(), Sound.CLICK, 1.0F, (byte) 1);
					}
				}
				FINAL_COUNTDOWN--;
			} else {
				BGChat.printTimeChat("");
				BGChat.printTimeChat("Invincibility was worn off.");
				log.info("Game phase: 3 - Fighting");
				for (Player pl : getGamers()) {
					pl.playSound(pl.getLocation(), Sound.ANVIL_LAND, 1.0F, (byte) 1);
				}
				if(SHOW_TIPS) {
					BGChat.printTipChat();
				}
				if(ADV_CHAT_SYSTEM && !SHOW_TIPS) {
					BGChat.updateChat();
				}
				DENY_DAMAGE_PLAYER = false;
				DEATH_MSG = true;
				timer3.cancel();
				timer2.scheduleAtFixedRate(task2, 0, 60000);
			}
		}
	};

	final Timer timer2 = new Timer();
	TimerTask task2 = new TimerTask() {
		public void run() {
			GAME_RUNNING_TIME++;

			checkwinner();
			BGVanish.updateVanished();
			
			if ((GAME_RUNNING_TIME % 5 != 0)
					& (BGMain.this.GAME_RUNNING_TIME % 10 != 0)) {
				if(SHOW_TIPS) {	
					BGChat.printTipChat();
				}
				if(ADV_CHAT_SYSTEM && !SHOW_TIPS) {
					BGChat.updateChat();
				}
			}
			
			if(FEAST) {
				if (GAME_RUNNING_TIME == FEAST_SPAWN_TIME - 3)
					BGFeast.announceFeast(3);
				if (GAME_RUNNING_TIME == FEAST_SPAWN_TIME - 2)
					BGFeast.announceFeast(2);
				if (GAME_RUNNING_TIME == FEAST_SPAWN_TIME - 1)
					BGFeast.announceFeast(1);
				
				if (GAME_RUNNING_TIME == FEAST_SPAWN_TIME)
					BGFeast.spawnFeast();
			}
			
			if (GAME_RUNNING_TIME == (BGMain.this.END_GAME_TIME - 1)) {
				if(END_GAME) {
					BGChat.printInfoChat("Final battle ahead. Teleporting everybody to spawn in 1 minute!");
					
					END_GAME = false;
					
					BGFBattle.createBattle();
					
					timer4.schedule(task4, 60000);
				}			
            }


			if (GAME_RUNNING_TIME.intValue() == MAX_GAME_RUNNING_TIME
					.intValue() - 1) {
				BGChat.printInfoChat("Final battle! 1 minute left.");
			}

			if (GAME_RUNNING_TIME.intValue() >= MAX_GAME_RUNNING_TIME
					.intValue())
				Bukkit.getServer().shutdown();
		}
	};

	public final Timer timer4 = new Timer();
	public TimerTask task4 = new TimerTask(){
		
		public void run() {
			
			World w = Bukkit.getWorld("world");
			w.setDifficulty(Difficulty.HARD);
			w.strikeLightning(BGMain.this.spawn.add(0.0D, 100.0D, 0.0D));
			BGChat.printInfoChat("Final battle! Teleported everybody to spawn.");
			log.info("Game phase: 4 - Final");
			BGFBattle.teleportGamers(getGamers());
		}
	};
	
	public void onLoad() {
		plugin = this;
		log = getPluginLogger();
		
		try {
			files = new BGFiles(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.log.info("Deleting old world.");
		Bukkit.getServer().unloadWorld("world", false);
		deleteDir(new File("world"));

		Random r = new Random();
		
		this.REGEN_WORLD = getConfig().getBoolean("REGEN_WORLD");
		this.GEN_MAPS = BGFiles.worldconf.getBoolean("GEN_MAPS");
		
		if (!this.REGEN_WORLD && !(GEN_MAPS && r.nextBoolean())) {
			List<String> mapnames = BGFiles.worldconf.getStringList("WORLDS");
			
			String map = mapnames.get(r.nextInt(mapnames.size()));
			String[] splitmap = map.split(",");
			
			this.log.info("Copying saved world. ("+splitmap[0]+")");
			try {
				copyDirectory(new File(this.getDataFolder(), splitmap[0]),
						new File("world"));
			} catch (IOException e) {
				log.warning("Error: " + e.toString());
			}
			BGMain.WORLDRADIUS = Integer.valueOf(Integer.parseInt(splitmap[1]));
		} else {
			this.log.info("Generating new world.");
			BGMain.WORLDRADIUS = Integer.valueOf(getConfig().getInt("WORLD_BORDER_RADIUS"));
		}
	}

	public void onEnable() {
		
		plugin.getServer().getWorld("world").setDifficulty(Difficulty.PEACEFUL);
		
		this.ADV_ABI = Boolean.valueOf(getConfig().getBoolean("ADVANCED_ABILITIES"));
		
		kit = new BGKit(this);
		listener = new BGListener(this);
		cooldown = new BGCooldown(this);
		chat = new BGChat(this);
		consolecmd = new BGConsole(this);
		player = new BGPlayer(this);
		vanish = new BGVanish(this);
		sign = new BGSign(this);
		feasts = new BGFeast(this);
		cornucopia = new BGCornucopia(this);
		reward = new BGReward(this);
		
		//Metrics
		try{
			Metrics metrics = new Metrics(this);
			metrics.start();
			log.info("Starting Plugin Metrics...");
		}catch (IOException e) {
			log.info("Error with Plugin Metrics!");
		}
		
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		

		if (this.getCommand("help") != null) 
			this.getCommand("help").setExecutor(player); 
		else 
			console.sendMessage(ChatColor.RED+"getCommand help returns null");

		if (this.getCommand("kit") != null) 
			this.getCommand("kit").setExecutor(player); 
		else 
			console.sendMessage(ChatColor.RED+"getCommand kit returns null");

		if (this.getCommand("kitinfo") != null) 
			this.getCommand("kitinfo").setExecutor(player); 
		else 
			console.sendMessage(ChatColor.RED+"getCommand kitinfo returns null");

		if (this.getCommand("start") != null) 
			this.getCommand("start").setExecutor(consolecmd); 
		else 
			console.sendMessage(ChatColor.RED+"getCommand start returns null");

		if (this.getCommand("spawn") != null) 
			this.getCommand("spawn").setExecutor(player); 
		else 
			console.sendMessage(ChatColor.RED+"getCommand spawn returns null");
		
		if (this.getCommand("coin") != null)
			this.getCommand("coin").setExecutor(consolecmd);
		else
			console.sendMessage(ChatColor.RED+"getCommand coin returns null");
		
		if (this.getCommand("fbattle") != null)
			this.getCommand("fbattle").setExecutor(consolecmd);
		else
			console.sendMessage(ChatColor.RED+"getCommand fbattle returns null");
		
		if (this.getCommand("bgversion") != null)
			this.getCommand("bgversion").setExecutor(consolecmd);
		else
			console.sendMessage(ChatColor.RED+"getCommand bgversion returns null");
		if(this.getCommand("bgdownload") != null)
			this.getCommand("bgdownload").setExecutor(consolecmd);
		else
			console.sendMessage(ChatColor.RED+"getCommand bgdownload returns null");
		if(this.getCommand("team") != null)
			this.getCommand("team").setExecutor(player);
		else
			console.sendMessage(ChatColor.RED+"getCommand team returns null");
		if(this.getCommand("gamemaker") != null)
			this.getCommand("gamemaker").setExecutor(player);
		else
			console.sendMessage(ChatColor.RED+"getCommand gamemaker returns null");
		if(this.getCommand("teleport") != null)
			this.getCommand("teleport").setExecutor(player);
		else
			console.sendMessage(ChatColor.RED+"getCommand teleport returns null");

		log.info("Loading configuration options.");
		this.DEATH_SIGNS = Boolean.valueOf(getConfig().getBoolean("DEATH_SIGNS"));
		this.DEATH_SG_PROTECTED = Boolean.valueOf(BGFiles.dsign.getBoolean("PROTECTED"));
		this.KIT_BUY_WEB = getConfig().getString("MESSAGE.KIT_BUY_WEBSITE");
		this.SERVER_TITLE = getConfig().getString("MESSAGE.SERVER_TITLE");
		this.HELP_MESSAGE = getConfig().getString("MESSAGE.HELP_MESSAGE");
		this.RANDOM_START = Boolean.valueOf(getConfig().getBoolean("RANDOM_START"));
		this.DEFAULT_KIT = Boolean.valueOf(getConfig().getBoolean("DEFAULT_KIT"));
		this.SHOW_TIPS = Boolean.valueOf(getConfig().getBoolean("SHOW_TIPS"));
		this.REGEN_WORLD = Boolean.valueOf(getConfig().getBoolean("REGEN_WORLD"));
		this.CORNUCOPIA = Boolean.valueOf(getConfig().getBoolean("CORNUCOPIA"));
		this.CORNUCOPIA_ITEMS = Boolean.valueOf(BGFiles.cornconf.getBoolean("ITEM_SPAWN"));
		this.CORNUCOPIA_CHESTS = Boolean.valueOf(BGFiles.cornconf.getBoolean("CHESTS"));
		this.CORNUCOPIA_PROTECTED = Boolean.valueOf(BGFiles.cornconf.getBoolean("PROTECTED"));
		this.FEAST = Boolean.valueOf(getConfig().getBoolean("FEAST"));
		this.FEAST_CHESTS = Boolean.valueOf(BGFiles.feastconf.getBoolean("CHESTS"));
		this.FEAST_PROTECTED = Boolean.valueOf(BGFiles.feastconf.getBoolean("PROTECTED"));
		this.SPECTATOR_SYSTEM = Boolean.valueOf(getConfig().getBoolean("SPECTATOR_SYSTEM"));
		this.TEAM = Boolean.valueOf(getConfig().getBoolean("TEAM"));
		this.NO_KIT_MSG = getConfig().getString("MESSAGE.NO_KIT_PERMISSION");
		this.GAME_IN_PROGRESS_MSG = getConfig().getString("MESSAGE.GAME_PROGRESS");
		this.SERVER_FULL_MSG = getConfig().getString("MESSAGE.SERVER_FULL");
		this.WORLD_BORDER_MSG = getConfig().getString("MESSAGE.WORLD_BORDER");
		this.MOTD_PROGRESS_MSG = getConfig().getString("MESSAGE.MOTD_PROGRESS");
		this.MOTD_COUNTDOWN_MSG = getConfig().getString("MESSAGE.MOTD_COUNTDOWN");
		this.ADV_CHAT_SYSTEM = Boolean.valueOf(getConfig().getBoolean("ADVANCED_CHAT"));
		this.SQL_USE = Boolean.valueOf(getConfig().getBoolean("MYSQL"));
		this.SQL_HOST = getConfig().getString("HOST");
		this.SQL_PORT = getConfig().getString("PORT");
		this.SQL_USER = getConfig().getString("USERNAME");
		this.SQL_PASS = getConfig().getString("PASSWORD");
		this.SQL_DATA = getConfig().getString("DATABASE");
		this.SIMP_REW = getConfig().getBoolean("SIMPLE_REWARD");
		this.REW = Boolean.valueOf(getConfig().getBoolean("REWARD"));
		this.COINS_FOR_KILL = Integer.valueOf(getConfig().getInt("COINS_FOR_KILL"));
		this.COINS_FOR_WIN = Integer.valueOf(getConfig().getInt("COINS_FOR_WIN"));
		this.MINIMUM_PLAYERS = Integer.valueOf(getConfig().getInt("MINIMUM_PLAYERS_START"));	
		this.MAX_GAME_RUNNING_TIME = Integer.valueOf(getConfig().getInt("TIME.MAX_GAME-MIN"));
		COUNTDOWN_SECONDS = Integer.valueOf(getConfig().getInt("TIME.COUNTDOWN-SEC"));
		this.FINAL_COUNTDOWN_SECONDS = Integer.valueOf(getConfig().getInt("TIME.FINAL_COUNTDOWN-SEC"));
		this.END_GAME_TIME = Integer.valueOf(getConfig().getInt("TIME.INCREASE_DIFFICULTY-MIN"));
		this.COMPASS = Boolean.valueOf(getConfig().getBoolean("COMPASS"));
		this.AUTO_COMPASS = Boolean.valueOf(getConfig().getBoolean("AUTO_COMPASS"));
		this.STOP_CMD = getConfig().getString("RESTART_SERVER_COMMAND");
		this.ITEM_MENU = getConfig().getBoolean("ITEM_MENU");
		
		if (ADV_ABI) {
			log.info("Enabeling the advanced abilities.");
			dis = new BGDisguise(this);
		}
		
		if(REW && !SQL_USE) {
			log.warning("MySQL has to be enabled for advanced reward, turning it off.");
			this.REW = false;
		}
		
		if(FEAST) {
			FEAST_SPAWN_TIME = Integer.valueOf(BGFiles.feastconf.getInt("SPAWN_TIME"));
		}
		
		if(CORNUCOPIA) {
			BGCornucopia.createCorn();
		}
		
		if(BGMain.WORLDRADIUS.intValue() < 50) {
			log.warning("Worldborder radius has to be 50 or higher!");
			getServer().getPluginManager().disablePlugin(this);
		}

		log.info("Getting winner of last game.");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(getDataFolder(),"leaderboard.yml")));
		} catch (FileNotFoundException e) {
			this.log.warning(e.toString());
		}

		String line = null;
		String merke = null;
		try {
			while ((line = br.readLine()) != null)
				merke = line;
		} catch (IOException e) {
			this.log.warning(e.toString());
		}
		try {
			br.close();
		} catch (IOException e) {
			log.warning(e.toString());
		}

		this.LAST_WINNER = merke;

		World thisWorld = getServer().getWorld("world");
		this.spawn = thisWorld.getSpawnLocation();

		Border newBorder = new Border(this.spawn.getX(), this.spawn.getZ(), BGMain.WORLDRADIUS.intValue());
		if (!this.BORDERS.containsKey(thisWorld)) {
			ArrayList<Border> newArray = new ArrayList<Border>();
			this.BORDERS.put(thisWorld, newArray);
		}
		((ArrayList<Border>) this.BORDERS.get(thisWorld)).add(newBorder);

		COUNTDOWN = COUNTDOWN_SECONDS;
		this.FINAL_COUNTDOWN = this.FINAL_COUNTDOWN_SECONDS;
		this.GAME_RUNNING_TIME = Integer.valueOf(0);

		this.DENY_BLOCKBREAK = Boolean.valueOf(true);
		this.DENY_BLOCKPLACE = Boolean.valueOf(true);
		this.DENY_ITEMDROP = Boolean.valueOf(true);
		this.DENY_ITEMPICKUP = Boolean.valueOf(true);
		this.DENY_DAMAGE_PLAYER = Boolean.valueOf(true);
		this.DENY_DAMAGE_ENTITY = Boolean.valueOf(true);
		this.DENY_SHOOT_BOW = Boolean.valueOf(true);

		if (SQL_USE) {
			SQLconnect();
			SQLquery("CREATE TABLE IF NOT EXISTS `GAMES` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `STARTTIME` datetime NOT NULL, `ENDTIME` datetime, `REF_WINNER` int(10), PRIMARY KEY (`ID`)) ENGINE=MyISAM DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLquery("CREATE TABLE IF NOT EXISTS `PLAYERS` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `NAME` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=MyISAM DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLquery("CREATE TABLE IF NOT EXISTS `PLAYS` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `REF_PLAYER` int(10), `REF_GAME` int(10), `KIT` varchar(255), `DEATHTIME` datetime, `REF_KILLER` int(10), `DEATH_REASON` varchar(255), PRIMARY KEY (`ID`)) ENGINE=MyISAM DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
			SQLquery("CREATE TABLE IF NOT EXISTS `REWARD` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `REF_PLAYER` int(10) NOT NULL, `COINS` int(10) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=MyISAM DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;");
		}

		Location loc = randomLocation(this.spawn.getChunk()).add(0.0D, 30.0D,0.0D);
		Bukkit.getServer().getWorld("world").loadChunk(loc.getChunk());
		this.timer1.scheduleAtFixedRate(this.task1, 0L, 1000L);

		PluginDescriptionFile pdfFile = getDescription();
		this.log.info("Plugin enabled");
		this.log.info("Author: " + pdfFile.getAuthors() + " | Version: " + pdfFile.getVersion());
		this.log.info("All rights reserved. This plugin is free to download. If you had to pay for it, contact us immediately!");
		
		log.info("Game phase: 1 - Waiting");
	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		
		Bukkit.getServer().getScheduler().cancelAllTasks();
		
		if (SQL_USE) {
			if (SQL_GAMEID != null) {
				Integer PL_ID = getPlayerID(NEW_WINNER);
				SQLquery("UPDATE `GAMES` SET `ENDTIME` = NOW(), `REF_WINNER` = "
						+ PL_ID + " WHERE `ID` = " + SQL_GAMEID + " ;");
				SQL_DSC = true;
				SQLdisconnect();
			}
		}

		this.timer2.cancel();
		this.timer1.cancel();
		this.timer3.cancel();

		for (Player p : getPlayers()) {
			p.kickPlayer(ChatColor.YELLOW + "Server is restarting!");
		}
		
		Bukkit.getServer().unloadWorld("world", false);
		
		this.log.info("Plugin disabled");
		this.log.info("Author: " + pdfFile.getAuthors() + " | Version: " + pdfFile.getVersion());
		
		Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), this.STOP_CMD);
	}

	private void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	public void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean inBorder(Location checkHere) {
		if (!this.BORDERS.containsKey(checkHere.getWorld()))
			return true;
		for (Border amIHere : this.BORDERS.get(checkHere.getWorld())) {
			int X = (int) Math.abs(amIHere.centerX - checkHere.getBlockX());
			int Z = (int) Math.abs(amIHere.centerZ - checkHere.getBlockZ());
			if ((X < amIHere.definiteSq) && (Z < amIHere.definiteSq))
				return true;
			if ((X > amIHere.radius) || (Z > amIHere.radius))
				continue;
			if (X * X + Z * Z < amIHere.radiusSq)
				return true;
		}
		return false;
	}
	
	public Location getSpawn() {
		Location loc = Bukkit.getWorld("world").getSpawnLocation();
		loc.setY(Bukkit.getWorld("world").getHighestBlockYAt(Bukkit.getWorld("world").getSpawnLocation()) + 1.5);
		return loc;
	}

	public Player[] getGamers() {
		ArrayList<Player> gamers = new ArrayList<Player>();
		Player[] list = Bukkit.getOnlinePlayers();
		for (Player p : list) {
			if (!plugin.isSpectator(p) && !plugin.isGameMaker(p)) {
				gamers.add(p);
			}
		}
		return (Player[]) gamers.toArray(new Player[0]);
	}

	public Player[] getPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		Player[] onlineplayers = Bukkit.getOnlinePlayers();
		for (int i = 0; i < onlineplayers.length; i++) {
			players.add(onlineplayers[i]);
		}
		return (Player[]) players.toArray(new Player[0]);
	}

	public void startgame() {
		log.info("Game phase: 2 - Starting");
		this.timer1.cancel();
		this.timer3.scheduleAtFixedRate(this.task3, 3000, 1000);

		this.DENY_LOGIN = true;
		this.DENY_BLOCKBREAK = false;
		this.DENY_BLOCKPLACE = false;
		this.DENY_ITEMDROP = false;
		this.DENY_ITEMPICKUP = false;
		this.DENY_DAMAGE_ENTITY = false;
		this.DENY_SHOOT_BOW = false;
		this.QUIT_MSG = true;

		if(CORNUCOPIA_ITEMS && CORNUCOPIA)
			BGCornucopia.spawnItems();
		
		if (SQL_USE) {
			PreparedStatement statement = null;
			ResultSet generatedKeys = null;

			try {
				statement = con.prepareStatement(
						"INSERT INTO `GAMES` (`STARTTIME`) VALUES (NOW()) ;",
						Statement.RETURN_GENERATED_KEYS);

				int affectedRows = statement.executeUpdate();
				if (affectedRows == 0) {
					throw new SQLException("Error!");
				}

				generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next()) {
					SQL_GAMEID = (int) generatedKeys.getLong(1);
				} else {
					throw new SQLException("Error!");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (generatedKeys != null)
					try {
						generatedKeys.close();
					} catch (SQLException logOrIgnore) {
					}
				if (statement != null)
					try {
						statement.close();
					} catch (SQLException logOrIgnore) {
					}
			}

		}

		this.DENY_CHECK_WORLDBORDER = Boolean.valueOf(true);
		Bukkit.getServer().getWorld("world").loadChunk(getSpawn().getChunk());

		Bukkit.getWorld("world").setDifficulty(Difficulty.HARD);
		for (Player p : getPlayers()) {
			if(this.isGameMaker(p) || this.isSpectator(p))
				continue;
			if (this.RANDOM_START == false) {
				Random r = new Random();
				Location startFrom = getSpawn();
				Location loc = startFrom.clone();
				int addx;
				int addy;
				do {
					
					addx = (r.nextBoolean() ? 1 : -1) * r.nextInt(7);
					addy = (r.nextBoolean() ? 1 : -1) * r.nextInt(7);
				}while((Math.abs(addx)+Math.abs(addy)) < 5);
				loc.add(addx, 60, addy);
				loc.setY(getServer().getWorld("world").getHighestBlockYAt(loc) + 1.5);
				p.teleport(loc);
			} else {
				Location tploc = getRandomLocation();
				while(!inBorder(tploc)) {
					tploc = getRandomLocation();
				}
				tploc.setY(getServer().getWorld("world").getHighestBlockYAt(tploc) + 1.5);
				p.teleport(tploc);
			}
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setExp(0);
			BGKit.giveKit(p);

			if (SQL_USE & !plugin.isSpectator(p)) {
				Integer PL_ID = getPlayerID(p.getName());
				SQLquery("INSERT INTO `PLAYS` (`REF_PLAYER`, `REF_GAME`, `KIT`) VALUES ("
						+ PL_ID
						+ ","
						+ SQL_GAMEID
						+ ",'"
						+ BGKit.getKit(p)
						+ "') ;");
			}
		}

		Bukkit.getServer().getWorld("world").setTime(0L);
		Bukkit.getServer().getWorld("world").setStorm(false);
		Bukkit.getServer().getWorld("world").setThundering(false);
		this.DENY_CHECK_WORLDBORDER = Boolean.valueOf(false);
		if (ADV_CHAT_SYSTEM) {
			BGChat.printInfoChat(" --- The games have begun! ---");
			BGChat.printDeathChat("§e\"May the odds be ever in your favor!\"");
		} else {
			BGChat.printTimeChat("");
			BGChat.printTimeChat("The games have begun!");
		}
		BGChat.printTimeChat("Everyone is invincible for "
				+ TIME(this.FINAL_COUNTDOWN_SECONDS) + ".");
	}

	public static Location randomLocation(Chunk c) {
		Random random = new Random();
		Location startFrom = Bukkit.getWorld("world").getSpawnLocation();
		Location loc = startFrom.clone();
		loc.add((random.nextBoolean() ? 1 : -1) * random.nextInt(WORLDRADIUS),
				60,
				(random.nextBoolean() ? 1 : -1) * random.nextInt(WORLDRADIUS));
		int newY = Bukkit.getWorld("world").getHighestBlockYAt(loc);
		loc.setY(newY);
		return loc;
	}

	public static Location getRandomLocation() {
		Random random = new Random();
		Location startFrom = Bukkit.getWorld("world").getSpawnLocation();
		Location loc;
		do{
			loc = startFrom.clone();
			loc.add((random.nextBoolean() ? 1 : -1) * random.nextInt(WORLDRADIUS),
				60,
				(random.nextBoolean() ? 1 : -1) * random.nextInt(WORLDRADIUS));
			int newY = Bukkit.getWorld("world").getHighestBlockYAt(loc);
			loc.setY(newY);
		}while(!plugin.inBorder(loc));
		return loc;
	}

	public void checkwinner() {
		if (getGamers().length <= this.WINNER_PLAYERS)
			if (getGamers().length == 0) {
				this.timer2.cancel();
				Bukkit.getServer().shutdown();
			} else {
				this.timer2.cancel();
				String winnername = getGamers()[0].getName();
				this.NEW_WINNER = winnername;
				try {
					String contents = winnername;
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							new File(getDataFolder(), "leaderboard.yml"), true));
					writer.newLine();
					writer.write(contents);
					writer.flush();
					writer.close();
				} catch (Exception ex) {
					this.log.warning(ex.toString());
				}
				
				Player pl = getGamers()[0];
				pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0F, (byte) 1);
				
				if(this.REW && this.COINS_FOR_WIN != 0) {
					String text = "You got ";
					if(this.COINS_FOR_WIN == 1)
						text += "1 Coin for winning the game!";
					else
						text += this.COINS_FOR_WIN+" Coins for winning the game!";
					getGamers()[0].kickPlayer(ChatColor.GOLD
							+ "You are the winner of this game!"+
							'\n'+ text);
				}else {
					getGamers()[0].kickPlayer(ChatColor.GOLD
							+ "You are the winner of this game!");
				}
				
				if(SQL_USE) {
					Integer PL_ID = getPlayerID(winnername);
					SQLquery("UPDATE `PLAYS` SET deathtime = NOW(), `DEATH_REASON` = 'WINNER' WHERE `REF_PLAYER` = "
							+ PL_ID
							+ " AND `REF_GAME` = "
							+ SQL_GAMEID + " ;");
				}
				
				if(REW) {
					if (getPlayerID(winnername) == null) {
						reward.createUser(winnername);
						reward.giveCoins(winnername, plugin.COINS_FOR_WIN);
					} else {
						reward.giveCoins(winnername, plugin.COINS_FOR_WIN);
					}
				}
				
				Bukkit.getServer().shutdown();
			}
	}

	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(dir, children[i]));
			}
		}
		dir.delete();
	}

	public String TIME(Integer i) {
		if (i.intValue() >= 60) {
			Integer time = Integer.valueOf(i.intValue() / 60);
			String add = "";
			if (time > 1) {
				add = "s";
			}
			return time + " minute" + add;
		}
		Integer time = i;
		String add = "";
		if (time > 1) {
			add = "s";
		}
		return time + " second" + add;
	}

	public boolean winner(Player p) {
		if (LAST_WINNER == null) {
			return false;
		}
		if (LAST_WINNER.equals(p.getName())) {
			return true;
		} else {
			return false;
		}
	}

	public Integer getPlayerID(String playername) {
		try {
			Statement stmt = con.createStatement();
			ResultSet r = stmt
					.executeQuery("SELECT `ID`, `NAME` FROM `PLAYERS` WHERE `NAME` = '"
							+ playername + "' ;");
			r.last();
			if (r.getRow() == 0) {
				stmt.close();
				r.close();
				return null;
			}
			Integer PL_ID = r.getInt("ID");
			stmt.close();
			r.close();
			return PL_ID;
		} catch (SQLException ex) {
			System.err.println("Error with following query: "
					+ "SELECT `ID`, `NAME` FROM `PLAYERS` WHERE `NAME` = '"
					+ playername + "' ;");
			System.err.println("MySQL-Error: " + ex.getMessage());
			return null;
		} catch (NullPointerException ex) {
			System.err
					.println("Error while performing a query. (NullPointerException)");
			return null;
		}
	}

	public void SQLconnect() {
		try {
			log.info("Connecting to MySQL database...");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String conn = "jdbc:mysql://" + SQL_HOST + ":" + SQL_PORT + "/"
					+ SQL_DATA;
			con = DriverManager.getConnection(conn, SQL_USER, SQL_PASS);
		} catch (ClassNotFoundException ex) {
			log.warning("No MySQL driver found!");
		} catch (SQLException ex) {
			log.warning("Error while fetching MySQL connection!");
		} catch (Exception ex) {
			log.warning("Unknown error while fetchting MySQL connection.");
		}
	}

	public Connection SQLgetConnection() {
		return con;
	}

	public void SQLquery(String sql) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException ex) {
			log.warning("Error with following query: "
					+ sql);
			log.warning("MySQL-Error: " + ex.getMessage());
			SQLdisconnect();
		} catch (NullPointerException ex) {
			log.warning("Error while performing a query. (NullPointerException)");
		}
	}

	public void SQLdisconnect() {
		try {
			log.warning("Disconnecting from MySQL database...");
			con.close();
		} catch (SQLException ex) {
			log.warning("Error while closing the connection...");
		} catch (NullPointerException ex) {
			log.warning("Error while closing the connection...");
		}

		if(!SQL_DSC){
			log.info("Reconnecting with MySQL database...");
			SQLconnect();
		}
	}
	
	public Integer getCoins(Integer playerID) {
		try {
			Statement stmt = con.createStatement();
			ResultSet r = stmt
					.executeQuery("SELECT `COINS`, `REF_PLAYER` FROM `REWARD` WHERE `REF_PLAYER` = "
							+ playerID + " ;");
			r.last();
			if (r.getRow() == 0) {
				stmt.close();
				r.close();
				return null;
			}
			Integer PL_ID = r.getInt("COINS");
			stmt.close();
			r.close();
			return PL_ID;
		} catch (SQLException ex) {
			log.warning("Error with following query: "
					+ "SELECT `COINS`, `REF_PLAYER` FROM `REWARD` WHERE `REF_PLAYER` = "
					+ playerID + " ;");
			System.err.println("MySQL-Error: " + ex.getMessage());
			return null;
		} catch (NullPointerException ex) {
			log.warning("Error while performing a query. (NullPointerException)");
			return null;
		}
	}
	
	public void addGameMaker(Player p) {
		spectators.remove(p);
		gamemakers.add(p);
		
		p.setGameMode(GameMode.CREATIVE);
		BGVanish.makeVanished(p);
		BGChat.printPlayerChat(p, "§eYou are now a gamemaker!");
	}
	
	public void remGameMaker(Player p) {
		gamemakers.remove(p);
		p.setGameMode(GameMode.SURVIVAL);
		BGVanish.makeVisible(p);
	}
	
	public Boolean isGameMaker(Player p) {
		return gamemakers.contains(p);
	}
	
	public Boolean isSpectator(Player p) {
		if(isGameMaker(p))
			return false;
		
		return spectators.contains(p);
	}
	
	public ArrayList<Player> getSpectators() {
		return spectators;
	}
	
	public ArrayList<Player> getGamemakers() {
		return gamemakers;
	}
	
	public void addSpectator(Player p) {
		if(isGameMaker(p))
			return;
			
		spectators.add(p);
		p.setGameMode(GameMode.CREATIVE);
		BGVanish.makeVanished(p);
		BGChat.printPlayerChat(p, "§eYou are now a spectator!");
	}
	
	public void remSpectator(Player p) {
		spectators.remove(p);
		p.setGameMode(GameMode.SURVIVAL);
		BGVanish.makeVisible(p);
	}
	
	public static Logger getPluginLogger() {
		return plugin.getLogger();
	}
	
	public File getPFile() {
		return getFile();
	}
}

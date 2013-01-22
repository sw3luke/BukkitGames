package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import main.BGMain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import utilities.enums.GameState;

public class BGChat {
	static Integer TIP_COUNT = 0;
	static ArrayList<String> TIPS = new ArrayList<String>();
	private static Logger log = BGMain.getPluginLogger();
	static String TIMER_MSG = ""; 
	static String DEATH_MSG = ""; 
	static String INFO_MSG = "Welcome to the HungerGames! | Get your kit now: §f/kit"; 
	static HashMap<Player, String> PLAYER_MSG = new HashMap<Player, String>();
	static HashMap<Player, Boolean> KIT_CHAT = new HashMap<Player, Boolean>();
	static HashMap<Player, Boolean> HELP_CHAT = new HashMap<Player, Boolean>();
	static HashMap<Player, String> KITINFO_CHAT = new HashMap<Player, String>();
	static String[] CHAT_MSG = new String[6];
	static HashMap<Integer, String> ABILITY_DESC = new HashMap<Integer, String>();
	static HashMap<Player, IconMenu> MENUS = new HashMap<Player, IconMenu>();
	
	static Boolean update = true;

	public BGChat() {		
		CHAT_MSG[0] = "";
		CHAT_MSG[1] = "";
		CHAT_MSG[2] = "";
		CHAT_MSG[3] = "";
		CHAT_MSG[4] = "";
		CHAT_MSG[5] = "";

		ABILITY_DESC.put(1, BGFiles.abconf.getString("AB.1.Desc"));
		ABILITY_DESC.put(2, BGFiles.abconf.getString("AB.2.Desc"));
		ABILITY_DESC.put(3, BGFiles.abconf.getString("AB.3.Desc"));
		ABILITY_DESC.put(4, BGFiles.abconf.getString("AB.4.Desc"));
		ABILITY_DESC.put(5, BGFiles.abconf.getString("AB.5.Desc"));
		ABILITY_DESC.put(6, BGFiles.abconf.getString("AB.6.Desc"));
		ABILITY_DESC.put(7, BGFiles.abconf.getString("AB.7.Desc"));
		ABILITY_DESC.put(8, BGFiles.abconf.getString("AB.8.Desc"));
		ABILITY_DESC.put(9, BGFiles.abconf.getString("AB.9.Desc"));
		ABILITY_DESC.put(10, BGFiles.abconf.getString("AB.10.Desc"));
		ABILITY_DESC.put(11, BGFiles.abconf.getString("AB.11.Desc"));
		ABILITY_DESC.put(12, BGFiles.abconf.getString("AB.12.Desc"));
		ABILITY_DESC.put(13, BGFiles.abconf.getString("AB.13.Desc"));
		ABILITY_DESC.put(14, BGFiles.abconf.getString("AB.14.Desc"));
		ABILITY_DESC.put(15, BGFiles.abconf.getString("AB.15.Desc"));
		ABILITY_DESC.put(16, BGFiles.abconf.getString("AB.16.Desc"));
		
		if(BGMain.ADV_ABI) {
			ABILITY_DESC.put(17, BGFiles.abconf.getString("AB.17.Desc"));
		}else {
			ABILITY_DESC.put(17, "Advanced Abilities disabled! This ability wont work!");
		}
		
		
		ABILITY_DESC.put(18, BGFiles.abconf.getString("AB.18.Desc"));
		ABILITY_DESC.put(19, BGFiles.abconf.getString("AB.19.Desc"));
		ABILITY_DESC.put(20, BGFiles.abconf.getString("AB.20.Desc"));
		ABILITY_DESC.put(21, BGFiles.abconf.getString("AB.21.Desc"));
		ABILITY_DESC.put(22, BGFiles.abconf.getString("AB.22.Desc"));
		ABILITY_DESC.put(23, BGFiles.abconf.getString("AB.23.Desc"));
		
		List<String> tiplist = BGFiles.config.getStringList("TIPS");
		for(String tip : tiplist)
			TIPS.add(tip);
		
	}

	public static void playerChatMsg(String message) {
		CHAT_MSG[5] = CHAT_MSG[4];
		CHAT_MSG[4] = CHAT_MSG[3];
		CHAT_MSG[3] = CHAT_MSG[2];
		CHAT_MSG[2] = CHAT_MSG[1];
		CHAT_MSG[1] = CHAT_MSG[0];
		CHAT_MSG[0] = message;
		updateChat();
	}

	public static void printInfoChat(String text) {
		if (BGMain.ADV_CHAT_SYSTEM) {
			INFO_MSG = text;
			updateChat();
		} else {
			Bukkit.getServer().broadcastMessage("§2" + text);
		}
	}

	public static void printDeathChat(String text) {
		if (BGMain.ADV_CHAT_SYSTEM) {
			DEATH_MSG = text;
			updateChat();
		} else {
			Bukkit.getServer().broadcastMessage("§c" + text);
		}
	}

	public static void printTimeChat(String text) {
		if (BGMain.ADV_CHAT_SYSTEM) {
			TIMER_MSG = text;
			updateChat();
		} else {
			Bukkit.getServer().broadcastMessage("§a" + text);
		}
	}

	public static void printPlayerChat(Player player, String text) {
		if (BGMain.ADV_CHAT_SYSTEM) {
			PLAYER_MSG.put(player, "§7" + text);
			updateChat(player);
			final Player pl = player;
			Bukkit.getServer().getScheduler()
					.scheduleSyncDelayedTask(BGMain.instance, new Runnable() {
						public void run() {
							BGChat.PLAYER_MSG.remove(pl);
						}
					}, 100);
		} else {
			player.sendMessage("§a" + text);
		}
	}

	public static void printHelpChat(Player player) {
		if (BGMain.ADV_CHAT_SYSTEM) {
			HELP_CHAT.put(player, true);
			updateChat();
			final Player pl = player;
			Bukkit.getServer().getScheduler()
					.scheduleSyncDelayedTask(BGMain.instance, new Runnable() {
						public void run() {
							BGChat.HELP_CHAT.remove(pl);
						}
					}, 100);
		} else {
			BGChat.printPlayerChat(player, BGMain.SERVER_TITLE);
			String are = "are";
			String players = "players";
			if (BGMain.getGamers().length == 1) {
				are = "is";
				players = "player";
			}

			Integer timeleft = BGMain.MAX_GAME_RUNNING_TIME
					- BGMain.GAME_RUNNING_TIME;
			String is = "are";
			String minute = "minutes";
			if (timeleft <= 1) {
				is = "minute";
				minute = "minute";
			}
			player.sendMessage("§7 - There " + are + " "
					+ BGMain.getGamers().length + " " + players + " online.");
			player.sendMessage("§7 - There " + is + " " + timeleft + " "
					+ minute + " left.");
			if (BGMain.HELP_MESSAGE != null && BGMain.HELP_MESSAGE != "")
				player.sendMessage("§7 - " + BGMain.HELP_MESSAGE);
		}
	}

	public static void printKitChat(Player player) {
		if(!BGMain.ITEM_MENU) {
			
		if (BGMain.ADV_CHAT_SYSTEM) {
			KIT_CHAT.put(player, true);
			updateChat();
			final Player pl = player;
			Bukkit.getServer().getScheduler()
					.scheduleSyncDelayedTask(BGMain.instance, new Runnable() {
						public void run() {
							BGChat.KIT_CHAT.remove(pl);
						}
					}, 100);
		} else {
			Set<String> kitname = BGFiles.kitconf.getKeys(false);
			String yourkits = "";
			String otherkits = "";
			for (String name : kitname) {
				if(name.equalsIgnoreCase("default"))
					continue;
				
				char[] stringArray = name.toCharArray();
				stringArray[0] = Character.toUpperCase(stringArray[0]);
				name = new String(stringArray);
				
				if (player.hasPermission("bg.kit." + name.toLowerCase())
						|| player.hasPermission("bg.kit.*") || (BGMain.SIMP_REW && BGMain.winner(player))
						|| (BGMain.REW && BGReward.BOUGHT_KITS.get(player.getName()) != null &&
							BGReward.BOUGHT_KITS.get(player.getName()).equals(name.toLowerCase()))) {
					yourkits = name + ", " + yourkits;
				} else {
					otherkits = name + ", " + otherkits;
				}
			}
			player.sendMessage("");
			player.sendMessage("§bAvailable kits: §7(/kit [KitName])");
			player.sendMessage("");
			player.sendMessage("§aYour kits: §f" + yourkits);
			player.sendMessage("§aOther kits: §f" + otherkits);
			player.sendMessage("§bMore kits available at: "
					+ BGMain.KIT_BUY_WEB);
			player.sendMessage("");
		}
		
		} else {
			 Set<String> kits = BGFiles.kitconf.getKeys(false);
			 
			 Integer invsize = 9;
			 for(int i=0; i<=10; i++) {
				 if((i*9) >= kits.size()) {
					 invsize = invsize + i*9;
					 break;
				 }
			 }
			 
			 if(MENUS.containsKey(player)) {
				 MENUS.get(player).destroy();
				 MENUS.remove(player);
			 }
			 
			 final Player pl = player;
			 IconMenu menu = new IconMenu("Select a kit", pl.getName(), invsize, new IconMenu.OptionClickEventHandler() {
		            @Override
		            public void onOptionClick(IconMenu.OptionClickEvent event) {
		            	BGKit.setKit(pl, ChatColor.stripColor(event.getName()));
		                event.setWillClose(true);
		                event.setWillDestroy(false);
		            }
		        }, BGMain.instance);
			 
			 Integer mypos = 0;
			 Integer othpos = 1;
			 for(String kitname : kits) {	
				 try {
					 if(kitname.equalsIgnoreCase("default"))
						 continue;
					 
						char[] stringArray = kitname.toCharArray();
						stringArray[0] = Character.toUpperCase(stringArray[0]);
						kitname = new String(stringArray);
					 
						ArrayList<String> container = new ArrayList<String>();
						ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname.toLowerCase());
						List<String> kititems = kit.getStringList("ITEMS");
						for (String item : kititems) {
							String[] oneitem = item.split(",");

							String itemstring = null;
							Integer id = null;
							Integer amount = null;
							String enchantment = null;
							String ench_numb = null;

							if (oneitem[0].contains(":")) {
								String[] ITEM_ID = oneitem[0].split(":");
								id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
								amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
							} else {
								id = Integer.valueOf(Integer.parseInt(oneitem[0]));
								amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
							}

							itemstring = " - "
									+ amount
									+ "x "
									+ Material.getMaterial(id.intValue()).toString()
											.replace("_", " ").toLowerCase();

							if (oneitem.length == 4) {
								enchantment = Enchantment
										.getById(Integer.parseInt(oneitem[2])).getName()
										.toLowerCase();
								ench_numb = oneitem[3];

								itemstring = itemstring + " with " + enchantment + " "
										+ ench_numb;
							}

							container.add("§f" + itemstring);
						}
						
						List<String> pots = kit.getStringList("POTION");
						for(String pot : pots) {	
							if (pot != null & pot != "") {
								if (!pot.equals(0)) {
									String[] potion = pot.split(",");
									if (Integer.parseInt(potion[0]) != 0) {
										PotionEffectType pt = PotionEffectType.getById(Integer.parseInt(potion[0]));
										String name = pt.getName();
										if (Integer.parseInt(potion[1]) == 0) {
											name += " (Duration: infinitely)";
										} else {
											name += " (Duration: "+potion[1]+" sec)";
										}
										container.add("§f * " + name);
									}
								}
							}
						}
						
						List<Integer> abils = kit.getIntegerList("ABILITY");
						for(Integer abil : abils) {
							String desc = getAbilityDesc(abil.intValue());
							if (desc != null)
								container.add("§7 + " + desc);
						}
									
						Integer itemid = kit.getInt("ITEMMENU");
						Material kitem = Material.getMaterial(itemid);
					    
						if (player.hasPermission("bg.kit." + kitname.toLowerCase())
								|| player.hasPermission("bg.kit.*") || (BGMain.SIMP_REW && BGMain.winner(player))
								|| (BGMain.REW && BGReward.BOUGHT_KITS.get(player.getName()) != null &&
									BGReward.BOUGHT_KITS.get(player.getName()).equals(kitname.toLowerCase()))) {
					    
							String[] info = new String[container.size()];
						    info = container.toArray(info);
							
							menu.setOption(mypos, new ItemStack(kitem, 1), "§a" + kitname, info);
							mypos++;
						} else {
							if(BGMain.REW) {
								if(BGKit.getCoins(kitname.toLowerCase()) == 1)
									container.add("§6PRICE: " + BGKit.getCoins(kitname.toLowerCase()) + " Coin");
								else if(BGKit.getCoins(kitname.toLowerCase()) > 1)
									container.add("§6PRICE: " + BGKit.getCoins(kitname.toLowerCase()) + " Coins");
							}
							
							String[] info = new String[container.size()];
						    info = container.toArray(info);
							
							menu.setOption(invsize - othpos, new ItemStack(kitem, 1), "§c" + kitname, info);
							othpos++;
						}
					container.clear();
				} catch (Exception e) {
					log.warning("[BukkitGames] Error while trying to parse kit '" + kitname + "'");
					e.printStackTrace();
				}	
			 }
			 MENUS.put(pl, menu);
			 menu.open(player);
		}
	}

	public static void printKitInfo(Player player, String kitname) {
		String kitinfoname = kitname;
		kitname = kitname.toLowerCase();
		ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname);
		if (kit == null && !BGKit.kits.contains(kitname)) {
			printPlayerChat(player,
					"That kit doesn't exist! View all kits with /kit");
			return;
		}

		if (BGMain.ADV_CHAT_SYSTEM) {
			KITINFO_CHAT.put(player, kitname);
			updateChat();
			final Player pl = player;
			Bukkit.getServer().getScheduler()
					.scheduleSyncDelayedTask(BGMain.instance, new Runnable() {
						public void run() {
							BGChat.KITINFO_CHAT.remove(pl);
						}
					}, 100);
		} else {
			char[] stringArray = kitinfoname.toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitinfoname = new String(stringArray);

			player.sendMessage("§a" + kitinfoname + " kit includes:");

			List<String> kititems = kit.getStringList("ITEMS");
			for (String item : kititems) {
				String[] oneitem = item.split(",");

				String itemstring = null;
				Integer id = null;
				Integer amount = null;
				String enchantment = null;
				String ench_numb = null;

				if (oneitem[0].contains(":")) {
					String[] ITEM_ID = oneitem[0].split(":");
					id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
					amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
				} else {
					id = Integer.valueOf(Integer.parseInt(oneitem[0]));
					amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
				}

				itemstring = " - "
						+ amount
						+ "x "
						+ Material.getMaterial(id.intValue()).toString()
								.replace("_", " ").toLowerCase();

				if (oneitem.length == 4) {
					enchantment = Enchantment
							.getById(Integer.parseInt(oneitem[2])).getName()
							.toLowerCase();
					ench_numb = oneitem[3];

					itemstring = itemstring + " with " + enchantment + " "
							+ ench_numb;
				}

				player.sendMessage("§f" + itemstring);
			}

			List<String> pots = kit.getStringList("POTION");
			for(String pot : pots) {	
				if (pot != null & pot != "") {
					if (!pot.equals(0)) {
						String[] potion = pot.split(",");
						if (Integer.parseInt(potion[0]) != 0) {
							PotionEffectType pt = PotionEffectType.getById(Integer.parseInt(potion[0]));
							String name = pt.getName();
							if (Integer.parseInt(potion[1]) == 0) {
								name += " (Duration: infinitely)";
							} else {
								name += " (Duration: "+potion[1]+" sec)";
							}
							player.sendMessage("§f * " + name);
						}
					}
				}
			}
			
			List<Integer> abils = kit.getIntegerList("ABILITY");
			for(Integer abil : abils) {
				String desc = getAbilityDesc(abil.intValue());

				if (desc != null) {
					player.sendMessage("§f + " + desc);

				}
			}
				
			if(BGKit.getCoins(kitname.toLowerCase()) == 1)
				player.sendMessage("§fPRICE: "+ BGKit.getCoins(kitname.toLowerCase())+ " Coin");
			else if(BGKit.getCoins(kitname.toLowerCase()) > 1)
				player.sendMessage("§fPRICE: "+ BGKit.getCoins(kitname.toLowerCase())+ " Coins");	
		}
	}

	public static void printTipChat() {
		if(TIPS.size() - 1 < TIP_COUNT)
			TIP_COUNT = 0;
		
		String tip = TIPS.get(TIP_COUNT);
		TIP_COUNT++;
		if (BGMain.ADV_CHAT_SYSTEM) {
			INFO_MSG = "[TIP] " + tip;
			updateChat();
		} else {
			if (tip != "" || tip != null)
				Bukkit.getServer().broadcastMessage("§7[TIP] " + tip);
		}
	}

	public static void updateChat() {
		if(!BGMain.ADV_CHAT_SYSTEM)
			return;
		
		for (Player pl : BGMain.getPlayers()) {
			updateChat(pl);
		}
	}

	public static void updateChat(Player p) {
		if(!BGMain.ADV_CHAT_SYSTEM)
			return;

		// Clear chat
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");

		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");

		if (HELP_CHAT.containsKey(p)) {
			Integer line = 5;
			String are = "are";
			String players = "players";
			if (BGMain.getGamers().length == 1) {
				are = "is";
				players = "player";
			}

			Integer timeleft = BGMain.MAX_GAME_RUNNING_TIME
					- BGMain.GAME_RUNNING_TIME;
			String is = "are";
			String minute = "minutes";
			if (timeleft <= 1) {
				is = "minute";
				minute = "minute";
			}
			Integer help_length = 0;
			if (BGMain.HELP_MESSAGE != null && BGMain.HELP_MESSAGE != "")
				help_length = BGMain.HELP_MESSAGE.length();

			while (help_length > 50) {
				line++;
				help_length = help_length - 50;
			}

			p.sendMessage("§b" + BGMain.SERVER_TITLE);
			p.sendMessage("");
			p.sendMessage("§7 - There " + are + " " + BGMain.getGamers().length
					+ " " + players + " online.");
			p.sendMessage("§7 - There " + is + " " + timeleft + " " + minute
					+ " left.");
			if (BGMain.HELP_MESSAGE != null && BGMain.HELP_MESSAGE != "") {
				p.sendMessage("§7 - " + BGMain.HELP_MESSAGE);
				line++;
			}

			if (PLAYER_MSG.containsKey(p))
				line++;

			while (line <= 10) {
				p.sendMessage("");
				line++;
			}

			if (PLAYER_MSG.containsKey(p))
				p.sendMessage(PLAYER_MSG.get(p));

		} else if (KIT_CHAT.containsKey(p)) {
			Integer line = 5;
			Set<String> kitname = BGFiles.kitconf.getKeys(false);

			String yourkits = "";
			String otherkits = "";
			for (String name : kitname) {
				if(name.equalsIgnoreCase("default"))
					continue;
				
				char[] stringArray = name.toCharArray();
				stringArray[0] = Character.toUpperCase(stringArray[0]);
				name = new String(stringArray);
				
				if (p.hasPermission("bg.kit." + name.toLowerCase()) || (BGMain.SIMP_REW && BGMain.winner(p))
					|| (BGMain.REW && BGReward.BOUGHT_KITS.get(p.getName()) != null
					&& BGReward.BOUGHT_KITS.get(p.getName()).equals(name.toLowerCase()))) {
					if(yourkits == "")
						yourkits = name;
					else
						yourkits = name + ", " + yourkits;
				} else {
					if(otherkits == "")
						otherkits = name;
					else
						otherkits = name + ", " + otherkits;
				}
			}
			Integer otherline = otherkits.length();
			Integer yourline = yourkits.length();
			yourline = yourline + 11;
			otherline = otherline + 12;

			line++;
			while (otherline > 50) {
				line++;
				otherline = otherline - 50;
			}
			line++;
			while (yourline > 50) {
				line++;
				yourline = yourline - 50;
			}
			p.sendMessage("§bAvailable kits: §7(/kit [KitName])");
			p.sendMessage("");
			p.sendMessage("§aYour kits: §f" + yourkits);
			p.sendMessage("§aOther kits: §f" + otherkits);
			p.sendMessage("");
			p.sendMessage("§bMore kits available at: " + BGMain.KIT_BUY_WEB);

			if (PLAYER_MSG.containsKey(p))
				line++;

			while (line <= 10) {
				p.sendMessage("");
				line++;
			}

			if (PLAYER_MSG.containsKey(p))
				p.sendMessage(PLAYER_MSG.get(p));

		} else if (KITINFO_CHAT.containsKey(p)) {
			Integer line = 0;
			String kitname = KITINFO_CHAT.get(p);
			String kitinfoname = kitname;
			kitname = kitname.toLowerCase();
			ConfigurationSection kit = BGFiles.kitconf
					.getConfigurationSection(kitname);

			char[] stringArray = kitinfoname.toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitinfoname = new String(stringArray);

			p.sendMessage("§a" + kitinfoname + " kit includes:");
			p.sendMessage("");
			line = line + 3;
			List<String> kititems = kit.getStringList("ITEMS");
			for (String item : kititems) {
				String[] oneitem = item.split(",");

				String itemstring = null;
				Integer id = null;
				Integer amount = null;
				String enchantment = null;
				String ench_numb = null;

				if (oneitem[0].contains(":")) {
					String[] ITEM_ID = oneitem[0].split(":");
					id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
					amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
				} else {
					id = Integer.valueOf(Integer.parseInt(oneitem[0]));
					amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
				}

				itemstring = " - "
						+ amount
						+ "x "
						+ Material.getMaterial(id.intValue()).toString()
								.replace("_", " ").toLowerCase();

				if (oneitem.length == 4) {
					enchantment = Enchantment
							.getById(Integer.parseInt(oneitem[2])).getName()
							.toLowerCase();
					ench_numb = oneitem[3];

					itemstring = itemstring + " with " + enchantment + " "
							+ ench_numb;
				}
				line++;
				p.sendMessage("§f" + itemstring);
			}

			List<Integer> abils = kit.getIntegerList("ABILITY");
			for(Integer abil : abils) {
				String desc = getAbilityDesc(abil.intValue());
				if (desc != null) {
					p.sendMessage("§f - " + desc);
					line++;
				}
			}
			
			List<String> pots = kit.getStringList("POTION");
			for(String pot : pots) {	
				if (pot != null & pot != "") {
					if (!pot.equals(0)) {
						String[] potion = pot.split(",");
						if (Integer.parseInt(potion[0]) != 0) {
							PotionEffectType pt = PotionEffectType.getById(Integer.parseInt(potion[0]));
							String name = pt.getName();
							if (Integer.parseInt(potion[1]) == 0) {
								name += " (Duration: infinitely)";
							} else {
								name += " (Duration: "+potion[1]+" sec)";
							}
							p.sendMessage("§f - " + name);
							line++;
						}
					}
				}
			}
			
			if(BGKit.getCoins(kitname.toLowerCase()) == 1) {
				p.sendMessage("§fPRICE: "+ BGKit.getCoins(kitname.toLowerCase())+ " Coin");
				line++;
			} else if(BGKit.getCoins(kitname.toLowerCase()) > 1) {
				p.sendMessage("§fPRICE: "+ BGKit.getCoins(kitname.toLowerCase())+ " Coins");
				line++;
			}
			
			if (PLAYER_MSG.containsKey(p))
				line++;

			while (line <= 10) {
				p.sendMessage("");
				line++;
			}
			if (PLAYER_MSG.containsKey(p))
				p.sendMessage("§7" + PLAYER_MSG.get(p));

		} else {
			p.sendMessage("§9" + INFO_MSG);
			p.sendMessage("§c" + DEATH_MSG);
			if (BGMain.GAMESTATE != GameState.GAME)
				p.sendMessage("§a" + TIMER_MSG);
			else {
				Integer timeleft = BGMain.MAX_GAME_RUNNING_TIME
						- BGMain.GAME_RUNNING_TIME;
				String minute = "minutes";
				if (timeleft <= 1) {
					minute = "minute";
				}

				p.sendMessage("§a" + "Players remaining: §f"
						+ BGMain.getGamers().length + " §a| Time remaining: §f"
						+ timeleft + " " + minute);
			}
			p.sendMessage("-----------------------------------------------------");
			if (!PLAYER_MSG.containsKey(p))
				p.sendMessage(CHAT_MSG[5]);
			p.sendMessage(CHAT_MSG[4]);
			p.sendMessage(CHAT_MSG[3]);
			p.sendMessage(CHAT_MSG[2]);
			p.sendMessage(CHAT_MSG[1]);
			p.sendMessage(CHAT_MSG[0]);
			if (PLAYER_MSG.containsKey(p))
				p.sendMessage(PLAYER_MSG.get(p));
		}

	}

	public static String getAbilityDesc(Integer ability) {
		if (ability == 0)
			return null;

		if (ABILITY_DESC.containsKey(ability))
			return ABILITY_DESC.get(ability);
		else
			return null;
	}

	public static void setAbilityDesc(Integer ability, String description)
			throws Error {
		if (ABILITY_DESC.containsKey(ability))
			throw new Error(
					"Cannot overwrite existing descriptions of abilties.");
		else
			ABILITY_DESC.put(ability, description);
	}

}
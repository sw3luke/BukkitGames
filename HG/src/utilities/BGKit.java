package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import main.BGMain;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BGKit {
	private static BGMain plugin;
	static Logger log = BGMain.getPluginLogger();
	static HashMap<Player, String> KIT = new HashMap<Player, String>();
	public static ArrayList<String> kits = new ArrayList<String>();

	public BGKit(BGMain instance) {
		plugin = instance;
		
		List<String> kitList = BGFiles.kitconf.getStringList("KITS");
		for(String kit : kitList) {
			kit = kit.toLowerCase();
			if(kit == "default")
				log.warning("There can not be a kit with the name 'default'!");
			else
				kits.add(kit);
		}
	}

	public static void giveKit(Player p) {
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		
		if (!KIT.containsKey(p)) {
			if (plugin.COMPASS.booleanValue()) {
				p.getInventory().addItem(
						new ItemStack[] { new ItemStack(Material.COMPASS, 1) });
			}
			
			if (plugin.DEFAULT_KIT) {
				ConfigurationSection def = BGFiles.kitconf.getConfigurationSection("default");
				setKitDisplayName(p, "Default");
				
				List<String> kititems = def.getStringList("ITEMS");
				for (String item : kititems) {
					String[] oneitem = item.split(",");
					ItemStack i = null;
					Integer id = null;
					Integer amount = null;
					Short durability = null;
					if (oneitem[0].contains(":")) {
						String[] ITEM_ID = oneitem[0].split(":");
						id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
						amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
						durability = Short.valueOf(Short.parseShort(ITEM_ID[1]));
						i = new ItemStack(id.intValue(), amount.intValue(),
								durability.shortValue());
					} else {
						id = Integer.valueOf(Integer.parseInt(oneitem[0]));
						amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
						i = new ItemStack(id.intValue(), amount.intValue());
					}

					if (oneitem.length == 4) {
						i.addUnsafeEnchantment(
								Enchantment.getById(Integer.parseInt(oneitem[2])),
								Integer.parseInt(oneitem[3]));
					}

					if ((id.intValue() < 298) || (317 < id.intValue())) {
						p.getInventory().addItem(new ItemStack[] { i });
					} else if ((id.intValue() == 298) || (id.intValue() == 302)
							|| (id.intValue() == 306) || (id.intValue() == 310)
							|| (id.intValue() == 314)) {
						i.setAmount(1);
						p.getInventory().setHelmet(i);
					} else if ((id.intValue() == 299) || (id.intValue() == 303)
							|| (id.intValue() == 307) || (id.intValue() == 311)
							|| (id.intValue() == 315)) {
						i.setAmount(1);
						p.getInventory().setChestplate(i);
					} else if ((id.intValue() == 300) || (id.intValue() == 304)
							|| (id.intValue() == 308) || (id.intValue() == 312)
							|| (id.intValue() == 316)) {
						i.setAmount(1);
						p.getInventory().setLeggings(i);
					} else if ((id.intValue() == 301) || (id.intValue() == 305)
							|| (id.intValue() == 309) || (id.intValue() == 313)
							|| (id.intValue() == 317)) {
						i.setAmount(1);
						p.getInventory().setBoots(i);
					}
				}

				List<String> pots = def.getStringList("POTION");
				for(String pot : pots) {	
					if (pot != null & pot != "") {
						if (!pot.equals(0)) {
							String[] potion = pot.split(",");
							if (Integer.parseInt(potion[0]) != 0) {
								if (Integer.parseInt(potion[1]) == 0) {
									p.addPotionEffect(new PotionEffect(PotionEffectType
											.getById(Integer.parseInt(potion[0])),
											plugin.MAX_GAME_RUNNING_TIME * 1200, Integer
											.parseInt(potion[2])));
								} else {
									p.addPotionEffect(new PotionEffect(PotionEffectType
											.getById(Integer.parseInt(potion[0])), Integer
											.parseInt(potion[1]) * 20, Integer
											.parseInt(potion[2])));
								}
							}
						}
					}
				}
			}
			
			return;
		}

		String kitname = (String) KIT.get(p);
		ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname
				.toLowerCase());

		List<String> kititems = kit.getStringList("ITEMS");
		for (String item : kititems) {
			String[] oneitem = item.split(",");
			ItemStack i = null;
			Integer id = null;
			Integer amount = null;
			Short durability = null;
			if (oneitem[0].contains(":")) {
				String[] ITEM_ID = oneitem[0].split(":");
				id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
				amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
				durability = Short.valueOf(Short.parseShort(ITEM_ID[1]));
				i = new ItemStack(id.intValue(), amount.intValue(),
						durability.shortValue());
			} else {
				id = Integer.valueOf(Integer.parseInt(oneitem[0]));
				amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
				i = new ItemStack(id.intValue(), amount.intValue());
			}

			if (oneitem.length == 4) {
				i.addUnsafeEnchantment(
						Enchantment.getById(Integer.parseInt(oneitem[2])),
						Integer.parseInt(oneitem[3]));
			}

			if ((id.intValue() < 298) || (317 < id.intValue())) {
				p.getInventory().addItem(new ItemStack[] { i });
			} else if ((id.intValue() == 298) || (id.intValue() == 302)
					|| (id.intValue() == 306) || (id.intValue() == 310)
					|| (id.intValue() == 314)) {
				i.setAmount(1);
				p.getInventory().setHelmet(i);
			} else if ((id.intValue() == 299) || (id.intValue() == 303)
					|| (id.intValue() == 307) || (id.intValue() == 311)
					|| (id.intValue() == 315)) {
				i.setAmount(1);
				p.getInventory().setChestplate(i);
			} else if ((id.intValue() == 300) || (id.intValue() == 304)
					|| (id.intValue() == 308) || (id.intValue() == 312)
					|| (id.intValue() == 316)) {
				i.setAmount(1);
				p.getInventory().setLeggings(i);
			} else if ((id.intValue() == 301) || (id.intValue() == 305)
					|| (id.intValue() == 309) || (id.intValue() == 313)
					|| (id.intValue() == 317)) {
				i.setAmount(1);
				p.getInventory().setBoots(i);
			}
		}

		List<String> pots = kit.getStringList("POTION");
		for(String pot : pots) {	
			if (pot != null & pot != "") {
				if (!pot.equals(0)) {
					String[] potion = pot.split(",");
					if (Integer.parseInt(potion[0]) != 0) {
						if (Integer.parseInt(potion[1]) == 0) {
							p.addPotionEffect(new PotionEffect(PotionEffectType
									.getById(Integer.parseInt(potion[0])),
									plugin.MAX_GAME_RUNNING_TIME * 1200, Integer
									.parseInt(potion[2])));
						} else {
							p.addPotionEffect(new PotionEffect(PotionEffectType
									.getById(Integer.parseInt(potion[0])), Integer
									.parseInt(potion[1]) * 20, Integer
									.parseInt(potion[2])));
						}
					}
				}
			}
		}

		if (plugin.COMPASS.booleanValue())
			p.getInventory().addItem(
					new ItemStack[] { new ItemStack(Material.COMPASS, 1) });
	}

	public static void setKit(Player player, String kitname) {
		kitname = kitname.toLowerCase();
		kitname = kitname.replace(".", "");
		ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname);

		if (kit == null  && !kits.contains(kitname)) {
			BGChat.printPlayerChat(player, "That kit doesn't exist!");
			return;
		}
		if(KIT.get(player) == kitname)
			return;

		if (player.hasPermission("bg.kit." + kitname)
				|| player.hasPermission("bg.kit.*") || (plugin.SIMP_REW && plugin.winner(player))
				|| (plugin.REW && plugin.reward.BOUGHT_KITS.get(player.getName()) != null
					&& plugin.reward.BOUGHT_KITS.get(player.getName()).equals(kitname))) {
			if (KIT.containsKey(player)) {
				KIT.remove(player);
			}

			KIT.put(player, kitname);
			char[] stringArray = kitname.toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			kitname = new String(stringArray);
			BGChat.printPlayerChat(player, "You have chosen " + kitname
					+ " as your kit.");
			
			setKitDisplayName(player, kitname);

		} else {
			BGChat.printPlayerChat(player, plugin.NO_KIT_MSG);
			return;
		}
	}

	private static void setKitDisplayName(Player player, String kitname) {
		if(!plugin.KIT_PREFIX)
			return;
		
		if (plugin.winner(player))
			player.setDisplayName("§8[" + kitname + "] §r" + ChatColor.GOLD
					+ player.getName() + ChatColor.WHITE);
		else if (player.hasPermission("bg.admin.color")
				|| player.hasPermission("bg.admin.*"))
			player.setDisplayName("§8[" + kitname + "] §r" + ChatColor.RED
					+ player.getName() + ChatColor.WHITE);
		else if (player.hasPermission("bg.vip.color")
				|| player.hasPermission("bg.vip.*"))
			player.setDisplayName("§8[" + kitname + "] §r" + ChatColor.BLUE
					+ player.getName() + ChatColor.WHITE);
		else
			player.setDisplayName("§8[" + kitname + "] §r"
					+ ChatColor.WHITE + player.getName() + ChatColor.WHITE);
	}
	
	public static Boolean hasAbility(Player player, Integer ability) {
		
		if (!KIT.containsKey(player)) {
			if (plugin.DEFAULT_KIT) {
				
				ConfigurationSection def = BGFiles.kitconf.getConfigurationSection("default");
				
				List<Integer> s = def.getIntegerList("ABILITY");
				for(Integer i : s) {
					if (i == ability) {
					return true;
					}
					continue;
				}
				return false;
			}else {
				return false;
			}
		}

		String kitname = getKit(player);
		ConfigurationSection kit = BGFiles.kitconf.getConfigurationSection(kitname);

		List<Integer> s = kit.getIntegerList("ABILITY");
		for(Integer i : s) {
			if (i == ability) {
			return true;
			}
			continue;
		}
		return false;
	}

	public static String getKit(Player player) {
		String kitname = KIT.get(player);
		return kitname;
	}
	
	public static int getCoins(String kitName) {
		
		ConfigurationSection def = BGFiles.kitconf.getConfigurationSection(kitName);
		
		return def.getInt("COINS");
	}
	
	public static boolean isKit(String kitName) {
		
		if(kits.contains(kitName))
			return true;
		return false;
	}
}
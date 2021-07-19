package tv.sushidog.esclandia;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class cmd_warp implements CommandExecutor {
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length == 0) {
				showCommands(player);
			} else {
				switch (args[0]) {
				case "add":
					if (player.hasPermission("warp.add")) {
						if (args.length > 1 && args[1].length() > 0) {
							onWarpAdd(player, args[1]);
						} else {
							player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.GOLD + "Usage: /warp add <name>");
						}
					} else {
						player.sendMessage(ChatColor.DARK_PURPLE + "[ESClandia] " + ChatColor.RED + "You don't have permission to use this command!");
					}

					break;

				case "delete":
					if (player.hasPermission("warp.delete")) {
						if (args.length > 1 && args[1].length() > 0) {
							onWarpDelete(player, args[1]);
						} else {
							player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.GOLD + "Usage: /warp delete <name>");
						}
					} else {
						player.sendMessage(ChatColor.DARK_PURPLE + "[ESClandia] " + ChatColor.RED + "You don't have permission to use this command!");
					}

					break;

				case "help":
					showCommands(player);

					break;

				case "list":
					showWarpList(player);
					break;

				default:
					warpPlayer(player, args[0]);
				}
			}
		}

		return true;
	}

	private void showCommands (Player player) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.GOLD + "Available warp commands:");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RESET + "/warp <name> - " + ChatColor.ITALIC + " Warp to the destination");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RESET + "add - " + ChatColor.ITALIC + " Create a new warp");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RESET + "delete - " + ChatColor.ITALIC + " Delete an existing warp");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RESET + "list - " + ChatColor.ITALIC + " View the list of warps");
	}

	private void onWarpAdd (Player player, String name) {
		File warpFile = new File("plugins/Esclandia/warps.yml");
		FileConfiguration warps = YamlConfiguration.loadConfiguration(warpFile);

		List<String> list = warps.getStringList("warps.list");
		list.add(name);
		warps.set("warps.list", list);

		if (warps.contains("warps." + name) == false) {
			Location location = player.getLocation();
			warps.set("warps." + name + ".world", location.getWorld().getName());
			warps.set("warps." + name + ".x", location.getX());
			warps.set("warps." + name + ".y", location.getY());
			warps.set("warps." + name + ".z", location.getZ());
			warps.set("warps." + name + ".yaw", location.getYaw());
			warps.set("warps." + name + ".pitch", location.getPitch());
		}

		try {
			warps.save(warpFile);
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.GREEN + "Warp '" + name + "' has been created!");
		} catch (IOException error) {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RED + "Error: Failed to create Warp " + name);
		}
	}

	private void onWarpDelete (Player player, String name) {
		File warpFile = new File("plugins/Esclandia/warps.yml");
		FileConfiguration warps = YamlConfiguration.loadConfiguration(warpFile);

		List<String> list = warps.getStringList("warps.list");
		list.remove(name);
		warps.set("warps.list", list);

		if (warps.contains("warps." + name) == true) {
			warps.set("warps." + name, null);
		}

		try {
			warps.save(warpFile);
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.GREEN + "Warp '" + name + "' has been deleted!");
		} catch (IOException error) {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RED + "Error: Failed to delete Warp " + name);
		}
	}

	private void showWarpList (Player player) {
		File warpFile = new File("plugins/Esclandia/warps.yml");
		FileConfiguration warps = YamlConfiguration.loadConfiguration(warpFile);

		List<String> list = warps.getStringList("warps.list");

		StringBuilder strbul = new StringBuilder();
		for (String str : list) {
			strbul.append(str + ", ");
		}

		if (strbul.length() > 0) {
			strbul.setLength(strbul.length() - 2);
			String warpList = strbul.toString();

			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.GOLD + "Available warps: " + ChatColor.RESET + warpList);
		} else {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RED + "There are no warps available");
		}
	}

	private void warpPlayer (Player player, String name) {
		File warpFile = new File("plugins/Esclandia/warps.yml");
		FileConfiguration warps = YamlConfiguration.loadConfiguration(warpFile);

		List<String> list = warps.getStringList("warps.list");
		

		String found = listContainsIndex(name, list);

		if (found != null) {
			String world = warps.getString("warps." + found + ".world");
			Double x = warps.getDouble("warps." + found + ".x");
			Double y = warps.getDouble("warps." + found + ".y");
			Double z = warps.getDouble("warps." + found + ".z");
			Float yaw = (float) warps.getDouble("warps." + found + ".yaw");
			Float pitch = (float) warps.getDouble("warps." + found + ".pitch");

			Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			player.teleport(location);
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RESET + "Warping to " + found);
			System.out.println("[ESClandia] " + player.getName() + " has warped to " + found);
		} else {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Warp] " + ChatColor.RED + "Error: Invalid Warp " + name + "!");
		}
	}

	private String listContainsIndex (String item, List<String> list) {
		Integer index = 0;

		for (String element : list) {
			if (element.equalsIgnoreCase(item) == true) {
				return element;
			}

			index ++;
		}

		return null;
	}
}

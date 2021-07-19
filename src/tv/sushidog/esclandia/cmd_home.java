package tv.sushidog.esclandia;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class cmd_home implements CommandExecutor {
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length == 0) {
				warpHome(player);
			} else {
				switch (args[0]) {
				case "delete":
					onHomeDelete(player);

					break;

				case "help":
					showCommands(player);

					break;

				case "set":
					onHomeSet(player);

					break;

				default:
					// Admin permissions: Check if valid player name
				}
			}
		}

		return true;
	}

	private void showCommands (Player player) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.GOLD + "Available home commands:");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.RESET + "/home - " + ChatColor.ITALIC + " Teleport home");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.RESET + "set - " + ChatColor.ITALIC + " Set your home warp");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.RESET + "delete - " + ChatColor.ITALIC + " Delete your home warp");
	}

	private void onHomeSet (Player player) {
		File userFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");
		FileConfiguration user = YamlConfiguration.loadConfiguration(userFile);

		Location location = player.getLocation();
		user.set("home.deleted", false);
		user.set("home.world", location.getWorld().getName());
		user.set("home.x", location.getX());
		user.set("home.y", location.getY());
		user.set("home.z", location.getZ());
		user.set("home.yaw", location.getYaw());
		user.set("home.pitch", location.getPitch());

		try {
			user.save(userFile);
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.GREEN + "Your home warp has been set!");
		} catch (IOException error) {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.RED + "Error: Failed to set your home warp!");
		}
	}

	private void onHomeDelete (Player player) {
		File userFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");
		FileConfiguration user = YamlConfiguration.loadConfiguration(userFile);

		if (user.getBoolean("home.deleted") == false) {
			user.set("home.deleted", true);

			try {
				user.save(userFile);
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.GREEN + "Your home warp has been deleted!");
			} catch (IOException error) {
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.RED + "Error: Failed to delete your home warp!");
			}
		} else {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.RED + "You do not have a home warp set!");
		}
	}

	private void warpHome (Player player) {
		File userFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");
		FileConfiguration user = YamlConfiguration.loadConfiguration(userFile);

		if (user.getBoolean("home.deleted") == false) {
			String world = user.getString("home.world");
			Double x = user.getDouble("home.x");
			Double y = user.getDouble("home.y");
			Double z = user.getDouble("home.z");
			Float yaw = (float) user.getDouble("home.yaw");
			Float pitch = (float) user.getDouble("home.pitch");
	
			Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			player.teleport(location);
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.RESET + "Warping home");
		} else {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Home] " + ChatColor.RED + "You do not have a home warp set!");
		}
	}
}

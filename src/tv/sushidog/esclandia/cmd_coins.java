package tv.sushidog.esclandia;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class cmd_coins implements CommandExecutor {
	private Esclandia plugin = Esclandia.get();
	private String currency;

	public cmd_coins () {
		File economyFile = new File("plugins/Esclandia/economy.yml");
		FileConfiguration economy = YamlConfiguration.loadConfiguration(economyFile);
		this.currency = economy.getString("currency");
	}

	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length == 0) {
				showCoinBalance(player);
			} else {
				switch (args[0]) {
				case "add":
					if (player.hasPermission("coins.add")) {
						if (args.length > 2 && args[1].length() > 0) {
							Player target = Bukkit.getPlayer(args[1]);
	
							if (target instanceof Player && target.getName() != "null" &&
								plugin.isNumeric(args[2])) {
								addCoins(player, target, Integer.parseInt(args[2]));
							} else {
								showAddUsage(player);
							}
						} else {
							showAddUsage(player);
						}
					} else {
						player.sendMessage(ChatColor.DARK_PURPLE + "[ESClandia] " + ChatColor.RED + "You don't have permission to use this command!");
					}

					break;

				case "help":
					showCommands(player);

					break;

				case "give":
					if (args.length > 2 && args[1].length() > 0) {
						Player target = Bukkit.getPlayer(args[1]);

						if (target instanceof Player && target.getName() != "null" &&
							plugin.isNumeric(args[2])) {
							giveCoins(player, target, Integer.parseInt(args[2]));
						} else {
							showGiveUsage(player);
						}
					} else {
						showGiveUsage(player);
					}

					break;

				case "take":
					if (player.hasPermission("coins.take")) {
						if (args.length > 2 && args[1].length() > 0) {
							Player target = Bukkit.getPlayer(args[1]);
	
							if (target instanceof Player && target.getName() != "null" &&
								plugin.isNumeric(args[2])) {
								takeCoins(player, target, Integer.parseInt(args[2]));
							} else {
								showTakeUsage(player);
							}
						} else {
							showTakeUsage(player);
						}
					} else {
						player.sendMessage(ChatColor.DARK_PURPLE + "[ESClandia] " + ChatColor.RED + "You don't have permission to use this command!");
					}

					break;

				default:
					// Admin permissions: Check if valid player name
				}
			}
		}

		return true;
	}

	private void showCommands (Player player) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "Available coins commands:");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.RESET + "give - " + ChatColor.ITALIC + " Give coins to another player");
	}

	private void showCoinBalance (Player player) {
		File userFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");
		FileConfiguration user = YamlConfiguration.loadConfiguration(userFile);

		Integer balance = user.getInt("coins");
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GREEN + "You have " + balance + " Daisy Coins");
	}

	private void showAddUsage (Player player) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "Usage: /coins add <player> <amount>");
	}

	private void showGiveUsage (Player player) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "Usage: /coins give <player> <amount>");
	}

	private void showTakeUsage (Player player) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "Usage: /coins take <player> <amount>");
	}

	private void giveCoins (Player player, Player target, Integer amount) {
		File userFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");
		FileConfiguration user = YamlConfiguration.loadConfiguration(userFile);

		File targetUserFile = new File("plugins/Esclandia/Players/" + target.getUniqueId() + ".yml");
		FileConfiguration targetUser = YamlConfiguration.loadConfiguration(targetUserFile);

		Integer balance = user.getInt("coins");

		if (amount > 0 && balance >= amount && player.getName() != target.getName()) {
			user.set("coins", balance - amount);
			targetUser.set("coins", balance + amount);

			try {
				user.save(userFile);
				targetUser.save(targetUserFile);
			} catch (IOException error) {
				System.out.println("[ESClandia] Error saving player data on /coins give");
				System.out.println(error);
			}

			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "You gave " + amount + " " + this.currency + " to " + ChatColor.RED + target.getName());
			target.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "You received " + amount + " " + this.currency + " from " + ChatColor.RED + player.getName());
		} else {
			if (player.getName() == target.getName()) {
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.RED + "You can't give " + this.currency + " to yourself!");
			} else if (balance < amount) {
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.RED + "You have insufficient funds!");
			} else if (amount == 0) {
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.RED + "You can't give nothing!");
			}
		}
	}

	private void addCoins (Player player, Player target, Integer amount) {
		File targetUserFile = new File("plugins/Esclandia/Players/" + target.getUniqueId() + ".yml");
		FileConfiguration targetUser = YamlConfiguration.loadConfiguration(targetUserFile);

		Integer balance = targetUser.getInt("coins");

		if (amount > 0) {
			targetUser.set("coins", balance + amount);

			try {
				targetUser.save(targetUserFile);
			} catch (IOException error) {
				System.out.println("[ESClandia] Error saving player data on /coins add");
				System.out.println(error);
			}

			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "You gave " + amount + " " + this.currency + " to " + ChatColor.RED + target.getName());
			target.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "You received " + amount + " " + this.currency);
		} else {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.RED + "You can't give nothing!");
		}
	}

	private void takeCoins (Player player, Player target, Integer amount) {
		File targetUserFile = new File("plugins/Esclandia/Players/" + target.getUniqueId() + ".yml");
		FileConfiguration targetUser = YamlConfiguration.loadConfiguration(targetUserFile);

		Integer balance = targetUser.getInt("coins");

		if (amount > 0) {
			targetUser.set("coins", balance - amount);

			try {
				targetUser.save(targetUserFile);
			} catch (IOException error) {
				System.out.println("[ESClandia] Error saving player data on /coins take");
				System.out.println(error);
			}

			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + "You took " + amount + " " + this.currency + " from " + ChatColor.RED + target.getName());
			target.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.GOLD + amount + " " + this.currency + " were taken from you");
		} else {
			player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Coins] " + ChatColor.RED + "You can't take nothing!");
		}
	}
}

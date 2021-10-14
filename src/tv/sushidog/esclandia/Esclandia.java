package tv.sushidog.esclandia;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Esclandia extends JavaPlugin implements Listener {
	private static Esclandia plugin;
	public Shop shop;
	public EscaCola escaCola;
	public Crops crops;

	@Override
    public void onEnable () {
		plugin = this;

		this.shop = new Shop();
		this.escaCola = new EscaCola();
		this.crops = new Crops();

		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getPluginManager().registerEvents(this.shop, this);
		this.getServer().getPluginManager().registerEvents(this.escaCola, this);
		this.getServer().getPluginManager().registerEvents(this.crops, this);

		this.getCommand("coins").setExecutor(new cmd_coins());
		this.getCommand("escacola").setExecutor(new cmd_escacola());
		this.getCommand("home").setExecutor(new cmd_home());
		this.getCommand("shop").setExecutor(new cmd_shop());
		this.getCommand("warp").setExecutor(new cmd_warp());

		pluginInit();
    }

	@Override
    public void onDisable () {
		plugin = null;
    }

	private void pluginInit () {
		File playerFolder = new File("plugins/Esclandia/Players");
		File warpFile = new File("plugins/Esclandia/warps.yml");
		File economyFile = new File("plugins/Esclandia/economy.yml");
		File shopsFile = new File("plugins/Esclandia/shops.yml");

		if (playerFolder.exists() == false) {
			try {
				playerFolder.mkdirs();
			} catch (Exception error) {
				System.out.println(error);
			}
		}

		if (warpFile.exists() == false) {
			try {
				warpFile.createNewFile();
				System.out.println("[ESClandia] warps.yml created");
			} catch (IOException error) {
				System.out.println("[ESClandia] Error creating warps.yml");
				System.out.println(error);
			}
		} else {
			System.out.println("[ESClandia] warps.yml found");
		}

		if (economyFile.exists() == false) {
			try {
				economyFile.createNewFile();
				economyInit();

				System.out.println("[ESClandia] economy.yml created");
			} catch (IOException error) {
				System.out.println("[ESClandia] Error creating economy.yml");
				System.out.println(error);
			}
		} else {
			System.out.println("[ESClandia] economy.yml found");
		}

		if (shopsFile.exists() == false) {
			try {
				shopsFile.createNewFile();
				shopsInit();

				System.out.println("[ESClandia] shops.yml created");
			} catch (IOException error) {
				System.out.println("[ESClandia] Error creating shops.yml");
				System.out.println(error);
			}
		} else {
			System.out.println("[ESClandia] shops.yml found");
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
			  saveAllPlayerData();
		  }
		}, 0, 1000 * 60 * 5);
	}

	public static Esclandia get () {
		return plugin;
	}

	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent event) {
		Player player = event.getPlayer();

		playerInit(player);

		if (player.isOp()) {
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[ESClandia]" + ChatColor.RESET + " Plugin is active. Use '/help esclandia' for more information.");
		}
	}

	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event) {
		Player player = event.getPlayer();

		savePlayerData(player);
	}

	private void savePlayerData (Player player) {
		File userFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");
		FileConfiguration user = YamlConfiguration.loadConfiguration(userFile);

		Location location = player.getLocation();
		user.set("lastLocation.world", location.getWorld().getName());
		user.set("lastLocation.x", location.getX());
		user.set("lastLocation.y", location.getY());
		user.set("lastLocation.z", location.getZ());
		user.set("lastLocation.yaw", location.getYaw());
		user.set("lastLocation.pitch", location.getPitch());

		try {
			user.save(userFile);
		} catch (IOException error) {
			System.out.println("[ESClandia] Error saving player data for " + player.getName());
		}
	}

	private void saveAllPlayerData () {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			savePlayerData(player);
		}
	}

	private void playerInit (Player player) {
		// Create player file if it doesn't exist
		File playerFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");

		if (playerFile.exists() == false) {
			try {
				playerFile.createNewFile();

				File userFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");
				FileConfiguration user = YamlConfiguration.loadConfiguration(userFile);

				Timestamp lastLogin = new Timestamp(System.currentTimeMillis());

				user.set("name", player.getName());
				user.set("ipAddress", player.getAddress().getAddress().getHostAddress());
				user.set("lastLogin", lastLogin);
				user.set("coins", 25);

				Location location = player.getLocation();
				user.set("lastLocation.world", location.getWorld().getName());
				user.set("lastLocation.x", location.getX());
				user.set("lastLocation.y", location.getY());
				user.set("lastLocation.z", location.getZ());
				user.set("lastLocation.yaw", location.getYaw());
				user.set("lastLocation.pitch", location.getPitch());

				user.set("home.deleted", true);
				user.save(userFile);

				System.out.println("[ESClandia] Player file created for " + player.getName());
			} catch (IOException error) {
				System.out.println("[ESClandia] Error creating player file for " + player.getName());
				System.out.println(error);
			}
		} else {
			File userFile = new File("plugins/Esclandia/Players/" + player.getUniqueId() + ".yml");
			FileConfiguration user = YamlConfiguration.loadConfiguration(userFile);

			Timestamp lastLogin = new Timestamp(System.currentTimeMillis());

			user.set("name", player.getName());
			user.set("ipAddress", player.getAddress().getAddress().getHostAddress());
			user.set("lastLogin", lastLogin);

			try {
				user.save(userFile);
			} catch (IOException error) {
				System.out.println("[ESClandia] Error saving player file for " + player.getName());
			}

			System.out.println("[ESClandia] Player file found for " + player.getName());
		}
	}

	private void economyInit () {
		File economyFile = new File("plugins/Esclandia/economy.yml");
		FileConfiguration economy = YamlConfiguration.loadConfiguration(economyFile);

		economy.set("currency", "Daisy Coins");
		economy.set("coins", 1000);
		economy.set("land.taxes", 5);
		economy.set("shops.fee", 30);
		economy.set("shops.taxes", 5);

		try {
			economy.save(economyFile);
		} catch (IOException error) {
			System.out.println("[ESClandia] Error saving economy.yml");
			System.out.println(error);
		}
	}

	private void shopsInit () {
		File shopsFile = new File("plugins/Esclandia/shops.yml");
		FileConfiguration shops = YamlConfiguration.loadConfiguration(shopsFile);

		try {
			shops.save(shopsFile);
		} catch (IOException error) {
			System.out.println("[ESClandia] Error saving shops.yml");
			System.out.println(error);
		}
	}

	public boolean isNumeric (String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception error) {
			return false;
		}
	}
}

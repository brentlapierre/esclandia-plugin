package tv.sushidog.esclandia;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;

public class cmd_shop implements CommandExecutor {
	private Esclandia plugin = Esclandia.get();

	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (player.getName().equals("craft_my_name")) {
				openShopManager(player);
			} else {
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESClandia] " + ChatColor.RESET + "The Shop feature is currently disabled!");
			}
		}

		return true;
	}

	private void openShopManager (Player player) {
		Inventory gui = plugin.shop.shopManager();
		plugin.shop.openGUI(gui, player);
	}
}

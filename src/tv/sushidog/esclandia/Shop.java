package tv.sushidog.esclandia;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.wesjd.anvilgui.AnvilGUI;

public class Shop implements Listener {
	private Esclandia plugin = Esclandia.get();

	@EventHandler
	public void onClick (InventoryClickEvent event) {
		String opened = event.getView().getTitle();
		Integer slot = event.getSlot();
		Player player = (Player) event.getWhoClicked();

		if (opened.contains("Shop Manager") == true) {
			switch (slot) {
			case 0: // Create Shop
				this.closeGUI(player);
				this.createShop(player);

				break;

			case 1: // Manage Shop
				this.closeGUI(player);
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Shop] " + ChatColor.RESET + "Shop Manager: Manage Shop");

				break;

			case 2: // Delete Shop
				this.closeGUI(player);
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Shop] " + ChatColor.RESET + "Shop Manager: Delete Shop");

				break;

			}

			event.setCancelled(true);
		} else if (opened.contains("Shop Create") == true) {
			switch (slot) {
			case 0: // Cancel Shop Creation
				this.closeGUI(player);
				Inventory gui = this.shopManager();
				this.openGUI(gui, player);

				break;

			case 2: // Create Shop
				player.sendMessage(ChatColor.DARK_PURPLE + "[ESC.Shop] " + ChatColor.RESET + "DEV: Shop Create - Name: ");

				break;

			}
		}
	}

	public Inventory shopManager () {
		GuiManager gui = new GuiManager();
		gui.create(ChatColor.DARK_PURPLE + "Shop Manager", InventoryType.CHEST, 9);

		/**
		 * Shop Manager
		 * ------
		 * 
		 * Create: New Shop
		 */
		File economyFile = new File("plugins/Esclandia/economy.yml");
		FileConfiguration economy = YamlConfiguration.loadConfiguration(economyFile);
		String currency = economy.getString("currency");
		Integer shopFee = economy.getInt("shops.fee");
		Integer shopTax = economy.getInt("shops.tax");

		ItemStack shopCreate = new ItemStack(Material.CRAFTING_TABLE);

		ItemMeta shopCreateMeta = shopCreate.getItemMeta();
		shopCreateMeta.setDisplayName(ChatColor.GREEN + "Create");
		shopCreateMeta.setLore(Arrays.asList(
			ChatColor.GRAY + "Start your own shop",
			ChatColor.GRAY + "Fee: " + shopFee + " " + currency,
			ChatColor.GRAY + "Taxes (weekly): " + shopTax + " " + currency
		));
		shopCreate.setItemMeta(shopCreateMeta);

		gui.addItem(0, shopCreate);

		/**
		 * Shop Manager
		 * ------
		 * 
		 * Manage: Manage your shop
		 */
		ItemStack shopManage = new ItemStack(Material.WRITABLE_BOOK);

		ItemMeta shopManageMeta = shopManage.getItemMeta();
		shopManageMeta.setDisplayName(ChatColor.GOLD + "Manage");
		shopManageMeta.setLore(Arrays.asList(
			ChatColor.GRAY + "Configure your shop"
		));
		shopManage.setItemMeta(shopManageMeta);

		gui.addItem(1, shopManage);

		/**
		 * Shop Manager
		 * ------
		 * 
		 * Delete: Delete your shop
		 */
		ItemStack shopDelete = new ItemStack(Material.COMPOSTER);

		ItemMeta shopDeleteMeta = shopDelete.getItemMeta();
		shopDeleteMeta.setDisplayName(ChatColor.RED + "Delete");
		shopDeleteMeta.setLore(Arrays.asList(
			ChatColor.GRAY + "Delete your shop"
		));
		shopDelete.setItemMeta(shopDeleteMeta);

		gui.addItem(2, shopDelete);

		return gui.gui;
	}

	private void createShop (Player player) {
		/**
		 * Shop Create
		 * ------
		 * 
		 * Cancel: Cancel Shop Creation
		 */
		ItemStack cancel = new ItemStack(Material.BARRIER);

		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.setDisplayName(ChatColor.GOLD + "Cancel");
		cancelMeta.setLore(Arrays.asList(
			ChatColor.GRAY + "Go back to Shop Manager"
		));
		cancel.setItemMeta(cancelMeta);

		/**
		 * Shop Create
		 * ------
		 * 
		 * Create: Create Shop
		 */
		ItemStack create = new ItemStack(Material.LIME_CONCRETE);

		ItemMeta createMeta = create.getItemMeta();
		createMeta.setDisplayName(ChatColor.GREEN + "Create Shop");
		createMeta.setLore(Arrays.asList(
			ChatColor.GRAY + "Create your Shop"
		));
		create.setItemMeta(createMeta);

		new AnvilGUI.Builder()
			.plugin(this.plugin)
			.title(ChatColor.DARK_PURPLE + "Shop Create")
			.text(ChatColor.WHITE + "Name of your shop")
			.itemLeft(cancel)
			.itemRight(create)
			.onComplete((Player merchant, String text) -> {
				return AnvilGUI.Response.close();
			})
			.open(player)
		;
	}

	public void openGUI (Inventory gui, Player player) {
		player.openInventory(gui);
	}

	public void closeGUI (Player player) {
		player.closeInventory();
	}
}

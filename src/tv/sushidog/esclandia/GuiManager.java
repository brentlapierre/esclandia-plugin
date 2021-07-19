package tv.sushidog.esclandia;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiManager {
	public Inventory gui;

	public Inventory create (String title, InventoryType type, Integer size) {
		if (type == InventoryType.CHEST) {
			this.gui = Bukkit.getServer().createInventory(null, size, title);
		} else {
			this.gui = Bukkit.getServer().createInventory(null, type, title);
		}

		return this.gui;
	}

	public void addItem (Integer slot, ItemStack item) {
		this.gui.setItem(slot, item);
	}

	public void removeItem (Integer slot) {
		this.gui.setItem(slot, new ItemStack(Material.AIR));
	}
}

package tv.sushidog.esclandia;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Crops implements Listener {
	@EventHandler
	public void onSeedPlant (PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Material blockType = block.getType();
		ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
		Material heldItemType = heldItem.getType();

		if (blockType == Material.FARMLAND && heldItemType == Material.WHEAT_SEEDS) {
			int seedType = heldItem.getItemMeta().getCustomModelData();

			Damageable crop = (Damageable) block.getBlockData();
			crop.damage(Double.parseDouble("0." + seedType));
		}
	}
}

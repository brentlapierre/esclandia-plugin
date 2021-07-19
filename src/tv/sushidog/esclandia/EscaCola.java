package tv.sushidog.esclandia;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EscaCola implements Listener {
	@EventHandler
	public void onPlayerRightClicked (PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();

		if (entity instanceof Player && entity.getName().equals("Sushidog_Art")) {
			ItemStack item = player.getInventory().getItemInMainHand();

			// Fill glass bottle with ESCa Cola
			if (item.getType() == Material.GLASS_BOTTLE) {
				ItemStack escaCola = new ItemStack(Material.POTION, 1);
				PotionMeta meta = (PotionMeta) escaCola.getItemMeta();

				meta.setDisplayName("ESCa Cola");
				meta.setColor(Color.PURPLE);
				meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 2400, 5), true);
				meta.addCustomEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 2400, 5), true);
				meta.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 3), true);

				escaCola.setItemMeta(meta);

				item.setAmount(item.getAmount() - 1);

				player.getInventory().addItem(escaCola); // Add ESCa Cola to players inventory

				player.sendMessage("Your glass bottle has been filled with ESCa Cola");
				entity.sendMessage("A portion of your life energy has been drained by " + ChatColor.RED + player.getName());
			}
		}
	}
}

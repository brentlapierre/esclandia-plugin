package tv.sushidog.esclandia;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class cmd_escacola implements CommandExecutor {
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Player target;

			if (args.length == 0) {
				target = null;
			} else {
				target = Bukkit.getPlayer(args[0]);
			}

			ItemStack escaCola = new ItemStack(Material.POTION, 1);
			PotionMeta meta = (PotionMeta) escaCola.getItemMeta();

			meta.setDisplayName("ESCa Cola");
			meta.setColor(Color.PURPLE);
			meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 2400, 5), true);
			meta.addCustomEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 2400, 5), true);
			meta.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 3), true);

			escaCola.setItemMeta(meta);

			// Add ESCa Cola to players inventory
			if (target == null) {
				target = player;
			}

			target.getInventory().addItem(escaCola);
			target.sendMessage("You have received a bottle of " + ChatColor.DARK_PURPLE + "ESCa Cola!");
		}

		return true;
	}
}

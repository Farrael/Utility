package farrael.fr.utility.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

import farrael.fr.utility.configuration.Configuration;

public class EntityListener implements Listener {

	@EventHandler
	public void onEntityInteract(EntityInteractEvent event) {
		if(!Configuration.ENABLE || !Configuration.SOIL_PROTECT)
			return;

		if (event.getBlock().getType() == Material.SOIL && event.getEntity() instanceof Creature)
			event.setCancelled(true);
	}
}

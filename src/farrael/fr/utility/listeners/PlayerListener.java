package farrael.fr.utility.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import farrael.fr.utility.Utility;
import farrael.fr.utility.configuration.Configuration;
import farrael.fr.utility.utils.ChatUtils;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		if(Configuration.ENABLE)
			ChatUtils.sendTabListToServer();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		if(Configuration.ENABLE)
			Bukkit.getScheduler().runTaskLater(Utility.getInstance(), new Runnable(){
				@Override
				public void run() {
					ChatUtils.sendTabListToServer();
				}
			}, 10);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		if(Configuration.ENABLE)
			Bukkit.getScheduler().runTaskLater(Utility.getInstance(), new Runnable(){
				@Override
				public void run() {
					ChatUtils.sendTabListToServer();
				}
			}, 10);
	}

	@EventHandler
	public void onPingRequest(ServerListPingEvent event){
		if(Configuration.ENABLE && Configuration.MOTD_ENABLE)
			event.setMotd(Configuration.MOTD);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(!Configuration.ENABLE || !Configuration.SOIL_PROTECT)
			return;

		if(event.getAction() == Action.PHYSICAL) {
			Block block = event.getClickedBlock();

			if(block == null)
				return;

			if(block.getType().equals(Material.SOIL)){
				event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
				event.setCancelled(true);
			}
		}
	}
}

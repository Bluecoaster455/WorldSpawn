package me.bluecoaster455.worldspawn.listeners;

import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.SpawnWorld;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class WSListeners implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt){
		Player p = evt.getPlayer();
		if(WSConfig.isHubOnJoin()){
			Location loc = WSConfig.getHub();
			if(loc != null) {
				p.teleport(loc);
			}
		}
		else if(WSConfig.isSpawnOnJoin()){
			SpawnWorld loc = WSConfig.getWorldSpawn(p.getWorld().getName());
			if(loc != null) {
				p.teleport(loc.getLocation());
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent evt){
		Boolean respawn = WSConfig.isSpawnOnRespawn();
		SpawnWorld respawnLocation = WSConfig.getWorldSpawn(evt.getPlayer().getLocation().getWorld().getName());

		if(respawnLocation == null){ // No spawn is available in the current world and no hub location defined
			return;
		}

		if(respawnLocation.isRespawn() != null){ // Respawn override?
			respawn = respawnLocation.isRespawn();
		}

		if(respawnLocation != null && respawn) { // If the player gets relocated when respawning
			evt.setRespawnLocation(respawnLocation.getLocation());
		}
	}
	
}

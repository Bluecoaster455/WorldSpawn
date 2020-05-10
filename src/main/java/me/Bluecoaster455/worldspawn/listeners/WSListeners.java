package me.bluecoaster455.worldspawn.listeners;

import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.SpawnWorld;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class WSListeners implements Listener{
	
	@EventHandler(priority=EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent evt){
		Player p = evt.getPlayer();
		if(WSConfig.isHubOnJoin()){
			SpawnWorld hub = WSConfig.getHub();
			if(hub != null && hub.worldExists()) {
				p.teleport(hub.getLocation());
			}
		}
		else if(WSConfig.isSpawnOnJoin()){
			SpawnWorld spawn = WSConfig.getWorldSpawn(p.getWorld().getName());
			if(spawn != null && spawn.worldExists()) {
				p.teleport(spawn.getLocation());
			}
		}
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent evt){
		Boolean respawn = WSConfig.isSpawnOnRespawn();
		SpawnWorld respawnLocation = WSConfig.getWorldSpawn(evt.getRespawnLocation().getWorld().getName());

		if(respawnLocation == null){ // No spawn is available in the current world and no hub location defined
			return;
		}

		if(respawnLocation.isRespawn() != null){ // Respawn override?
			respawn = respawnLocation.isRespawn();
		}

		if(respawnLocation != null && (respawn || (!respawn && !evt.isBedSpawn()))) { // If the player gets relocated when respawning
			evt.setRespawnLocation(respawnLocation.getLocation());
		}
	}
	
}

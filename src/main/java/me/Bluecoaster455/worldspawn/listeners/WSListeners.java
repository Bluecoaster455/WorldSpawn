package me.bluecoaster455.worldspawn.listeners;

import java.util.HashMap;
import java.util.UUID;

import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.SpawnWorld;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class WSListeners implements Listener{
	
	private HashMap<UUID, Location> gLastDeathLocation = new HashMap<>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt){
		Player p = evt.getPlayer();
		if(WSConfig.isHubOnJoin()){
			Location loc = WSConfig.getHub();
			if(loc != null) {
				p.teleport(loc);
			}
			return;
		}
		if(WSConfig.isSpawnOnJoin()){
			SpawnWorld loc = WSConfig.getWorldSpawn(p.getWorld().getName());
			if(loc != null) {
				p.teleport(loc.getLocation());
			}
			return;
		}
	}
	
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent evt){
		Player p = (Player)evt.getEntity();
		gLastDeathLocation.put(p.getUniqueId(), p.getLocation());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent evt){
		Boolean respawn = WSConfig.isSpawnOnRespawn();

		Player p = evt.getPlayer();
		Location deathlocation = p.getLocation();
		SpawnWorld respawnLocation = WSConfig.getWorldSpawn(deathlocation.getWorld().getName());

		if(respawnLocation.isRespawn() != null){
			respawn = respawnLocation.isRespawn();
		}

		if(respawnLocation != null && respawn) {
			evt.setRespawnLocation(respawnLocation.getLocation());
		}
	}
	
}

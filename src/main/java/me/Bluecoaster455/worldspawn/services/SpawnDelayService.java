package me.bluecoaster455.worldspawn.services;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.bluecoaster455.worldspawn.WorldSpawn;
import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.SpawnWorld;

public class SpawnDelayService implements Listener {
	
	//Player uuid : delayer id
	private HashMap<UUID, Integer> gTeleporting;
	
	public SpawnDelayService() {
		gTeleporting = new HashMap<>();
	}
	
	public void cancelTeleport(Player pPlayer) {
		UUID uuid = pPlayer.getUniqueId();
		
		if(gTeleporting.containsKey(uuid)) {
			int id = gTeleporting.get(uuid);
			Bukkit.getScheduler().cancelTask(id);
			pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("spawning-cancelled"));
			gTeleporting.remove(uuid);
		}
		
	}
	
	public void teleport(Player pPlayer) {
		Location loc = pPlayer.getLocation();
		SpawnWorld spawn = WSConfig.getWorldSpawn(loc.getWorld().getName());

		if(spawn == null || !spawn.worldExists()){
			return;
		}
		
		pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("spawning-message").replace("%w", spawn.getLocation().getWorld().getName()));
		pPlayer.teleport(spawn.getLocation());
		
		if(gTeleporting.containsKey(pPlayer.getUniqueId())) {
			gTeleporting.remove(pPlayer.getUniqueId());
		}
	}
	
	public void teleportHub(Player pPlayer) {
		SpawnWorld hub = WSConfig.getHub();

		if(hub == null || !hub.worldExists()){
			return;
		}
		
		pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("hub-spawning"));
		pPlayer.teleport(hub.getLocation());
		
		if(gTeleporting.containsKey(pPlayer.getUniqueId())) {
			gTeleporting.remove(pPlayer.getUniqueId());
		}
	}
	
	public void delayTeleport(Player pPlayer, int pTime) {
		UUID uuid = pPlayer.getUniqueId();

		Location loc = pPlayer.getLocation();
		SpawnWorld spawn = WSConfig.getWorldSpawn(loc.getWorld().getName());
		
		if(gTeleporting.containsKey(uuid)) {
			int id = gTeleporting.get(uuid);
			Bukkit.getScheduler().cancelTask(id);
		}
		
		if(pPlayer.hasPermission("worldspawn.bypass.delay")) {
			teleport(pPlayer);
			return;
		}
		
		int pid = Bukkit.getScheduler().scheduleSyncDelayedTask(WorldSpawn.getPlugin(), () -> {
			teleport(pPlayer);
		}, pTime*20);

		if(pTime > 0){
			pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("spawning-delay-message").replace("%t", pTime+"").replace("%w", spawn.getLocation().getWorld().getName()));
		}

		gTeleporting.put(uuid, pid);
	}

	public void delayTeleportHub(Player pPlayer, int pTime){
		UUID uuid = pPlayer.getUniqueId();

		if(gTeleporting.containsKey(uuid)) {
			int id = gTeleporting.get(uuid);
			Bukkit.getScheduler().cancelTask(id);
		}
		
		if(pPlayer.hasPermission("worldspawn.bypass.delay")) {
			teleportHub(pPlayer);
			return;
		}

		int pid = Bukkit.getScheduler().scheduleSyncDelayedTask(WorldSpawn.getPlugin(), () -> {
			teleportHub(pPlayer);
		}, pTime*20);

		if(pTime > 0){
			pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("spawning-hub-delay-message").replace("%t", pTime+""));
		}
		
		gTeleporting.put(uuid, pid);
	}
	
	@EventHandler
	public void onPlayerHit(EntityDamageEvent evt) {
		if(evt.getEntity() instanceof Player) {
			Player p = (Player)evt.getEntity();
			cancelTeleport(p);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent evt) {
		Player p = evt.getPlayer();
		if(gTeleporting.containsKey(p.getUniqueId())) {
			cancelTeleport(p);
		}
	}
	
}

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
import me.bluecoaster455.worldspawn.models.Hub;
import me.bluecoaster455.worldspawn.models.Permissions;
import me.bluecoaster455.worldspawn.models.Spawn;

public class SpawnDelayService implements Listener {
	
	//Player uuid : delayer id
	private static HashMap<UUID, Integer> gTeleporting = new HashMap<>();
	private static HashMap<UUID, Location> gBlockTeleporting = new HashMap<>();
	
	public static void cancelTeleport(Player pPlayer) {
		UUID uuid = pPlayer.getUniqueId();
		
		if(gTeleporting.containsKey(uuid)) {
			int id = gTeleporting.get(uuid);
			Bukkit.getScheduler().cancelTask(id);
			pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("spawning-cancelled"));
			gTeleporting.remove(uuid);
			gBlockTeleporting.remove(uuid);
		}
	}
	
	public static void teleport(Player pPlayer) {
		Location loc = pPlayer.getLocation();
		Spawn spawn = WorldSpawnService.getSpawn(loc.getWorld().getName(), true);

		if(spawn == null || !spawn.worldExists()){
			return;
		}
		
		pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("spawning-message").replace("%w", spawn.getLocation().getWorld().getName()));
		pPlayer.teleport(spawn.getLocation());
		
		if(gTeleporting.containsKey(pPlayer.getUniqueId())) {
			gTeleporting.remove(pPlayer.getUniqueId());
		}
		
		if(gBlockTeleporting.containsKey(pPlayer.getUniqueId())) {
			gBlockTeleporting.remove(pPlayer.getUniqueId());
		}
	}
	
	public static void teleportHub(Player pPlayer) {
		Hub hub = WorldSpawnService.getHub();

		if(hub == null || !hub.worldExists()){
			return;
		}
		
		pPlayer.sendMessage(WSConfig.getMainPrefix() + WSConfig.getMessage("hub-spawning"));
		pPlayer.teleport(hub.getLocation());
		
		if(gTeleporting.containsKey(pPlayer.getUniqueId())) {
			gTeleporting.remove(pPlayer.getUniqueId());
		}
		
		if(gBlockTeleporting.containsKey(pPlayer.getUniqueId())) {
			gBlockTeleporting.remove(pPlayer.getUniqueId());
		}
	}
	
	public static void delayTeleport(Player pPlayer) {
		int time = WSConfig.getSpawnDelayTime();
		UUID uuid = pPlayer.getUniqueId();

		Location loc = pPlayer.getLocation();
		Spawn spawn = WorldSpawnService.getSpawn(loc.getWorld().getName(), true);
		
		if(gTeleporting.containsKey(uuid)) {
			int id = gTeleporting.get(uuid);
			Bukkit.getScheduler().cancelTask(id);
		}
		
		if(Permissions.hasPermission(pPlayer, Permissions.BYPASS_DELAY)) {
			teleport(pPlayer);
		} else {
			int pid = Bukkit.getScheduler().scheduleSyncDelayedTask(WorldSpawn.getPlugin(), () -> {
				teleport(pPlayer);
			}, time*20);
	
			if(time > 0){
				pPlayer.sendMessage(WSConfig.getMainPrefix() + WSConfig.getMessage("spawning-delay-message").replace("%t", time+"").replace("%w", spawn.getLocation().getWorld().getName()));
			}
	
			gTeleporting.put(uuid, pid);
			gBlockTeleporting.put(uuid, pPlayer.getLocation().getBlock().getLocation());
		}
	}

	public static void delayTeleportHub(Player pPlayer){
		int time = WSConfig.getHubDelayTime();
		UUID uuid = pPlayer.getUniqueId();

		if(gTeleporting.containsKey(uuid)) {
			int id = gTeleporting.get(uuid);
			Bukkit.getScheduler().cancelTask(id);
		}
		
		if(Permissions.hasPermission(pPlayer, Permissions.BYPASS_DELAY)) {
			teleportHub(pPlayer);
			return;
		}

		int pid = Bukkit.getScheduler().scheduleSyncDelayedTask(WorldSpawn.getPlugin(), () -> {
			teleportHub(pPlayer);
		}, time * 20);

		if(time > 0){
			pPlayer.sendMessage(WSConfig.getMainPrefix() + WSConfig.getMessage("spawning-hub-delay-message").replace("%t", time+""));
		}
		
		gTeleporting.put(uuid, pid);
		gBlockTeleporting.put(uuid, pPlayer.getLocation().getBlock().getLocation());
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
		if(gTeleporting.containsKey(p.getUniqueId()) && gBlockTeleporting.containsKey(p.getUniqueId())) {
			Location teleportBlock = gBlockTeleporting.get(p.getUniqueId());
			Location playerBlockLocation = p.getLocation().getBlock().getLocation();
			if(!teleportBlock.equals(playerBlockLocation)){
				cancelTeleport(p);
			}
		}
	}
	
}

package me.Bluecoaster455.worldspawn.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Bluecoaster455.worldspawn.WorldSpawn;
import me.Bluecoaster455.worldspawn.config.WSConfig;

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
		}
		
	}
	
	public void teleport(Player pPlayer) {
		Location loc = pPlayer.getLocation();
		Location spawn = WSConfig.getWorldSpawn(loc.getWorld().getName());
		
		pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("spawning-message").replace("%w", spawn.getWorld().getName()));
		pPlayer.teleport(spawn);
		
		if(gTeleporting.containsKey(pPlayer.getUniqueId())) {
			gTeleporting.remove(pPlayer.getUniqueId());
		}
	}
	
	public void delayTeleport(Player pPlayer, int pTime) {
		UUID uuid = pPlayer.getUniqueId();

		Location loc = pPlayer.getLocation();
		Location spawn = WSConfig.getWorldSpawn(loc.getWorld().getName());
		
		if(gTeleporting.containsKey(uuid)) {
			int id = gTeleporting.get(uuid);
			Bukkit.getScheduler().cancelTask(id);
		}
		
		if(pPlayer.hasPermission("worldspawn.bypass.delay")) {
			teleport(pPlayer);
			return;
		}
		
		int pid = Bukkit.getScheduler().scheduleSyncDelayedTask(WorldSpawn.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				teleport(pPlayer);
			}
		}, pTime*20);

		pPlayer.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("spawning-delay-message").replace("%t", pTime+"").replace("%w", spawn.getWorld().getName()));
		
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

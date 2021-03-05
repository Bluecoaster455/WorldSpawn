package me.bluecoaster455.worldspawn.services;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.bluecoaster455.worldspawn.WorldSpawn;
import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.Hub;
import me.bluecoaster455.worldspawn.models.Spawn;

public class WorldSpawnService implements Listener {
  
	private static HashMap<String, Spawn> gWorldSpawns = new HashMap<>();
	private static Hub gHubLocation;
  
  /**
   * Get the world spawn location by the name of the world
   * @param worldName World name
   * @return Location of the world spawn.
   */
  public static Location getWorldSpawnLocation(String worldName) {
    return spawnExists(worldName) ? getSpawn(worldName, true).getLocation() : null;
  }

  /**
   * Get the World Spawn location by world instance.
   * @param world Bukkit world instance
   * @return Location of the world spawn.
   */
  public static Location getWorldSpawnLocation(World world) {
    return getWorldSpawnLocation(world.getName());
  }

  /**
   * Get hub.
   * @return
   */
  public static Hub getHub() {
    return gHubLocation != null && gHubLocation.worldExists() ? gHubLocation : null;
  }
	
  /**
   * Sets the hub location
   * @param location Location
   */
	public static void setHub(Location location){
    Hub hub = getHub();
    if(hub == null){
      hub = new Hub(location);
    } else {
      hub.setLocation(location);
    }
    hub.save();
	}
	
  /**
   * Sets the hub
   * @param hub Hub instance
   */
	public static void setHub(Hub hub){
    gHubLocation = hub;
    hub.save();
	}
	
  /**
   * Checks if the spawn exists.
   * @param pSpawnLocationName Name of the spawn.
   * @return
   */
	public static boolean spawnExists(String pSpawnLocationName){
		return gWorldSpawns.containsKey(pSpawnLocationName);
	}
	
  /**
   * Establish a link between two worlds so that the world links to the spawn location.
   * @param pWorldName World name to link
   * @param pSpawnLocationName Name of the spawn to link the world.
   * @return
   */
	public static boolean setSpawnLink(String pWorldName, String pSpawnLocationName){
		Spawn loc = getSpawn(pSpawnLocationName);
    if(loc == null) return false;
		setSpawn(pWorldName, loc.getLocation(), loc.isRespawn());
		return true;
	}

  /**
   * Get spawn by name.
   * @param pSpawnLocationName Name of the spawn
   * @param deep Find the actual spawn if the world is linked to an other world.
   * @return Spawn or null if not exists.
   */
	public static Spawn getSpawn(String pSpawnLocationName, boolean deep) {
    
		if(spawnExists(pSpawnLocationName)){
			Spawn spawn = gWorldSpawns.get(pSpawnLocationName);
      if(spawn.worldExists()) {
        Spawn finalSpawn = spawn;
        if(deep && !pSpawnLocationName.equals(spawn.getLocation().getWorld().getName())) {
          Spawn topSpawn = getSpawn(spawn.getLocation().getWorld().getName(), true);
          if(topSpawn != null){
            finalSpawn = topSpawn;
          }
        }
        return finalSpawn;
      }
		}

		return null;
	}

  /**
   * Get spawn by name.
   * @param pSpawnLocationName Name of the spawn
   * @return Spawn or null if not exists.
   */
	public static Spawn getSpawn(String pSpawnLocationName) {
    return getSpawn(pSpawnLocationName, false);
  }
	
  /**
   * Sets a spawn. Will create if not exists or update if exists.
   * @param pSpawnLocationName Name of the spawn.
   * @param pLocation Location of the spawn.
   * @param pRespawn If respawn is enabled.
   */
	public static void setSpawn(String pSpawnLocationName, Location pLocation, Boolean pRespawn) {
		Spawn spawn = getSpawn(pSpawnLocationName);
		if(spawn == null) {
			spawn = new Spawn(pSpawnLocationName, pLocation.getWorld().getName(), pLocation.toVector(), pLocation.getYaw(), pLocation.getPitch(), pRespawn);
			gWorldSpawns.put(pSpawnLocationName, spawn);
		} else {
			spawn.setLocation(pLocation);
			spawn.setSpawnOnRespawn(pRespawn);
		}
		spawn.save();
	}
	
  /**
   * Sets a spawn.
   * @param spawn Spawn instance
   */
	public static void setSpawn(String name, Spawn spawn){
    gWorldSpawns.put(name, spawn);
	}
	
  /**
   * Deletes a spawn.
   * @param pSpawnLocationName Name of the spawn.
   * @return If the spawn is deleted successfully.
   */
	public static boolean deleteSpawn(String pSpawnLocationName){
		if(!spawnExists(pSpawnLocationName)){
			return false;
		}
		gWorldSpawns.remove(pSpawnLocationName);
		WorldSpawn.getPlugin().getConfig().set("spawns."+pSpawnLocationName, null);
		WorldSpawn.getPlugin().saveConfig();
		return true;
	}

  /**
   * Set spawn on respawn.
   * @param pSpawnLocationName Name of the spawn.
   * @param respawn If the players will spawn after respawning.
   */
	public static boolean setSpawnOnRespawn(String pSpawnLocationName, Boolean respawn) {
		Spawn spawn = getSpawn(pSpawnLocationName, true);
    if(spawn != null){
      spawn.setSpawnOnRespawn(respawn);
      spawn.save();
      return true;
    }
    return false;
	}
  
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent evt){
		Player p = evt.getPlayer();
		if(WSConfig.isHubOnJoin()){
			Hub hub = WorldSpawnService.getHub();
			if(hub != null && hub.worldExists()) {
				p.teleport(hub.getLocation());
			}
		}
		else if(WSConfig.isSpawnOnJoin()){
			Spawn spawn = WorldSpawnService.getSpawn(p.getWorld().getName(), true);
			if(spawn != null && spawn.worldExists()) {
				p.teleport(spawn.getLocation());
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent evt){
		Spawn respawnLocation = WorldSpawnService.getSpawn(evt.getRespawnLocation().getWorld().getName(), true);
		if(respawnLocation == null){ // No spawn is available in the current world and no hub location defined
			return;
		}
		Boolean respawn = respawnLocation.isRespawn();
		if(respawnLocation != null && (respawn || (!respawn && !evt.isBedSpawn() && evt.getRespawnLocation().getWorld().getEnvironment() != Environment.NETHER))) { // If the player gets relocated when respawning
			evt.setRespawnLocation(respawnLocation.getLocation());
		}
	}

}

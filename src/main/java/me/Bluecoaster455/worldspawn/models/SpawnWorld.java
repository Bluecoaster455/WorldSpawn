package me.bluecoaster455.worldspawn.models;

import org.bukkit.Location;

import me.bluecoaster455.worldspawn.config.WSConfig;

public class SpawnWorld {
    private Location location;
    private Boolean respawn;

    public SpawnWorld(Location location, Boolean respawn){
        this.location = location;
        this.respawn = respawn;
    }

    public Location getLocation(){
        return location;
    }

    public Boolean isRespawn(){
        return respawn == null ? WSConfig.isSpawnOnRespawn() : respawn;
    }

}
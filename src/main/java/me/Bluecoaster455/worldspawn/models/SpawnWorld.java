package me.bluecoaster455.worldspawn.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import me.bluecoaster455.worldspawn.config.WSConfig;

public class SpawnWorld {
    private String worldname;
    private Vector position;
    private float yaw;
    private float pitch;
    private Boolean respawn;

    public SpawnWorld(String worldname, Vector position, float yaw, float pitch, Boolean respawn){
        this.worldname = worldname;
        this.position = position;
        this.respawn = respawn;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SpawnWorld(String worldname, double x, double y, double z, float yaw, float pitch, Boolean respawn){
        this.worldname = worldname;
        this.position = new Vector(x,y,z);
        this.respawn = respawn;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location getLocation(){
        World world = Bukkit.getWorld(worldname);
        return new Location(world, position.getX(), position.getY(), position.getZ(), yaw, pitch);
    }

    public boolean worldExists(){
        World world = Bukkit.getWorld(worldname);
        return world != null;
    }

    public Boolean isRespawn(){
        return respawn == null ? WSConfig.isSpawnOnRespawn() : respawn;
    }

}
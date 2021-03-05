package me.bluecoaster455.worldspawn.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import me.bluecoaster455.worldspawn.WorldSpawn;
import me.bluecoaster455.worldspawn.config.WSConfig;

public class Spawn {
    private String spawnname;
    private String worldname;
    private Vector position;
    private float yaw;
    private float pitch;
    private Boolean respawn;

    public Spawn(String spawnname, String worldname, Vector position, float yaw, float pitch, Boolean respawn){
        this.spawnname = spawnname;
        this.worldname = worldname;
        this.position = position;
        this.respawn = respawn;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Spawn(String spawnname, String worldname, double x, double y, double z, float yaw, float pitch, Boolean respawn){
        this.spawnname = spawnname;
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

    public void setLocation(Location location) {
        this.position = location.toVector();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public boolean worldExists(){
        World world = Bukkit.getWorld(worldname);
        return world != null;
    }

    public boolean isRespawn(){
        return respawn == null ? WSConfig.isSpawnOnRespawn() : respawn;
    }

    public void setSpawnOnRespawn(Boolean respawn) {
        this.respawn = respawn == null ? true : respawn;
    }

    public void save() {
		FileConfiguration conf = WorldSpawn.getPlugin().getConfig();
		conf.set("spawns."+this.spawnname+".spawn-on-respawn", this.respawn);
		conf.set("spawns."+this.spawnname+".world", this.worldname);
		conf.set("spawns."+this.spawnname+".x", this.position.getX());
		conf.set("spawns."+this.spawnname+".y", this.position.getY());
		conf.set("spawns."+this.spawnname+".z", this.position.getZ());
		conf.set("spawns."+this.spawnname+".yaw", this.yaw);
		conf.set("spawns."+this.spawnname+".pitch", this.pitch);
		WorldSpawn.getPlugin().saveConfig();
    }

}
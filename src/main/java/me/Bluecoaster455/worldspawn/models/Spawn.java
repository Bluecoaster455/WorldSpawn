package me.bluecoaster455.worldspawn.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import me.bluecoaster455.worldspawn.WorldSpawn;
import me.bluecoaster455.worldspawn.config.WSConfig;

public class Spawn {
    private String worldname;
    private Vector position;
    private float yaw;
    private float pitch;
    private Boolean respawn;

    public Spawn(String worldname, Vector position, float yaw, float pitch, Boolean respawn){
        this.worldname = worldname;
        this.position = position;
        this.respawn = respawn;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Spawn(String worldname, double x, double y, double z, float yaw, float pitch, Boolean respawn){
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

    public void setSpawnOnRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    public void save() {
		FileConfiguration conf = WorldSpawn.getPlugin().getConfig();
		conf.set("spawns."+this.worldname+".spawn-on-respawn", this.respawn);
		conf.set("spawns."+this.worldname+".world", this.worldname);
		conf.set("spawns."+this.worldname+".x", this.position.getX());
		conf.set("spawns."+this.worldname+".y", this.position.getY());
		conf.set("spawns."+this.worldname+".z", this.position.getZ());
		conf.set("spawns."+this.worldname+".yaw", this.yaw);
		conf.set("spawns."+this.worldname+".pitch", this.pitch);
		WorldSpawn.getPlugin().saveConfig();
    }

}
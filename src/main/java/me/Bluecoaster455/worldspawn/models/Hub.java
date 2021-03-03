package me.bluecoaster455.worldspawn.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import me.bluecoaster455.worldspawn.WorldSpawn;

public class Hub {
  
  public String worldname;
  public Vector position;
  public float yaw;
  public float pitch;

  public Hub(Location location) {
    setLocation(location);
  }

  public Hub(String worldname, double x, double y, double z, float yaw, float pitch) {
    this.worldname = worldname;
    this.position = new Vector(x, y, z);
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public void setLocation(Location location) {
    this.worldname = location.getWorld().getName();
    this.position = location.toVector();
    this.yaw = location.getYaw();
    this.pitch = location.getPitch();
  }

  public Location getLocation() {
    World world = Bukkit.getWorld(this.worldname);
    if(world == null){
      return null;
    }
    return new Location(world, this.position.getX(), this.position.getY(), this.position.getZ(), this.yaw, this.pitch);
  }

  public boolean worldExists() {
    World world = Bukkit.getWorld(this.worldname);
    return world != null;
  }

  public void save() {
		FileConfiguration conf = WorldSpawn.getPlugin().getConfig();
		conf.set("hub.world", this.worldname);
		conf.set("hub.x", this.position.getX());
		conf.set("hub.y", this.position.getY());
		conf.set("hub.z", this.position.getZ());
		conf.set("hub.yaw", this.yaw);
		conf.set("hub.pitch", this.pitch);
		WorldSpawn.getPlugin().saveConfig();
  }

}

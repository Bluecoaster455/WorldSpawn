package me.bluecoaster455.worldspawn.config;

import java.io.File;
import java.util.HashMap;

import me.bluecoaster455.worldspawn.WorldSpawn;
import me.bluecoaster455.worldspawn.commands.HubCommand;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WSConfig {
	
	private static HashMap<String, Location> gWorldSpawns;
	private static Location gHubLocation;

	private static boolean hub_enabled = true;
	private static boolean hub_on_join = false;
	private static boolean spawn_on_join = false;
	private static int spawn_delay = 0;
	private static String lang = "EN";
	
	private static HashMap<String, String> gMessages = new HashMap<>();
	
	public static void save(){
		WorldSpawn.getPlugin().saveConfig();
	}
	
	public static void reload(WorldSpawn plugin){
		plugin.saveDefaultConfig();
		
		String[] langs = new String[] {"EN","DE","ES","IT","RU","FR","HU"};

		File msgFolder = new File(plugin.getDataFolder(),"Messages");
		
		if(!msgFolder.exists()) {
			msgFolder.mkdir();
		}
		
		for(String l : langs) {
			if(!new File(plugin.getDataFolder(),"Messages"+File.separator+l+"_messages.yml").exists()) {
				plugin.saveResource("Messages"+File.separator+l+"_messages.yml", false);
			}
		}
		
		plugin.reloadConfig();

		hub_enabled = plugin.getConfig().getBoolean("hub-enabled");
		spawn_on_join = plugin.getConfig().getBoolean("spawn-on-join");
		hub_on_join = spawn_on_join ? false : plugin.getConfig().getBoolean("hub-on-join");
		spawn_delay = plugin.getConfig().getInt("spawn-delay");
		lang = plugin.getConfig().getString("language");
		
		gHubLocation = null;
		gWorldSpawns = new HashMap<>();
		
		World hubworld = Bukkit.getWorld(plugin.getConfig().getString("hub.world"));
		double hubx = plugin.getConfig().getDouble("hub.x");
		double huby = plugin.getConfig().getDouble("hub.y");
		double hubz = plugin.getConfig().getDouble("hub.z");
		float hubyaw = (float)plugin.getConfig().getDouble("hub.yaw");
		float hubpitch = (float)plugin.getConfig().getDouble("hub.pitch");
		
		gHubLocation = new Location(hubworld, hubx, huby, hubz, hubyaw, hubpitch);
		
		for(String aspawn : plugin.getConfig().getConfigurationSection("spawns").getKeys(false)){
			World spawnworld = Bukkit.getWorld(plugin.getConfig().getString("spawns."+aspawn+".world"));
			double spawnx = plugin.getConfig().getDouble("spawns."+aspawn+".x");
			double spawny = plugin.getConfig().getDouble("spawns."+aspawn+".y");
			double spawnz = plugin.getConfig().getDouble("spawns."+aspawn+".z");
			float spawnyaw = (float)plugin.getConfig().getDouble("spawns."+aspawn+".yaw");
			float spawnpitch = (float)plugin.getConfig().getDouble("spawns."+aspawn+".pitch");
			gWorldSpawns.put(aspawn, new Location(spawnworld, spawnx, spawny, spawnz, spawnyaw, spawnpitch));
		}
		reloadMessages();

		if(isHubEnabled()) {
			Bukkit.getPluginCommand("hub").setExecutor(new HubCommand());
		}
		else {
			Bukkit.getPluginCommand("hub").setExecutor(null);
		}
	}
	
	public static void reloadMessages() {
		
		File file = new File(WorldSpawn.getPlugin().getDataFolder(),"Messages"+File.separator+lang+"_messages.yml");
		if(!file.exists()) {
			file = new File(WorldSpawn.getPlugin().getDataFolder(),"Messages"+File.separator+"EN_messages.yml");
		}
		
		FileConfiguration fMessages = YamlConfiguration.loadConfiguration(file);
		gMessages.put("worldspawn-main-prefix", fMessages.getString("worldspawn-main-prefix"));
		gMessages.put("worldspawn-admin-prefix", fMessages.getString("worldspawn-admin-prefix"));
		gMessages.put("worldspawn-error-prefix", fMessages.getString("worldspawn-error-prefix"));
		gMessages.put("command-no-permission", fMessages.getString("command-no-permission"));
		gMessages.put("not-a-player-error", fMessages.getString("not-a-player-error"));
		gMessages.put("specified-world-not-exist", fMessages.getString("specified-world-not-exist"));
		gMessages.put("no-spawn-world", fMessages.getString("no-spawn-world"));
		gMessages.put("config-reload", fMessages.getString("config-reload"));
		gMessages.put("hub-spawning", fMessages.getString("hub-spawning"));
		gMessages.put("spawn-link-fail", fMessages.getString("spawn-link-fail"));
		gMessages.put("spawn-link-success", fMessages.getString("spawn-link-success"));
		gMessages.put("hub-set-success", fMessages.getString("hub-set-success"));
		gMessages.put("set-spawn-success", fMessages.getString("set-spawn-success"));
		gMessages.put("spawn-delete-success", fMessages.getString("spawn-delete-success"));
		gMessages.put("spawning-delay-message", fMessages.getString("spawning-delay-message"));
		gMessages.put("spawning-message", fMessages.getString("spawning-message"));
		gMessages.put("spawning-cancelled", fMessages.getString("spawning-cancelled"));
		gMessages.put("hub-not-exists", fMessages.getString("hub-not-exists"));
	}
	
	public static String getMainPrefix() {
		return gMessages.get("worldspawn-main-prefix").replace("&", "§");
	}
	
	public static String getErrorPrefix() {
		return gMessages.get("worldspawn-error-prefix").replace("&", "§");
	}
	
	public static String getAdminPrefix() {
		return gMessages.get("worldspawn-admin-prefix").replace("&", "§");
	}
	
	public static String getMessage(String pKey) {
		return gMessages.get(pKey).replace("&", "§");
	}
	
	public static boolean isSpawnOnJoin(){
		return spawn_on_join;
	}
	
	public static boolean isHubEnabled(){
		return hub_enabled;
	}
	
	public static boolean isHubOnJoin(){
		return hub_on_join;
	}
	
	public static boolean isSpawnDelayOn(){
		return spawn_delay > 0;
	}
	
	public static int getSpawnDelayTime() {
		return spawn_delay;
	}
	
	public static Location getHub(){
		return gHubLocation;
	}
	
	public static Location getWorldSpawn(String pWorldName){
		if(existsSpawn(pWorldName)){
			return gWorldSpawns.get(pWorldName);
		}
		return getHub();
	}
	
	public static void setHub(Location pLocation){
		FileConfiguration conf = WorldSpawn.getPlugin().getConfig();
		conf.set("hub.world", pLocation.getWorld().getName());
		conf.set("hub.x", pLocation.getX());
		conf.set("hub.y", pLocation.getY());
		conf.set("hub.z", pLocation.getZ());
		conf.set("hub.yaw", pLocation.getYaw());
		conf.set("hub.pitch", pLocation.getPitch());
		gHubLocation = pLocation;
		setSpawn(pLocation.getWorld().getName(), pLocation);
	}
	
	public static boolean existsSpawn(String pSpawnLocationName){
		return gWorldSpawns.containsKey(pSpawnLocationName);
	}
	
	public static int setSpawnLink(String pWorldName, String pSpawnLocationName){
		if(!existsSpawn(pSpawnLocationName)){
			return 0;
		}
		Location loc = getWorldSpawn(pSpawnLocationName);
		setSpawn(pWorldName, loc);
		return 1;
	}
	
	public static int deleteSpawn(String pSpawnLocationName){
		if(!existsSpawn(pSpawnLocationName)){
			return 0;
		}
		gWorldSpawns.remove(pSpawnLocationName);
		WorldSpawn.getPlugin().getConfig().set("spawns."+pSpawnLocationName, null);
		save();
		return 1;
	}
	
	public static void setSpawn(String pWorldName, Location pLocation){
		FileConfiguration conf = WorldSpawn.getPlugin().getConfig();
		conf.set("spawns."+pWorldName+".world", pLocation.getWorld().getName());
		conf.set("spawns."+pWorldName+".x", pLocation.getX());
		conf.set("spawns."+pWorldName+".y", pLocation.getY());
		conf.set("spawns."+pWorldName+".z", pLocation.getZ());
		conf.set("spawns."+pWorldName+".yaw", pLocation.getYaw());
		conf.set("spawns."+pWorldName+".pitch", pLocation.getPitch());
		gWorldSpawns.put(pWorldName, pLocation);
		save();
	}
	
}

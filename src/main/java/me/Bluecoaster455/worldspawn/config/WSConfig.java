package me.bluecoaster455.worldspawn.config;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import me.bluecoaster455.worldspawn.WorldSpawn;
import me.bluecoaster455.worldspawn.commands.HubCommand;
import me.bluecoaster455.worldspawn.models.SpawnWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WSConfig {

	private static HashMap<String, SpawnWorld> gWorldSpawns;
	private static SpawnWorld gHubLocation;

	private static boolean hub_enabled = true;
	private static boolean hub_on_join = false;
	private static boolean spawn_on_join = false;
	private static boolean spawn_on_respawn = false;
	private static int spawn_delay = 0;
	private static int hub_delay = 0;
	private static String lang = "EN";

	private static HashMap<String, String> gMessages = new HashMap<>();

	public static void save() {
		WorldSpawn.getPlugin().saveConfig();
	}

	public static void reload(WorldSpawn plugin) {
		plugin.saveDefaultConfig();

		String[] langs = new String[] { "EN", "DE", "ES", "IT", "RU", "FR", "HU" };

		File msgFolder = new File(plugin.getDataFolder(), "Messages");

		if (!msgFolder.exists()) {
			msgFolder.mkdir();
		}

		for (String l : langs) {
			if (!new File(plugin.getDataFolder(), "Messages" + File.separator + l + "_messages.yml").exists()) {
				plugin.saveResource("Messages" + File.separator + l + "_messages.yml", false);
			}
		}

		plugin.reloadConfig();

		hub_enabled = plugin.getConfig().getBoolean("hub-enabled", true);
		spawn_on_join = plugin.getConfig().getBoolean("spawn-on-join", false);
		spawn_on_respawn = plugin.getConfig().getBoolean("spawn-on-respawn", true);
		hub_on_join = spawn_on_join ? false : plugin.getConfig().getBoolean("hub-on-join", true);
		spawn_delay = plugin.getConfig().getInt("spawn-delay", 0);
		hub_delay = plugin.getConfig().getInt("hub-delay", 0);
		lang = plugin.getConfig().getString("language", "EN");

		reloadMessages(plugin);

		gHubLocation = null;
		gWorldSpawns = new HashMap<>();

		String hubworldname = plugin.getConfig().getString("hub.world", "world");
		double hubx = plugin.getConfig().getDouble("hub.x", 0.0);
		double huby = plugin.getConfig().getDouble("hub.y", 80.0);
		double hubz = plugin.getConfig().getDouble("hub.z", 0.0);
		float hubyaw = (float) plugin.getConfig().getDouble("hub.yaw", 90);
		float hubpitch = (float) plugin.getConfig().getDouble("hub.pitch", 0.0);

		gHubLocation = new SpawnWorld(hubworldname, hubx, huby, hubz, hubyaw, hubpitch, null);

		Bukkit.getConsoleSender().sendMessage(WSConfig.getMainPrefix()+"Hub loaded!");

		for (String aspawn : plugin.getConfig().getConfigurationSection("spawns").getKeys(false)) {
			String worldname = plugin.getConfig().getString("spawns." + aspawn + ".world");
			double spawnx = plugin.getConfig().getDouble("spawns." + aspawn + ".x");
			double spawny = plugin.getConfig().getDouble("spawns." + aspawn + ".y");
			double spawnz = plugin.getConfig().getDouble("spawns." + aspawn + ".z");
			float spawnyaw = (float) plugin.getConfig().getDouble("spawns." + aspawn + ".yaw");
			float spawnpitch = (float) plugin.getConfig().getDouble("spawns." + aspawn + ".pitch");
			
			Boolean respawn = null;
			if(plugin.getConfig().isSet("spawns." + aspawn + ".spawn-on-respawn")){
				respawn = plugin.getConfig().getBoolean("spawns." + aspawn + ".spawn-on-respawn");
			}

			Bukkit.getConsoleSender().sendMessage(WSConfig.getMainPrefix()+"Spawn for \""+worldname+"\" loaded!");

			SpawnWorld spawn = new SpawnWorld(worldname, spawnx, spawny, spawnz, spawnyaw, spawnpitch, respawn);
			gWorldSpawns.put(aspawn, spawn);
		}

		if (isHubEnabled()) {
			Bukkit.getPluginCommand("hub").setExecutor(new HubCommand());
		} else {
			Bukkit.getPluginCommand("hub").setExecutor(null);
		}
	}

	public static void reloadMessages(WorldSpawn plugin) {

		String filename = "Messages/" + lang + "_messages.yml";
		File file = new File(WorldSpawn.getPlugin().getDataFolder(), filename);
		if (!file.exists()) {
			filename = "Messages/EN_messages.yml";
			file = new File(WorldSpawn.getPlugin().getDataFolder(), filename);
		}

		YamlConfiguration dMessages = null;
		FileConfiguration fMessages = YamlConfiguration.loadConfiguration(file);
		try {
			InputStream stream = plugin.getResource(filename);
			Reader defConfigStream = new InputStreamReader(stream, "UTF8");
			if (defConfigStream != null) {
				dMessages = YamlConfiguration.loadConfiguration(defConfigStream);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		gMessages.put("worldspawn-main-prefix", fMessages.getString("worldspawn-main-prefix", dMessages.getString("worldspawn-main-prefix")));
		gMessages.put("worldspawn-admin-prefix", fMessages.getString("worldspawn-admin-prefix", dMessages.getString("worldspawn-admin-prefix")));
		gMessages.put("worldspawn-error-prefix", fMessages.getString("worldspawn-error-prefix", dMessages.getString("worldspawn-error-prefix")));
		gMessages.put("command-no-permission", fMessages.getString("command-no-permission", dMessages.getString("command-no-permission")));
		gMessages.put("not-a-player-error", fMessages.getString("not-a-player-error", dMessages.getString("not-a-player-error")));
		gMessages.put("specified-world-not-exist", fMessages.getString("specified-world-not-exist", dMessages.getString("specified-world-not-exist")));
		gMessages.put("no-spawn-world", fMessages.getString("no-spawn-world", dMessages.getString("no-spawn-world")));
		gMessages.put("config-reload", fMessages.getString("config-reload", dMessages.getString("config-reload")));
		gMessages.put("hub-spawning", fMessages.getString("hub-spawning", dMessages.getString("hub-spawning")));
		gMessages.put("spawn-link-fail", fMessages.getString("spawn-link-fail", dMessages.getString("spawn-link-fail")));
		gMessages.put("spawn-link-success", fMessages.getString("spawn-link-success", dMessages.getString("spawn-link-success")));
		gMessages.put("hub-set-success", fMessages.getString("hub-set-success", dMessages.getString("hub-set-success")));
		gMessages.put("set-spawn-success", fMessages.getString("set-spawn-success", dMessages.getString("set-spawn-success")));
		gMessages.put("spawn-delete-success", fMessages.getString("spawn-delete-success", dMessages.getString("spawn-delete-success")));
		gMessages.put("spawning-delay-message", fMessages.getString("spawning-delay-message", dMessages.getString("spawning-delay-message")));
		gMessages.put("spawning-hub-delay-message", fMessages.getString("spawning-hub-delay-message", dMessages.getString("spawning-hub-delay-message")));
		gMessages.put("spawning-message", fMessages.getString("spawning-message", dMessages.getString("spawning-message")));
		gMessages.put("spawning-cancelled", fMessages.getString("spawning-cancelled", dMessages.getString("spawning-cancelled")));
		gMessages.put("hub-not-exists", fMessages.getString("hub-not-exists", dMessages.getString("hub-not-exists")));
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
	
	public static boolean isSpawnOnRespawn(){
		return spawn_on_respawn;
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
	
	public static boolean isHubDelayOn(){
		return hub_delay > 0;
	}
	
	public static int getHubDelayTime() {
		return hub_delay;
	}
	
	public static SpawnWorld getHub(){
		return gHubLocation;
	}
	
	public static SpawnWorld getWorldSpawn(String pWorldName){
		if(existsSpawn(pWorldName)){
			SpawnWorld spawn = gWorldSpawns.get(pWorldName);
			return spawn.getLocation().getWorld() != null ? spawn : null;
		}

		if(gHubLocation != null){
			if(gHubLocation.worldExists())
				return gHubLocation;
			else
				return null;
		}

		return null;
	}
	
	public static void setHub(Location pLocation){
		FileConfiguration conf = WorldSpawn.getPlugin().getConfig();
		conf.set("hub.world", pLocation.getWorld().getName());
		conf.set("hub.x", pLocation.getX());
		conf.set("hub.y", pLocation.getY());
		conf.set("hub.z", pLocation.getZ());
		conf.set("hub.yaw", pLocation.getYaw());
		conf.set("hub.pitch", pLocation.getPitch());
		gHubLocation = new SpawnWorld(pLocation.getWorld().getName(), pLocation.toVector(), pLocation.getYaw(), pLocation.getPitch(), null);
		setSpawn(pLocation.getWorld().getName(), pLocation, null);
	}
	
	public static boolean existsSpawn(String pSpawnLocationName){
		return gWorldSpawns.containsKey(pSpawnLocationName);
	}
	
	public static int setSpawnLink(String pWorldName, String pSpawnLocationName){
		if(!existsSpawn(pSpawnLocationName)){
			return 0;
		}
		SpawnWorld loc = getWorldSpawn(pSpawnLocationName);
		setSpawn(pWorldName, loc.getLocation(), loc.isRespawn());
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
	
	public static void setSpawn(String pWorldName, Location pLocation, Boolean pRespawn){
		FileConfiguration conf = WorldSpawn.getPlugin().getConfig();
		conf.set("spawns."+pWorldName+".spawn-on-respawn", pRespawn);
		conf.set("spawns."+pWorldName+".world", pLocation.getWorld().getName());
		conf.set("spawns."+pWorldName+".x", pLocation.getX());
		conf.set("spawns."+pWorldName+".y", pLocation.getY());
		conf.set("spawns."+pWorldName+".z", pLocation.getZ());
		conf.set("spawns."+pWorldName+".yaw", pLocation.getYaw());
		conf.set("spawns."+pWorldName+".pitch", pLocation.getPitch());
		SpawnWorld spawn = new SpawnWorld(pLocation.getWorld().getName(), pLocation.toVector(), pLocation.getYaw(), pLocation.getPitch(), pRespawn);
		gWorldSpawns.put(pWorldName, spawn);
		save();
	}
	
}

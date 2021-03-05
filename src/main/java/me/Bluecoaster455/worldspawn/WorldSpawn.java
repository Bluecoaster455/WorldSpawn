package me.bluecoaster455.worldspawn;

import me.bluecoaster455.worldspawn.commands.DelSpawnCommand;
import me.bluecoaster455.worldspawn.commands.HubCommand;
import me.bluecoaster455.worldspawn.commands.LinkSpawnCommand;
import me.bluecoaster455.worldspawn.commands.SetHubCommand;
import me.bluecoaster455.worldspawn.commands.SetSpawnCommand;
import me.bluecoaster455.worldspawn.commands.SpawnCommand;
import me.bluecoaster455.worldspawn.commands.SpawnOnRespawnCommand;
import me.bluecoaster455.worldspawn.commands.WorldSpawnCommand;
import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.services.SpawnDelayService;
import me.bluecoaster455.worldspawn.services.WorldSpawnService;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class WorldSpawn extends JavaPlugin {
	
	private static WorldSpawn gPlugin;
	
	private static SpawnDelayService gSDSvc;
	
	public static SpawnDelayService getSpawnDelaySvc() {
		return gSDSvc;
	}
	
	public static WorldSpawn getPlugin(){
		return gPlugin;
	}
	
	@Override
	public void onEnable() {
		gPlugin = this;
		new Metrics(this, 6585);
		
		
		Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand());
		Bukkit.getPluginCommand("setspawn").setExecutor(new SetSpawnCommand());
		Bukkit.getPluginCommand("sethub").setExecutor(new SetHubCommand());
		Bukkit.getPluginCommand("hub").setExecutor(new HubCommand());
		Bukkit.getPluginCommand("linkspawn").setExecutor(new LinkSpawnCommand());
		Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand());
		Bukkit.getPluginCommand("delspawn").setExecutor(new DelSpawnCommand());
		Bukkit.getPluginCommand("worldspawn").setExecutor(new WorldSpawnCommand());
		Bukkit.getPluginCommand("spawnonrespawn").setExecutor(new SpawnOnRespawnCommand());
		
		Bukkit.getPluginManager().registerEvents(new SpawnDelayService(), this);
		Bukkit.getPluginManager().registerEvents(new WorldSpawnService(), this);
		
		WSConfig.reload(WorldSpawn.getPlugin());
		
		getLogger().info("Hello server!");
		getLogger().info("World Spawn by Bluecoaster455 v"+getDescription().getVersion());
	}
	
}

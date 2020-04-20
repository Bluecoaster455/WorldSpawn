package me.bluecoaster455.worldspawn;

import me.bluecoaster455.worldspawn.commands.DelSpawnCommand;
import me.bluecoaster455.worldspawn.commands.HubCommand;
import me.bluecoaster455.worldspawn.commands.LinkSpawnCommand;
import me.bluecoaster455.worldspawn.commands.SetHubCommand;
import me.bluecoaster455.worldspawn.commands.SetSpawnCommand;
import me.bluecoaster455.worldspawn.commands.SpawnCommand;
import me.bluecoaster455.worldspawn.commands.WorldSpawnCommand;
import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.listeners.WSListeners;
import me.bluecoaster455.worldspawn.services.SpawnDelayService;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class WorldSpawn extends JavaPlugin implements Listener{
	
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
		
		gSDSvc = new SpawnDelayService();
		
		Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand());
		Bukkit.getPluginCommand("setspawn").setExecutor(new SetSpawnCommand());
		Bukkit.getPluginCommand("sethub").setExecutor(new SetHubCommand());
		Bukkit.getPluginCommand("hub").setExecutor(new HubCommand());
		Bukkit.getPluginCommand("linkspawn").setExecutor(new LinkSpawnCommand());
		Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand());
		Bukkit.getPluginCommand("delspawn").setExecutor(new DelSpawnCommand());
		Bukkit.getPluginCommand("worldspawn").setExecutor(new WorldSpawnCommand());
		
		Bukkit.getPluginManager().registerEvents(new WSListeners(), this);
		Bukkit.getPluginManager().registerEvents(this, this);
		WSConfig.reload(WorldSpawn.getPlugin());
		
		Bukkit.getPluginManager().registerEvents(gSDSvc, this);
		
		getLogger().info("Hello server!");
		getLogger().info("World Spawn by Bluecoaster455 v"+getDescription().getVersion());
		
	}
	
	@EventHandler
	public void onWorldLoaded(WorldLoadEvent evt){
		WSConfig.reload(WorldSpawn.getPlugin());
	}
	
}

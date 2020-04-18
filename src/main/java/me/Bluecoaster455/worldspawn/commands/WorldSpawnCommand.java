package me.Bluecoaster455.worldspawn.commands;

import me.Bluecoaster455.worldspawn.WorldSpawn;
import me.Bluecoaster455.worldspawn.config.WSConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WorldSpawnCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("worldspawn.admin")){
			WSConfig.reload(WorldSpawn.getPlugin());
			sender.sendMessage(WSConfig.getAdminPrefix()+WSConfig.getMessage("config-reload"));
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("command-no-permission"));
		}
		return true;
	}

}
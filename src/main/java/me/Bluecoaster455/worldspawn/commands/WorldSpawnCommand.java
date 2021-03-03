package me.bluecoaster455.worldspawn.commands;

import me.bluecoaster455.worldspawn.WorldSpawn;
import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WorldSpawnCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(Permissions.hasPermission(sender, Permissions.ADMIN)){
			WSConfig.reload(WorldSpawn.getPlugin());
			sender.sendMessage(WSConfig.getAdminPrefix()+WSConfig.getMessage("config-reload"));
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("command-no-permission"));
		}
		return true;
	}
}
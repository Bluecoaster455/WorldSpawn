package me.bluecoaster455.worldspawn.commands;

import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.Permissions;
import me.bluecoaster455.worldspawn.services.WorldSpawnService;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHubCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("not-a-player-error"));
			return true;
		}
		
		Player p = (Player)sender;
		
		if(Permissions.hasPermission(p, Permissions.ADMIN)){
			Location loc = p.getLocation();
			WorldSpawnService.setHub(loc);
			p.sendMessage(WSConfig.getAdminPrefix()+WSConfig.getMessage("hub-set-success"));
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("command-no-permission"));
		}
		
		return true;
	}

}

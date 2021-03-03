package me.bluecoaster455.worldspawn.commands;

import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.Hub;
import me.bluecoaster455.worldspawn.models.Permissions;
import me.bluecoaster455.worldspawn.services.SpawnDelayService;
import me.bluecoaster455.worldspawn.services.WorldSpawnService;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){ 
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("not-a-player-error"));
			return true;
		}
		
		Player p = (Player)sender;
		
		if(Permissions.hasPermission(p, Permissions.USE)){
			Hub hub = WorldSpawnService.getHub();
			
			if(hub == null || !hub.worldExists()){
				p.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("hub-not-exists"));
				return true;
			}

			SpawnDelayService.delayTeleportHub(p);
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("command-no-permission"));
		}
		
		return true;
	}

}
package me.Bluecoaster455.worldspawn.commands;

import me.Bluecoaster455.worldspawn.config.WSConfig;

import org.bukkit.Location;
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
		
		if(p.hasPermission("worldspawn.use")){
			
			Location loc = p.getLocation();
			Location spawn = WSConfig.getHub();
			
			if(spawn == null){
				p.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("hub-not-exists"));
				return true;
			}

			p.sendMessage(WSConfig.getMainPrefix()+WSConfig.getMessage("hub-spawning"));
			p.teleport(spawn);
			
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("command-no-permission"));
		}
		
		return true;
	}

}
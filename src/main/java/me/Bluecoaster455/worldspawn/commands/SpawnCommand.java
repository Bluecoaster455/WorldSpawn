package me.Bluecoaster455.worldspawn.commands;

import me.Bluecoaster455.worldspawn.WorldSpawn;
import me.Bluecoaster455.worldspawn.config.WSConfig;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor{

	
		
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("not-a-player-error"));
			return true;
		}
		
		Player p = (Player)sender;
		
		if(p.hasPermission("worldspawn.use")){
			
			Location loc = p.getLocation();
			Location spawn = WSConfig.getWorldSpawn(loc.getWorld().getName());
			
			if(spawn == null){
				p.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("no-spawn-world"));
				return true;
			}

			WorldSpawn.getSpawnDelaySvc().delayTeleport(p, WSConfig.getSpawnDelayTime());
			
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("command-no-permission"));
		}
		
		return true;
	}

}
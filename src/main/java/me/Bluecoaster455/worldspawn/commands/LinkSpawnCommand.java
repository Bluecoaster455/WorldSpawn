package me.bluecoaster455.worldspawn.commands;

import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.Permissions;
import me.bluecoaster455.worldspawn.services.WorldSpawnService;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkSpawnCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage(WSConfig.getErrorPrefix() + WSConfig.getMessage("not-a-player-error"));
			return true;
		}
		
		Player p = (Player)sender;
		
		if(Permissions.hasPermission(p, Permissions.ADMIN)){
			
			if(args.length < 1){
				p.sendMessage(WSConfig.getAdminPrefix() + "§6/"+label+" <world>");
				return true;
			}
			
			Location loc = p.getLocation();
			boolean result = WorldSpawnService.setSpawnLink(loc.getWorld().getName(), args[0]);
			if(!result){
				p.sendMessage(WSConfig.getAdminPrefix() + WSConfig.getMessage("spawn-link-fail").replace("%w", args[0]));
			} else {
				p.sendMessage(WSConfig.getAdminPrefix() + WSConfig.getMessage("spawn-link-success").replace("%w", loc.getWorld().getName()).replace("%t", args[0]));
			}
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix() + WSConfig.getMessage("command-no-permission"));
		}
		
		return true;
	}

}

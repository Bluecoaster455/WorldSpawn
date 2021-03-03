package me.bluecoaster455.worldspawn.commands;

import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.Permissions;
import me.bluecoaster455.worldspawn.services.WorldSpawnService;
import me.bluecoaster455.worldspawn.utils.Utils;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("not-a-player-error"));
			return true;
		}
		
		Player p = (Player)sender;
		
		if(Permissions.hasPermission(p, Permissions.ADMIN)){
			Location loc = p.getLocation();
			Boolean respawn = null;

			if(args.length >= 1){
        try {
          respawn = Utils.parseBool(args[0]);
        } catch (IllegalArgumentException ex) {
					p.sendMessage(WSConfig.getAdminPrefix()+"§6/"+label+" <true|false>");
        }
			}

			WorldSpawnService.setSpawn(loc.getWorld().getName(), loc, respawn);
			p.sendMessage(WSConfig.getAdminPrefix()+WSConfig.getMessage("set-spawn-success").replace("%w", loc.getWorld().getName()));
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("command-no-permission"));
		}
		
		return true;
	}

}

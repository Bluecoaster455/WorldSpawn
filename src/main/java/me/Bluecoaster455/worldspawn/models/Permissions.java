package me.bluecoaster455.worldspawn.models;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Permissions {
  ADMIN("worldspawn.admin"),
  BYPASS_DELAY("worldspawn.bypass.delay"),
  USE("worldspawn.use");

  private String permissionNode;

  private Permissions(String permission) {
    this.permissionNode = permission;
  }

  public String getPermissionNode(){
    return this.permissionNode;
  }

  public static boolean hasPermission(Player player, Permissions permission) {
    return player.hasPermission(permission.getPermissionNode());
  }

  public static boolean hasPermission(CommandSender sender, Permissions permission) {
    return sender.hasPermission(permission.getPermissionNode());
  }
}

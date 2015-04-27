package com.gmail.gogobebe2.wild;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Wild extends JavaPlugin {
    private List<Player> playersOnCooldown = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("wild")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Error! You have to be a player to use this command!");
                return true;
            }
            Player player = (Player) sender;
            if (args.length > 0) {
                player.sendMessage(ChatColor.GREEN + "You entered some arguments to the command /wild. I will just ignore them :P");
            }
            //

        }
        return false;
    }

    private Location getGround(Location location) {
        Block block = location.getBlock();
        double y = location.getY();
        if (block.getType().equals(Material.AIR)) {
            location.setY(y - 1);
            return getGround(location);
        }
        return location;
    }
}

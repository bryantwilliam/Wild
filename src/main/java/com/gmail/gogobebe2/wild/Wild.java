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
import java.util.Random;

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

            teleRandomLocation(player, -5000, 5000);
            return true;
        }
        return false;
    }

    public static double randDouble(double min, double max) {
        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }

    private void teleRandomLocation(Player player, double min, double max) {
        Location location = player.getLocation();
        while (true) {
            location.setX(randDouble(min, max));
            location.setZ(randDouble(min, max));
            Location ground = getGround(location);
            if (ground != null) {
                location = ground;
                break;
            }
        }
        player.teleport(location);
        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "After exploring the land, you've finally come across"
                + "some land that looks like a good spot to build your faction home...");
        player.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "Goodluck on your adventures and missions!!!");
    }

    private Location getGround(Location location) {
        return getGround(location, 260D);
    }

    private Location getGround(Location location, double y) {
        location.setY(y);
        Block block = location.getBlock();
        if (y < 0) {
            return null;
        }
        else if (block.getType().equals(Material.AIR) || block.getType().equals(Material.LAVA) || block.getType().equals(Material.WATER)) {
            location.setY(y - 1);
            return getGround(location, y);
        }
        return location;
    }
}

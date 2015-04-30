package com.gmail.gogobebe2.wild;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Wild extends JavaPlugin {
    private Map<Player, Time> playersOnCooldown = new HashMap<>();
    private final Time cooldownTime = new Time(TimeUnit.HOURS.toMillis(24));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("wild")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Error! You have to be a player to use this command!");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("wild.use")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }
            Time time = new Time();
            if (playersOnCooldown.containsKey(player)) {
                Time timeBeen = new Time(time.getMiliseconds() - playersOnCooldown.get(player).getMiliseconds());
                if (timeBeen.getHours() < cooldownTime.getHours() && !player.hasPermission("wild.nocooldown")) {
                    Time timeRemaining = new Time(cooldownTime.getMiliseconds() - timeBeen.getMiliseconds());
                    player.sendMessage(ChatColor.RED + "Error! You can only use this command every 24 hours."
                            + " You currently have " + ChatColor.DARK_RED + timeRemaining.getFormattedTime() + ChatColor.RED + " remaining.");
                    return true;
                }
                playersOnCooldown.remove(player);
            }
            playersOnCooldown.put(player, time);
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
        return min + (max - min) * r.nextDouble();
    }

    private void teleRandomLocation(Player player, double min, double max) {
        Location location = player.getLocation();
        Location destination;
        do {
            destination = location.clone();
            destination.setX(randDouble(min, max));
            destination.setZ(randDouble(min, max));
            destination = getGround(destination, 257D);
            if (destination != null) {
                break;
            }
        } while (true);
        player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Traveling into the wild where beasts roam the land and men fight to survive.");
        player.teleport(destination);
        player.sendMessage(ChatColor.GREEN + "You have been teleported to X:" + ChatColor.GOLD + Math.round(destination.getX() * 100.0) / 100.0
                + ChatColor.GREEN + ", Y:" + ChatColor.GOLD + Math.round(destination.getY() * 100.0) / 100.0 + ChatColor.GREEN + ", Z:"
                + ChatColor.GOLD + Math.round(destination.getZ() * 100.0) / 100.0  + ChatColor.GREEN + ".");
    }

    private Location getGround(Location location, double maxHeight) {
        Location loc = location.clone();
        loc.setY(maxHeight);
        return getGround(loc);
    }

    private Location getGround(Location location) {
        Location loc = location.clone();
        if (loc.getY() < 0) {
            return null;
        } else if (!isSafeToTele(loc)) {
            return getGround(getLocationUnderneath(loc));
        }
        return loc;
    }

    private boolean isSafeToTele(Location location) {
        Location underLoc = getLocationUnderneath(location);
        Block block = location.getBlock();
        Block underBlock = underLoc.getBlock();
        return block.getType().equals(Material.AIR)
                && !(underBlock.getType().equals(Material.AIR)
                || (underBlock.getType().equals(Material.LAVA) || underBlock.getType().equals(Material.STATIONARY_LAVA))
                || (underBlock.getType().equals(Material.WATER) || underBlock.getType().equals(Material.STATIONARY_WATER)));

    }

    private Location getLocationUnderneath(Location location) {
        Location loc = location.clone();
        return loc.subtract(0, 1, 0);
    }
}

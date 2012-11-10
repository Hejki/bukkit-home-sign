package org.hejki.bukkit.homesign;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * HomeSign plugin, register edit sign listener for home location set. Use command /home to teleport
 * player to it's home location.
 *
 * @author Petr Hejkal
 */
public class HomeSign extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlaceSignListener(this), this);
        super.onEnable();
    }

    /**
     * Save player's current location as its home location.
     */
    public void saveHomeLocation(Player player) {
        getConfig().set("homes." + player.getName() + ".location", player.getLocation().toVector());
        getConfig().set("homes." + player.getName() + ".world", player.getWorld().getName());
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("home".equals(label)) {
            Player player = getServer().getPlayer(sender.getName());

            Vector loc = getConfig().getVector("homes." + player.getName() + ".location");
            String worldName = getConfig().getString("homes." + player.getName() + ".world");
            if (loc == null || worldName == null) {
                sender.sendMessage("You have not set home location.");
                sender.sendMessage("Place sign with [home] text to set your home location.");
            } else {
                World world = getServer().getWorld(worldName);
                player.teleport(new Location(world, loc.getX(), loc.getY(), loc.getZ()));
            }
            return true;
        }
        return false;
    }
}

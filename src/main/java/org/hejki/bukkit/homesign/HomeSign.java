package org.hejki.bukkit.homesign;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * HomeSign plugin, register edit sign listener for home location set. Use command /home to teleport
 * player to it's home location.
 *
 * @author Petr Hejkal
 */
public class HomeSign extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlaceSignListener(this), this);
        super.onEnable();
        log("Enabled successfully!");
    }

    public void log(String message) {
        System.out.println(String.format("[%s] %s", getName(), message));
    }

    /**
     * Save player's current location as its home location.
     */
    public void saveHomeLocation(Player player) {
        Location location = player.getLocation();

        setConfig(player, "world", player.getWorld().getName());
        setConfig(player, "location.x", location.getX());
        setConfig(player, "location.y", location.getY());
        setConfig(player, "location.z", location.getZ());
        setConfig(player, "location.f", location.getYaw());
        setConfig(player, "location.p", location.getPitch());
        saveConfig();
    }

    private void setConfig(Player player, String config, Object value) {
        getConfig().set(getConfigKey(player, config), value);
    }

    private String getConfigKey(Player player, String config) {
        return String.format("homes.%s.%s", player.getName(), config);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("home".equals(label)) {
            Player player = getServer().getPlayer(sender.getName());

            String worldName = getConfig().getString("homes." + player.getName() + ".world");

            if (worldName == null) {
                sender.sendMessage("You have not set home location.");
                sender.sendMessage("Place sign with [home] text to set your home location.");
            } else {
                World world = getServer().getWorld(worldName);
                double x = getConfig().getDouble(getConfigKey(player, "location.x"));
                double y = getConfig().getDouble(getConfigKey(player, "location.y"));
                double z = getConfig().getDouble(getConfigKey(player, "location.z"));
                float yaw = (float) getConfig().getDouble(getConfigKey(player, "location.f"));
                float pitch = (float) getConfig().getDouble(getConfigKey(player, "location.p"));

                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);
                player.getLocation().setPitch(pitch);
                player.getLocation().setYaw(yaw);
            }
            return true;
        }
        return false;
    }
}

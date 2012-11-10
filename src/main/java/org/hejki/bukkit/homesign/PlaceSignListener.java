package org.hejki.bukkit.homesign;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Check sign change event and register home location if sign first line is "[home]".
 *
 * @author Petr Hejkal
 */
public class PlaceSignListener implements Listener {
    private HomeSign plugin;

    public PlaceSignListener(HomeSign plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void placeSign(SignChangeEvent e) {
        String fistLine = e.getLine(0);
        if ("[home]".equalsIgnoreCase(fistLine) == false) {
            return;
        }

        plugin.saveHomeLocation(e.getPlayer());
        e.setLine(0, "");
    }
}

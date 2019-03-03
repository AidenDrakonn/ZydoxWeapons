package me.drakonn.zydoxweapons.weapons.cobwebbow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CobwebBreakEvent implements Listener {

    private CobwebBow cobwebBow;
    public CobwebBreakEvent(CobwebBow cobwebBow) {
        this.cobwebBow = cobwebBow;
    }

    @EventHandler
    public void onCobwebBreak(BlockBreakEvent event)
    {
        if(!cobwebBow.cobwebs.contains(event.getBlock().getLocation()))
            return;

        event.setCancelled(true);
    }
}

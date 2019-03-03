package me.drakonn.zydoxweapons.weapons.cobwebbow;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowHitEntityListener implements Listener {

    private CobwebBow cobwebBow;
    public ArrowHitEntityListener(CobwebBow cobwebBow) {
        this.cobwebBow = cobwebBow;
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void projectileHitEvent(EntityDamageByEntityEvent event)
    {
        if(!cobwebBow.arrows.contains((Projectile)event.getDamager()))
            return;

        event.setCancelled(true);
        Projectile projectile = (Projectile)event.getDamager();
        cobwebBow.arrows.remove(projectile);
        projectile.remove();
        Location oldLoc = event.getEntity().getLocation().getBlock().getLocation();
        Location location = new Location(oldLoc.getWorld(), oldLoc.getBlockX(), oldLoc.getBlockY()+1, oldLoc.getBlockZ());
        cobwebBow.cobwebs.add(location);
        Block block = location.getBlock();
        BlockPlaceEvent blockEvent = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(Material.FIRE), (Player)((Projectile)event.getDamager()).getShooter(), true);
        Bukkit.getPluginManager().callEvent(blockEvent);
        if(!blockEvent.isCancelled()) {
            cobwebBow.cobwebs.add(location);
            Material oldMat = block.getType();
            byte data = block.getData();
            block.setType(Material.WEB);

            new BukkitRunnable() {

                @Override
                public void run() {
                    block.setType(oldMat);
                    block.setData(data);
                    cobwebBow.cobwebs.remove(location);
                }
            }.runTaskLater(ZydoxWeapons.getInstance(), (long) cobwebBow.getLinger() * 20);
        }
    }
}

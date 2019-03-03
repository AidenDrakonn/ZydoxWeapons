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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowHitListener implements Listener {

    private CobwebBow cobwebBow;
    public ArrowHitListener(CobwebBow cobwebBow) {
        this.cobwebBow = cobwebBow;
    }

    @EventHandler (priority = EventPriority.LOW)
    public void projectileHitEvent(ProjectileHitEvent event)
    {
        if(!cobwebBow.arrows.contains(event.getEntity()))
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!cobwebBow.arrows.contains(event.getEntity()))
                    return;

                Location location = event.getEntity().getLocation().getBlock().getLocation();
                Block block = location.getBlock();
                Projectile projectile = event.getEntity();
                cobwebBow.arrows.remove(projectile);
                projectile.remove();
                BlockPlaceEvent blockEvent = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(Material.FIRE), (Player)event.getEntity().getShooter(), true);
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
        }.runTaskLater(ZydoxWeapons.getInstance(), 4);
    }
}

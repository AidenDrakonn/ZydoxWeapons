package me.drakonn.zydoxweapons.weapons.flamethrower;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.flamethrower.customprojectile.event.CustomProjectileHitEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FlameHitListener implements Listener {

    private FlameThrower flameThrower;
    public FlameHitListener(FlameThrower flameThrower)
    {
        this.flameThrower = flameThrower;
    }

    @EventHandler
    public void onFlameHit(CustomProjectileHitEvent event)
    {
        event.getProjectile().getEntity().remove();
        if(event.getHitType().equals(CustomProjectileHitEvent.HitType.BLOCK))
        {
            Location location = event.getProjectile().getEntity().getLocation();
            Block block = location.getBlock();
            BlockPlaceEvent blockEvent = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(Material.FIRE), (Player)event.getProjectile().getShooter(), true);
            Bukkit.getPluginManager().callEvent(blockEvent);
            if(!blockEvent.isCancelled()) {
                if (flameThrower.fireOnHit)
                    location.getBlock().setType(Material.FIRE);

                Item item = location.getWorld().dropItem(location, new ItemStack(Material.BLAZE_POWDER));
                item.setPickupDelay((int) FlameThrower.getLinger() * 25);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        item.remove();
                    }
                }.runTaskLater(ZydoxWeapons.getInstance(), (long) FlameThrower.getLinger());
            }
            return;
        }
        if(event.getHitType().equals(CustomProjectileHitEvent.HitType.ENTITY)) {
            LivingEntity hitEntity = event.getHitEntity();
            double damage = flameThrower.getDamage() - (Util.getDamageReduced(hitEntity) * flameThrower.getDamage());
            EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(event.getProjectile().getShooter(),event.getHitEntity(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
            Bukkit.getPluginManager().callEvent(damageEvent);
            if(!damageEvent.isCancelled())
            {
                hitEntity.damage(damage);
                if(flameThrower.catchAttackedOnFire)
                    hitEntity.setFireTicks(flameThrower.burnFor*20);
            }
        }
    }
}

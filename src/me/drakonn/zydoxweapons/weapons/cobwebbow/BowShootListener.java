package me.drakonn.zydoxweapons.weapons.cobwebbow;

import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ammo.Ammo;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class BowShootListener implements Listener {

    private CobwebBow cobwebBow;
    public BowShootListener(CobwebBow cobwebBow) {
        this.cobwebBow = cobwebBow;
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event)
    {
        if(!(event.getEntity() instanceof Player))
            return;

        ItemStack item = event.getBow();
        Player player = (Player)event.getEntity();

        if(!cobwebBow.isItem(item))
            return;

        if(Util.getAmmo(item) == 0){
        player.sendMessage(MessageManager.reloadAmmo);
        event.setCancelled(true);
        return;
        }

        event.getProjectile().remove();

        ItemStack newItem = cobwebBow.setNbtData(Util.getUses(item)-1, Util.getAmmo(item)-1);
        player.getInventory().setItemInHand(newItem);
        Projectile projectile = player.shootArrow();
        cobwebBow.arrows.add(projectile);
        if(Util.getUses(item) == 1)
        {
            int ammo = Util.getAmmo(item);
            if(ammo > 0) {
                ItemStack ammoItem = Ammo.getAmmoItem();
                ammoItem.setAmount(ammo);
                Util.givePlayerItem(player, ammoItem);
            }
            player.getInventory().setItemInHand(null);
            player.sendMessage(MessageManager.outOfUses);
        }

        player.updateInventory();
    }
}

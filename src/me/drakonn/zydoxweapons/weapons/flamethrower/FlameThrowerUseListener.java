package me.drakonn.zydoxweapons.weapons.flamethrower;

import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ammo.Ammo;
import me.drakonn.zydoxweapons.weapons.flamethrower.customprojectile.projectile.ItemProjectile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class FlameThrowerUseListener implements Listener {

    private FlameThrower flameThrower;
    public FlameThrowerUseListener(FlameThrower flameThrower)
    {
        this.flameThrower = flameThrower;
    }

    @EventHandler
    public void onFlameThrowerUse(PlayerInteractEvent event)
    {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(event.getItem() == null)
            return;

        ItemStack item = event.getItem();

        if(!flameThrower.isItem(item))
            return;

        Player player = event.getPlayer();
        event.setCancelled(true);

        if(Util.getAmmo(item) == 0){
            player.sendMessage(MessageManager.reloadAmmo);
            event.setCancelled(true);
            return;
        }

        ItemStack newItem = flameThrower.setNbtData(Util.getUses(item)-1, Util.getAmmo(item)-1);
        player.getInventory().setItemInHand(newItem);

        if(Util.getUses(item) == 1) {
            int ammo = Util.getAmmo(item);
            if(ammo > 0) {
                ItemStack ammoItem = Ammo.getAmmoItem();
                ammoItem.setAmount(ammo);
                Util.givePlayerItem(player, ammoItem);
            }
            player.getInventory().setItemInHand(null);
            player.sendMessage(MessageManager.outOfUses);
        }

        int i = 0;
        Location location = player.getEyeLocation();
        for(int pitchOff = -2; pitchOff <= 2; ++pitchOff)
        {
            for(int yawOff = -5; yawOff <= 5; ++yawOff)
            {
                i++;
                if(i%3 ==0) {
                    location.setYaw(location.getYaw() + yawOff);
                    location.setPitch(location.getPitch() + pitchOff);
                    String randomUUID = UUID.randomUUID().toString();
                    ItemStack projectileItem = new ItemStack(Material.BLAZE_POWDER);
                    ItemMeta meta = projectileItem.getItemMeta();
                    meta.setLore(Arrays.asList(randomUUID));
                    projectileItem.setItemMeta(meta);
                    ItemProjectile projectile = new ItemProjectile(UUID.randomUUID().toString(), player, location, projectileItem, flameThrower.getPower());
                    if (flameThrower.ammoOnFire)
                        projectile.setOnFire(Integer.MAX_VALUE);
                }
            }
        }
    }
}

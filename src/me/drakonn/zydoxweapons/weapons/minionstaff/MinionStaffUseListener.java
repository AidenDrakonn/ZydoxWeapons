package me.drakonn.zydoxweapons.weapons.minionstaff;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.EntityTypes;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ammo.Ammo;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class MinionStaffUseListener implements Listener {

    private MinionStaff minionStaff;
    public MinionStaffUseListener(MinionStaff minionStaff)
    {
        this.minionStaff = minionStaff;
    }

    @EventHandler
    public void onMinionStaffUse(PlayerInteractEvent event)
    {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(event.getItem() == null)
            return;

        ItemStack item = event.getItem();

        if(!minionStaff.isItem(item))
            return;

        Player player = event.getPlayer();
        event.setCancelled(true);

        if(Util.getAmmo(item) == 0){
            player.sendMessage(MessageManager.reloadAmmo);
            event.setCancelled(true);
            return;
        }

        ItemStack newItem = minionStaff.setNbtData(Util.getUses(item)-1, Util.getAmmo(item)-1);
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

        Location location = player.getLocation();

        for(int amount = 0; amount < minionStaff.getAmount(); amount++)
        {
            net.minecraft.server.v1_8_R3.Entity NMSentity = new ZombieMinion(location.getWorld(), player);
            Location randomLoc = Util.randomLocation(location, 6);
            EntityTypes.spawnEntity(NMSentity , randomLoc);
            Entity entity = NMSentity.getBukkitEntity();
            minionStaff.minions.put(entity, player.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                minionStaff.minions.remove(entity);
                if(entity.isDead())
                    return;
                entity.remove();
                }
            }.runTaskLater(ZydoxWeapons.getInstance(), minionStaff.getStayFor()*20);
        }
    }


}

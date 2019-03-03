package me.drakonn.zydoxweapons.weapons.witchstaff;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.EntityTypes;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ammo.Ammo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class WitchStaffUseListener implements Listener {

    private WitchStaff witchStaff;
    public WitchStaffUseListener(WitchStaff witchStaff) {
        this.witchStaff = witchStaff;
    }
    private HashMap<UUID, Integer> scheduler = new HashMap<>();

    @EventHandler
    public void onWitchStaffUse(PlayerInteractEvent event)
    {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(event.getItem() == null)
            return;

        ItemStack item = event.getItem();

        if(!witchStaff.isItem(item))
            return;

        Player player = event.getPlayer();
        event.setCancelled(true);

        if(Util.getAmmo(item) == 0){
            player.sendMessage(MessageManager.reloadAmmo);
            event.setCancelled(true);
            return;
        }

        ItemStack newItem = witchStaff.setNbtData(Util.getUses(item)-1, Util.getAmmo(item)-1);
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

        for(int amount = 0; amount < witchStaff.getAmount(); amount++)
        {
            Location randomLoc = Util.randomLocation(location, 6);
            CustomEntityBat NMSBat = new CustomEntityBat(player.getWorld(), player);
            EntityTypes.spawnEntity(NMSBat, randomLoc);
            Bat bat = (Bat)NMSBat.getBukkitEntity();
            bat.setHealth(witchStaff.getHealth());
            runBat(NMSBat);
            witchStaff.bats.add(bat);
            new BukkitRunnable() {
                @Override
                public void run() {
                if(bat.isDead())
                    return;
                bat.setHealth(0);
                bat.remove();
                }
            }.runTaskLater(ZydoxWeapons.getInstance(), witchStaff.getStayFor()*20);
        }
    }

    private void runBat(CustomEntityBat customBat)
    {
        Bat bat = (Bat)customBat.getBukkitEntity();
        new BukkitRunnable() {
            boolean b;
            int lastAttack = 0;
            int i = 0;
            LivingEntity target = customBat.target;

            {
                if(target != null)
                b = track(target.getLocation(), 2.7D, bat);
                else
                    b = false;
            }

            public void run() {
                if (target != null && !target.isDead()) {
                    i++;

                    if (!b) {
                        b = track(target.getLocation(), 2D, bat);
                    }

                    if (!target.isDead() && b && lastAttack <= i-2 && !bat.isDead()) {
                        double damage = witchStaff.getDamage() - (Util.getDamageReduced(target)*witchStaff.getDamage());
                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(customBat.master, customBat.target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
                        Bukkit.getPluginManager().callEvent(event);
                        if(!event.isCancelled()) {
                            if (Math.random() < witchStaff.getEffectChance()) {
                                PotionEffect effect = getRandomEffect();
                                effect.apply(target);
                            }

                            b = false;
                            lastAttack = i;
                        }
                        return;
                    }

                    if(bat.isDead()) {
                        cancel();
                        return;
                    }
                }
                else
                {
                    target = customBat.target;
                }
            }
        }.runTaskTimer(ZydoxWeapons.getInstance(), 3, 4);
    }

    public boolean track(Location location, double allowedDistance, Bat bat) {
        if (!bat.isDead()) {
            double distanceBetween = location.distanceSquared(bat.getLocation());
            if(distanceBetween > allowedDistance)
                return false;
            else return true;
        } else {
            return false;
        }
    }

    private PotionEffect getRandomEffect()
    {
        List<PotionEffect> effects = witchStaff.getEffects();
        int index = ThreadLocalRandom.current().nextInt(effects.size());
        return effects.get(index);
    }
}

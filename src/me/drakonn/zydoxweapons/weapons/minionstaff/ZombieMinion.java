package me.drakonn.zydoxweapons.weapons.minionstaff;

import me.drakonn.zydoxweapons.util.Util;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityZombie;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

public class ZombieMinion extends EntityZombie {

    private Player target;

    public ZombieMinion(org.bukkit.World world, Player master)
    {
        super(((CraftWorld)world).getHandle());
        setHealth(MinionStaff.getHealth());
        setBaby(true);
        setCustomName(MinionStaff.getMinionName());
        setCustomNameVisible(true);
        setEquipment(4, CraftItemStack.asNMSCopy(MinionStaff.getHelmet()));
        setEquipment(3, CraftItemStack.asNMSCopy(MinionStaff.getChestplate()));
        setEquipment(2, CraftItemStack.asNMSCopy(MinionStaff.getLeggings()));
        setEquipment(1, CraftItemStack.asNMSCopy(MinionStaff.getBoots()));
        setEquipment(0, CraftItemStack.asNMSCopy(MinionStaff.getWeapon()));
        LivingEntity tempTarget = Util.getTarget(master);
        if(tempTarget instanceof Player)
            target = (Player)tempTarget;
        if(target != null)
            setGoalTarget(((EntityLiving) ((CraftLivingEntity)target).getHandle()) , EntityTargetEvent.TargetReason.CUSTOM, true);
    }
}

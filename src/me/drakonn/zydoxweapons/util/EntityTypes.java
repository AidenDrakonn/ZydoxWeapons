package me.drakonn.zydoxweapons.util;

import me.drakonn.zydoxweapons.weapons.minionstaff.ZombieMinion;
import me.drakonn.zydoxweapons.weapons.witchstaff.CustomEntityBat;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.Map;

public enum EntityTypes {

    ZOMBIE_MINION("Zombie", 54, ZombieMinion.class),
    BAT_MINION("Bat", 65, CustomEntityBat .class);

    EntityTypes(String name, int id, Class<? extends Entity> custom)
    {
        addToMaps(custom, name, id);
    }

    public static void spawnEntity(Entity entity, Location loc)
    {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld)loc.getWorld()).getHandle().addEntity(entity);
    }

    private static void addToMaps(Class clazz, String name, int id)
    {
        ((Map)Util.getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map)Util.getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        ((Map)Util.getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
    }
}

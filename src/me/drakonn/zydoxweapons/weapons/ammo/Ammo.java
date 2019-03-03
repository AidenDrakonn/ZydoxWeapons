package me.drakonn.zydoxweapons.weapons.ammo;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.util.Util;
import org.bukkit.inventory.ItemStack;


public class Ammo {

    private static ItemStack item;

    private AmmoGui ammoGui = new AmmoGui(ZydoxWeapons.getInstance().getConfig());

    public Ammo()
    {
        ZydoxWeapons zydoxWeapons = ZydoxWeapons.getInstance();
        item = Util.getItem(zydoxWeapons.getConfig().getConfigurationSection("ammo.item"));
        ammoGui.setupGui();
        zydoxWeapons.getServer().getPluginManager().registerEvents(new AmmoGuiListener(), zydoxWeapons);
    }


    public static ItemStack getAmmoItem() {
        return item.clone();
    }

}

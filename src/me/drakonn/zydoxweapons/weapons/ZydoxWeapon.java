package me.drakonn.zydoxweapons.weapons;

import com.google.common.collect.Lists;
import me.drakonn.zydoxweapons.util.NBTApi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class ZydoxWeapon {


    private static List<ZydoxWeapon> zydoxWeapons = Lists.newArrayList();

    private String name;
    public int uses;
    public int maxAmmo;
    public ItemStack item;
    private List<String> aliases;

    public ZydoxWeapon(String name) {
        this.name = name;
        this.aliases = Lists.newArrayList();
        zydoxWeapons.add(this);
    }

    public boolean isItem(ItemStack item)
    {
        if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().equals(getItem().getItemMeta().getDisplayName())
                    && item.getType().equals(getItem().getType()))
                return true;
        }
        return false;
    }

    public ItemStack setNbtData(int uses, int ammo)
    {
        NBTItem nbtItem = new NBTItem(getItem());
        nbtItem.setInteger("uses", uses);
        nbtItem.setInteger("ammo", ammo);
        ItemStack item = nbtItem.getItem();
        item = setPlaceHolders(item, uses, ammo);
        return item;
    }

    public ItemStack setPlaceHolders(ItemStack item, int uses, int ammo)
    {
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasLore())
            return item;
        List<String> lore = meta.getLore();
        List<String> newLore = new ArrayList<>();

        for(String string : lore)
        {
            string = string.replaceAll("%ammo%", Integer.toString(ammo));
            string = string.replaceAll("%uses%", Integer.toString(uses));
            newLore.add(string);
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    public abstract ItemStack getItem();

    public abstract ItemStack getSetItem();

    public abstract int repairTokensDo();

    public abstract ShapedRecipe getRecipe();

    public static final ZydoxWeapon getZydoxWeapon(String name) {
        return zydoxWeapons.stream().filter(zydoxWeapon -> zydoxWeapon.getName().equalsIgnoreCase(name))
        .findFirst().orElse(null);
    }

    public static final ZydoxWeapon getZydoxWeapon(ItemStack item) {
        return zydoxWeapons.stream().filter(zydoxWeapon -> zydoxWeapon.isItem(item))
                .findFirst().orElse(null);
    }

    public String getName() {
        return name;
    }

    public int getUses() {
        return uses;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }
}

package me.drakonn.zydoxweapons.weapons.cobwebbow;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ZydoxWeapon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class CobwebBow extends ZydoxWeapon {

    private double linger;
    private int repairTokens;
    public List<Projectile> arrows = new ArrayList<>();
    public List<Location> cobwebs = new ArrayList<>();

    public CobwebBow() {
        super("cobwebbow");
        ConfigurationSection section = ZydoxWeapons.getInstance().getConfig().getConfigurationSection("cobwebbow");
        this.linger = section.getDouble("linger");
        this.maxAmmo = section.getInt("maxammo");
        this.uses = section.getInt("uses");
        this.repairTokens = section.getInt("repairtoken");
        this.item = Util.getItem(section.getConfigurationSection("item"));
        item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        registerListeners();
    }

    private void registerListeners()
    {
        ZydoxWeapons instance = ZydoxWeapons.getInstance();
        instance.getServer().getPluginManager().registerEvents(new BowShootListener(this), instance);
        instance.getServer().getPluginManager().registerEvents(new ArrowHitListener(this), instance);
        instance.getServer().getPluginManager().registerEvents(new ArrowHitEntityListener(this), instance);
        instance.getServer().getPluginManager().registerEvents(new CobwebBreakEvent(this), instance);
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public ItemStack getSetItem() {
        return setNbtData(uses, 0);
    }

    public int repairTokensDo() {
        return repairTokens;
    }

    public ShapedRecipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getSetItem());
        recipe.shape("msm", "sns", "msm");
        recipe.setIngredient('s', Material.STRING);
        recipe.setIngredient('m', Material.MOB_SPAWNER, 51);
        recipe.setIngredient('n', Material.NETHER_STAR);
        return recipe;
    }

    public double getLinger() {
        return linger;
    }
}

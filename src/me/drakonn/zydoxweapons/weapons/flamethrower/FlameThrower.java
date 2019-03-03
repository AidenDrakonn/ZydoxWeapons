package me.drakonn.zydoxweapons.weapons.flamethrower;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ZydoxWeapon;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class FlameThrower extends ZydoxWeapon {

    private static int linger;
    private float power;
    private int damage;
    private int repairTokens;
    public boolean fireOnHit;
    public boolean ammoOnFire;
    public boolean catchAttackedOnFire;
    public int burnFor;

    public FlameThrower() {
        super("flamethrower");
        ConfigurationSection section = ZydoxWeapons.getInstance().getConfig().getConfigurationSection("flamethrower");
        linger = section.getInt("linger");
        this.damage = section.getInt("damage");
        this.power = (float)section.getDouble("power");
        this.maxAmmo = section.getInt("maxammo");
        this.uses = section.getInt("uses");
        this.item = Util.getItem(section.getConfigurationSection("item"));
        this.fireOnHit = section.getBoolean("fireonhit");
        this.ammoOnFire = section.getBoolean("ammoonfire");
        this.repairTokens = section.getInt("repairtoken");
        this.catchAttackedOnFire = section.getBoolean("catchattackedonfire");
        this.burnFor = section.getInt("catchattackedonfireduration");
        registerListeners();
    }

    private void registerListeners()
    {
        ZydoxWeapons instance = ZydoxWeapons.getInstance();
        instance.getServer().getPluginManager().registerEvents(new FlameHitListener(this), instance);
        instance.getServer().getPluginManager().registerEvents(new FlameThrowerUseListener(this), instance);
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
        recipe.shape("mpm", "pnp", "mpm");
        recipe.setIngredient('p', Material.BLAZE_POWDER);
        recipe.setIngredient('m', Material.MOB_SPAWNER, 62);
        recipe.setIngredient('n', Material.NETHER_STAR);
        return recipe;
    }

    public static double getLinger() {
        return linger;
    }

    public float getPower() {
        return power;
    }

    public int getDamage() {
        return damage;
    }


}

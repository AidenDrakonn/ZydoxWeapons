package me.drakonn.zydoxweapons.weapons.witchstaff;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ZydoxWeapon;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Bat;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class WitchStaff extends ZydoxWeapon {

    public List<Bat> bats = new ArrayList<>();
    private List<PotionEffect> effects = new ArrayList<>();
    private int amount;
    private int damage;
    private int stayFor;
    private int health;
    private int repairTokens;
    private double effectChance;

    public WitchStaff()
    {
        super("witchstaff");
        ConfigurationSection section = ZydoxWeapons.getInstance().getConfig().getConfigurationSection("witchstaff");
        this.maxAmmo = section.getInt("maxammo");
        this.uses = section.getInt("uses");
        this.repairTokens = section.getInt("repairtoken");
        this.item = Util.getItem(section.getConfigurationSection("item"));
        ConfigurationSection batSection = section.getConfigurationSection("bat");
        health = batSection.getInt("health");
        amount = batSection.getInt("amount");
        damage = batSection.getInt("damage");
        stayFor = batSection.getInt("stayfor");
        effectChance = batSection.getDouble("effectchance");
        for(String key : batSection.getConfigurationSection("effects").getKeys(false))
            effects.add(Util.getEffect(batSection.getConfigurationSection("effects."+key)));

        registerListeners();
    }

    private void registerListeners()
    {
        ZydoxWeapons instance = ZydoxWeapons.getInstance();
        instance.getServer().getPluginManager().registerEvents(new WitchStaffUseListener(this), instance);
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
        recipe.shape("mbm", "bnb", "mbm");
        recipe.setIngredient('b', Material.GLASS_BOTTLE);
        recipe.setIngredient('m', Material.MOB_SPAWNER, 66);
        recipe.setIngredient('n', Material.NETHER_STAR);
        return recipe;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }

    public int getAmount() {
        return amount;
    }

    public int getStayFor() {
        return stayFor;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public double getEffectChance() {
        return effectChance;
    }
}

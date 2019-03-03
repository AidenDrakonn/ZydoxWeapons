package me.drakonn.zydoxweapons.weapons.minionstaff;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ZydoxWeapon;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.UUID;

public class MinionStaff extends ZydoxWeapon {

    public HashMap<Entity, UUID> minions = new HashMap<>();
    private int amount;
    private int stayFor;
    private static int health;
    private int repairTokens;
    private static String minionName;
    private static ItemStack helmet;
    private static ItemStack chestplate;
    private static ItemStack leggings;
    private static ItemStack boots;
    private static ItemStack weapon;

    public MinionStaff() {
        super("minionstaff");
        ConfigurationSection section = ZydoxWeapons.getInstance().getConfig().getConfigurationSection("minionstaff");
        this.maxAmmo = section.getInt("maxammo");
        this.uses = section.getInt("uses");
        this.item = Util.getItem(section.getConfigurationSection("item"));
        ConfigurationSection minionSection = section.getConfigurationSection("minion");
        minionName = Util.color(minionSection.getString("name"));
        health = minionSection.getInt("health");
        amount = minionSection.getInt("amount");
        repairTokens = section.getInt("repairtoken");
        stayFor = minionSection.getInt("stayfor");
        helmet = Util.getEquipment(minionSection.getConfigurationSection("equipment.helmet"));
        chestplate = Util.getEquipment(minionSection.getConfigurationSection("equipment.chestplate"));
        leggings = Util.getEquipment(minionSection.getConfigurationSection("equipment.leggings"));
        boots = Util.getEquipment(minionSection.getConfigurationSection("equipment.boots"));
        weapon = Util.getEquipment(minionSection.getConfigurationSection("equipment.weapon"));
        registerListeners();
    }

    private void registerListeners()
    {
        ZydoxWeapons instance = ZydoxWeapons.getInstance();
        instance.getServer().getPluginManager().registerEvents(new MinionStaffUseListener(this), instance);
        instance.getServer().getPluginManager().registerEvents(new MinionAttackListener(this), instance);
        instance.getServer().getPluginManager().registerEvents(new MinionTargetListener(this), instance);
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
        recipe.shape("mfm", "fnf", "mfm");
        recipe.setIngredient('f', Material.ROTTEN_FLESH);
        recipe.setIngredient('m', Material.MOB_SPAWNER, 54);
        recipe.setIngredient('n', Material.NETHER_STAR);
        return recipe;
    }

    public int getAmount() {
        return amount;
    }

    public int getStayFor() {
        return stayFor;
    }

    public static int getHealth() {
        return health;
    }

    public static String getMinionName() {
        return minionName;
    }

    public static ItemStack getHelmet() {
        return helmet;
    }

    public static ItemStack getChestplate() {
        return chestplate;
    }

    public static ItemStack getLeggings() {
        return leggings;
    }

    public static ItemStack getBoots() {
        return boots;
    }

    public static ItemStack getWeapon() {
        return weapon;
    }
}

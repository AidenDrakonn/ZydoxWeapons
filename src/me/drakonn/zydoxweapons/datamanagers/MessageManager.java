package me.drakonn.zydoxweapons.datamanagers;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    public static String boughtAmmo;
    public static String noAmmo;
    public static String reloadAmmo;
    public static String outOfUses;
    public static String noPermission;
    public static String insufficientFunds;
    public static String cantUnload;
    public static String ammoLoaded;
    public static String ammoUnloaded;
    public static String cantLoad;
    public static String invalidCommand;
    public static List<String> help = new ArrayList<>();

    private ZydoxWeapons plugin;
    public MessageManager(ZydoxWeapons plugin)
    {
        this.plugin = plugin;
    }

    public void setMessages()
    {
        help.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("messages");
        boughtAmmo = Util.color(section.getString("boughtammo"));
        noAmmo = Util.color(section.getString("noammo"));
        reloadAmmo = Util.color(section.getString("reloadammo"));
        outOfUses = Util.color(section.getString("outofuses"));
        insufficientFunds = Util.color(section.getString("insufficientfunds"));
        noPermission = Util.color(section.getString("nopermission"));
        cantUnload = Util.color(section.getString("cantunload"));
        ammoLoaded = Util.color(section.getString("ammoloaded"));
        ammoUnloaded = Util.color(section.getString("ammounloaded"));
        cantLoad = Util.color(section.getString("cantload"));
        invalidCommand = Util.color(section.getString("invalidcommand"));
        setHelp();
    }

    private void setHelp()
    {
        help.add("§7-----------------§bZydoxWeapons§7-----------------");
        help.add("§b/zweapons give (player) [amount] §8- §fGive a player a zydox weapon");
        help.add("§b/zweapons reload §8- §fReload the plugin and apply all config changes");
        help.add("§b/zweapons help §8- §fShow this help message");
        help.add("§b/ammo §8- §fOpens the ammo shop");
        help.add("§b/load §8- §fLoads the weapon in your hand");
        help.add("§b/unload §8- §fUnloads the weapon in your hand");
    }
}

package me.drakonn.zydoxweapons;

import me.drakonn.zydoxweapons.command.ZWeaponsCommand;
import me.drakonn.zydoxweapons.command.ammo.AmmoCommand;
import me.drakonn.zydoxweapons.command.ammo.LoadCommand;
import me.drakonn.zydoxweapons.command.ammo.UnloadCommand;
import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.EntityTypes;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ammo.Ammo;
import me.drakonn.zydoxweapons.weapons.cobwebbow.CobwebBow;
import me.drakonn.zydoxweapons.weapons.flamethrower.FlameThrower;
import me.drakonn.zydoxweapons.weapons.minionstaff.MinionStaff;
import me.drakonn.zydoxweapons.weapons.minionstaff.ZombieMinion;
import me.drakonn.zydoxweapons.weapons.witchstaff.WitchStaff;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public class ZydoxWeapons extends JavaPlugin {

    private CobwebBow cobwebBow;
    private FlameThrower flameThrower;
    private MinionStaff minionStaff;
    private WitchStaff witchStaff;
    private Ammo ammo;
    private MessageManager messageManager;
    private static ZydoxWeapons instance;
    private Economy economy;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        economy = Util.setupEconomy(this);
        load();
        registerRecipes();
        registerCommands();
    }

    public void load()
    {
        messageManager = new MessageManager(this);
        messageManager.setMessages();
        witchStaff = new WitchStaff();
        flameThrower = new FlameThrower();
        cobwebBow = new CobwebBow();
        minionStaff = new MinionStaff();
        ammo = new Ammo();
    }

    private void registerRecipes()
    {
        getServer().addRecipe(cobwebBow.getRecipe());
        getServer().addRecipe(flameThrower.getRecipe());
        getServer().addRecipe(minionStaff.getRecipe());
        getServer().addRecipe(witchStaff.getRecipe());
    }

    private void registerCommands()
    {
        getCommand("load").setExecutor(new LoadCommand());
        getCommand("ammo").setExecutor(new AmmoCommand());
        getCommand("unload").setExecutor(new UnloadCommand());
        getCommand("zweapons").setExecutor(new ZWeaponsCommand());
    }

    public static ZydoxWeapons getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }
}

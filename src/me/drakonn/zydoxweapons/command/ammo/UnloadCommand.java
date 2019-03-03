package me.drakonn.zydoxweapons.command.ammo;

import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ZydoxWeapon;
import me.drakonn.zydoxweapons.weapons.ammo.Ammo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class UnloadCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!command.getLabel().equalsIgnoreCase("unload"))
            return true;

        if(!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;

        if(!player.hasPermission("zweapons.ammo.unload"))
        {
            player.sendMessage(MessageManager.noPermission);
            return true;
        }

        PlayerInventory inv = player.getInventory();

        ItemStack item = inv.getItemInHand();
        ZydoxWeapon weapon = ZydoxWeapon.getZydoxWeapon(item);
        if(weapon == null)
        {
            player.sendMessage(MessageManager.cantUnload);
            return true;
        }
        int currentAmmo = Util.getAmmo(item);
        int amount = currentAmmo;
        if(args.length > 0 && Util.isInt(args[0]) && Integer.valueOf(args[0]) <= weapon.getMaxAmmo())
            amount = Integer.valueOf(args[0]);

        if(amount > currentAmmo)
            amount = currentAmmo;

        ItemStack unloadedWeapon = weapon.setNbtData(Util.getUses(item), currentAmmo-amount);
        inv.setItemInHand(unloadedWeapon);
        ItemStack ammoToAdd = Ammo.getAmmoItem();
        ammoToAdd.setAmount(amount);
        if(ammoToAdd.getAmount() != 0)
            Util.givePlayerItem(player, ammoToAdd);
        String ammoUnloaded = MessageManager.ammoUnloaded;
        ammoUnloaded = ammoUnloaded.replaceAll("%amount%", Integer.toString(amount));
        player.sendMessage(ammoUnloaded);
        player.updateInventory();
        return true;
    }
}

package me.drakonn.zydoxweapons.command.ammo;

import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.Util;
import me.drakonn.zydoxweapons.weapons.ZydoxWeapon;
import me.drakonn.zydoxweapons.weapons.ammo.Ammo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class LoadCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!command.getLabel().equalsIgnoreCase("load"))
            return true;

        if(!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;

        if(!player.hasPermission("zweapons.ammo.load"))
        {
            player.sendMessage(MessageManager.noPermission);
            return true;
        }
        PlayerInventory inv = player.getInventory();

        ItemStack item = inv.getItemInHand();
        ZydoxWeapon weapon = ZydoxWeapon.getZydoxWeapon(item);
        if(weapon == null)
        {
            player.sendMessage(MessageManager.cantLoad);
            return true;
        }

        int amount = weapon.getMaxAmmo()-Util.getAmmo(item);
        if(args.length > 0 && Util.isInt(args[0]) && Integer.valueOf(args[0]) <= weapon.getMaxAmmo())
            amount = Integer.valueOf(args[0]);

        int totalAmmo = getAmmo(inv);
        if(!(totalAmmo >= amount))
        {
            player.sendMessage(MessageManager.noAmmo);
            return true;
        }

        int totalLoaded = Util.getAmmo(item)+amount;

        if(totalLoaded > weapon.getMaxAmmo())
            amount = weapon.getMaxAmmo()-Util.getAmmo(item);


        ItemStack loadedWeapon = weapon.setNbtData(Util.getUses(item), Util.getAmmo(item)+amount);
        inv.setItemInHand(loadedWeapon);
        removeItemFromInv(inv, amount);
        String ammoLoaded = MessageManager.ammoLoaded;
        ammoLoaded = ammoLoaded.replaceAll("%amount%", Integer.toString(amount));
        player.sendMessage(ammoLoaded);
        player.updateInventory();
        return true;
    }

    private int getAmmo(Inventory inv)
    {
        ItemStack ammo = Ammo.getAmmoItem();
        int i = 0;
        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;

            if(!item.hasItemMeta() || !item.getItemMeta().hasLore())
                continue;

            if(item.getItemMeta().getLore().equals(ammo.getItemMeta().getLore()))
                i = i + item.getAmount();
        }
        return i;
    }

    private void removeItemFromInv(PlayerInventory inv, int amt) {
        ItemStack item = Ammo.getAmmoItem();
        int amount = 0;
        for (final ItemStack i : inv.getContents())
            if (i != null && i.getType() == item.getType())
                amount += i.getAmount();
        inv.remove(item.getType());
        if (amount > amt) {
            item.setAmount(amount - amt);
            inv.addItem(item);
        }
    }


}

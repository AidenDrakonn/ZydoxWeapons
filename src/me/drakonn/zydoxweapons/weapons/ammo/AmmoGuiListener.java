package me.drakonn.zydoxweapons.weapons.ammo;

import me.drakonn.zydoxweapons.ZydoxWeapons;
import me.drakonn.zydoxweapons.datamanagers.MessageManager;
import me.drakonn.zydoxweapons.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AmmoGuiListener implements Listener {


    @EventHandler
    public void onGuiInteract(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(!event.getClickedInventory().getTitle().equals(AmmoGui.getGuiTitle()))
            return;

        if(event.getClickedInventory().getSize() != AmmoGui.getGuiSize())
            return;

        Player player = (Player)event.getWhoClicked();

        event.setCancelled(true);
        player.updateInventory();

        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getType().equals(Material.AIR))
            return;

        AmmoGuiItem ammoGuiItem = AmmoGui.getAmmoGuiItem(clickedItem);

        if(ammoGuiItem == null)
            return;

        int cost = ammoGuiItem.getCost();
        if(ZydoxWeapons.getInstance().getEconomy() != null) {
            Economy economy = ZydoxWeapons.getInstance().getEconomy();
            double balance = economy.getBalance(player);
            if (balance <= cost) {
                player.sendMessage(MessageManager.insufficientFunds);
                return;
            }
            economy.withdrawPlayer(player, cost);
        }

        ItemStack ammoItem = Ammo.getAmmoItem();
        ammoItem.setAmount(ammoGuiItem.getAmount());
        Util.givePlayerItem(player, ammoItem);
        player.sendMessage(getBoughtMessage(ammoGuiItem.getAmount(), ammoGuiItem.getCost()));
    }

    private String getBoughtMessage(int amount, int cost)
    {
        String message = MessageManager.boughtAmmo;
        message = message.replaceAll("%amount%", Integer.toString(amount));
        message = message.replaceAll("%price%", Integer.toString(cost));
        return message;
    }
}

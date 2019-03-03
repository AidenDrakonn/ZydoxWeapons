package me.drakonn.zydoxweapons.weapons.ammo;

import me.drakonn.zydoxweapons.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AmmoGui {

    private static List<AmmoGuiItem> items = new ArrayList<>();
    private static Inventory ammoGuiInventory;
    private static int guiSize;
    private static String guiTitle;
    private ItemStack fillItem;
    
    private FileConfiguration config;
    public AmmoGui(FileConfiguration config)
    {
        this.config = config;
    }

    public void setupGui()
    {
        fillItem = Util.getItem(config.getConfigurationSection("ammo.gui.fillitem"));
        
        ConfigurationSection topSection = config.getConfigurationSection("ammo.gui.items");
        for(String key : topSection.getKeys(false))
        {
            ConfigurationSection section = topSection.getConfigurationSection(key);
            loadGuiItem(section);
        }

        guiSize = config.getInt("ammo.gui.size");
        guiTitle = Util.color(config.getString("ammo.gui.title"));
        Inventory inv = Bukkit.createInventory(null, guiSize, guiTitle);

        for(AmmoGuiItem ammoGuiItem : items)
        {
            inv.setItem(ammoGuiItem.getInvSlot(), ammoGuiItem.getItem());
        }

        for(int i =0; i < inv.getSize(); i++)
        {
            if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR))
            {
                inv.setItem(i, fillItem);
            }
        }
        ammoGuiInventory = inv;
    }

    private void loadGuiItem(ConfigurationSection section)
    {
        int cost = Util.getCost(section);
        int amount = Util.getAmount(section);
        int invSlot = Util.getInvSlot(section);
        List<String> lore = Util.getlore(section);
        String name = Util.color(section.getString("name"));
        ItemStack item = Ammo.getAmmoItem();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        if(amount <= 64)
            item.setAmount(amount);
        items.add(new AmmoGuiItem(item, invSlot, cost, amount));
    }

    public static AmmoGuiItem getAmmoGuiItem(ItemStack item) {
        return items.stream().filter(ammoGuiItem -> ammoGuiItem.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())
                && ammoGuiItem.getItem().getItemMeta().getLore().equals(item.getItemMeta().getLore())).findFirst().orElse(null);
    }

    public static int getGuiSize() {
        return guiSize;
    }

    public static String getGuiTitle() {
        return guiTitle;
    }

    public static Inventory getAmmoGuiInventory() {
        return ammoGuiInventory;
    }
}

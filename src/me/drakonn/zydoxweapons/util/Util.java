package me.drakonn.zydoxweapons.util;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import me.drakonn.zydoxweapons.util.NBTApi.NBTItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    public static ItemStack getItem(final ConfigurationSection section) {
        Set<String> keys = section.getKeys(false);
        ItemStack item = new ItemStack(Material.PAPER);
        if(keys.contains("material")) {
            if (getMaterial(section.getString("material")) != null)
                item.setType(getMaterial(section.getString("material")));
            if (section.getString("material").split(":").length > 1 && isInt(section.getString("material").split(":")[1]))
                item.setDurability((short) Integer.parseInt(section.getString("material").split(":")[1]));
        }

        final ItemMeta meta = item.getItemMeta();

        if (keys.contains("name"))
            meta.setDisplayName(color(section.getString( "name")));
        else meta.setDisplayName(color("&cNo name Set"));

        if (keys.contains("lore"))
            meta.setLore(color(section.getStringList( "lore")));

        if (keys.contains("enchanted") && section.getBoolean("enchanted"))
            meta.addEnchant(EnchantGlow.getGlow(), 1, true);

        item.setItemMeta(meta);
        return item;
    }

    public static List<String> getlore(final ConfigurationSection section)
    {
        List<String> lore;
        if(section.getKeys(false).contains("lore"))
            lore = section.getStringList("lore");
        else lore = new ArrayList<>();
        
        return color(lore);
    }
    
    public static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static int getAmmo(final ItemStack item)
    {
        NBTItem nbtItem = new NBTItem(item);
        if(!nbtItem.hasKey("ammo"))
            return 0;
        return nbtItem.getInteger("ammo");
    }

    public static int getUses(final ItemStack item)
    {
        NBTItem nbtItem = new NBTItem(item);
        if(!nbtItem.hasKey("uses"))
            return 0;
        return nbtItem.getInteger("uses");
    }

    public static List<String> color(final List<String> list) {
        final List<String> colored = new ArrayList<String>();
        for (final String s : list)
            colored.add(color(s));
        return colored;
    }

    public static Material getMaterial(String s) {
        s = s.toUpperCase().replace(" ", "_");
        if (s.contains(":"))
            s = s.split("\\:")[0];
        try {
            return Material.getMaterial(s) != null ? Material.getMaterial(s)
                    : Material.matchMaterial(s) != null ? Material.matchMaterial(s)
                    : Material.valueOf(s) != null ? Material.valueOf(s)
                    : isInt(s) ? Material.getMaterial(Integer.parseInt(s)) : Material.PAPER;
        } catch (final Exception ex) {
            return Material.PAPER;
        }
    }

    public static boolean isInt(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public static class EnchantGlow extends EnchantmentWrapper {
        private static Enchantment glow = null;
        private final String name;

        public static ItemStack addGlow(final ItemStack itemstack) {
            itemstack.addEnchantment(getGlow(), 1);
            return itemstack;
        }

        public static Enchantment getGlow() {
            if (glow != null)
                return glow;
            Field field = null;
            try {
                field = Enchantment.class.getDeclaredField("acceptingNew");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                return glow;
            }
            field.setAccessible(true);
            try {
                field.set(null, true);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            try {
                glow = new EnchantGlow(Enchantment.values().length + 100);
            } catch (final Exception e) {
                glow = Enchantment.getByName("Glow");
            }
            if (Enchantment.getByName("Glow") == null)
                Enchantment.registerEnchantment(glow);
            return glow;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enchantment getEnchantment() {
            return Enchantment.getByName("Glow");
        }

        @Override
        public int getMaxLevel() {
            return 1;
        }

        @Override
        public int getStartLevel() {
            return 1;
        }

        @Override
        public EnchantmentTarget getItemTarget() {
            return EnchantmentTarget.ALL;
        }

        @Override
        public boolean canEnchantItem(final ItemStack item) {
            return true;
        }

        @Override
        public boolean conflictsWith(final Enchantment other) {
            return false;
        }

        public EnchantGlow(final int i) {
            super(i);
            name = "Glow";
        }
    }

    public static void givePlayerItem(final Player player, final ItemStack item)
    {
        if (player.getInventory().firstEmpty() != -1)
        {
            player.getInventory().addItem(item);
        }
        else if (getSlot(player, item.getType()) != -1)
        {
            player.getInventory().addItem(item);
        }
        else
        {
            player.sendMessage( "§c§l(!) §cYour inventory is full!");
            player.getWorld().dropItem(player.getLocation(), item);
        }
    }

    public static int getSlot(final Player p, final Material type)
    {
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if ((p.getInventory().getItem(i).getType() == type) && (p.getInventory().getItem(i).getAmount() < p.getInventory().getItem(i).getMaxStackSize())) {
                return i;
            }
        }
        return -1;
    }

    public static Economy setupEconomy(final Plugin p) {
        if (p.getServer().getPluginManager().getPlugin("Vault") == null)
            return null;
        final RegisteredServiceProvider<Economy> rsp = p.getServer().getServicesManager()
                .getRegistration(Economy.class);
        return rsp == null ? null : rsp.getProvider();
    }

    public static int getCost(final ConfigurationSection section)
    {
        if(section.getKeys(false).contains("cost"))
            return section.getInt("cost");
        System.out.println("[ZydoxWeapons] No cost set for " + section.getName() + " setting cost as 0");
        return 0;
    }

    public static int getInvSlot(final ConfigurationSection section)
    {
        if(section.getKeys(false).contains("invslot"))
        {
            return section.getInt("invslot");
        }

        System.out.println("[ZydoxWeapons] No invslot set for " + section.getName() + " setting to 0");
        return 0;
    }

    public static int getAmount(final ConfigurationSection section)
    {
        if(section.getKeys(false).contains("amount"))
        {
            return section.getInt("amount");
        }

        System.out.println("[ZydoxWeapons] No amount set for " + section.getName() + " setting to 1");
        return 1;
    }

    public static double getDamageReduced(final LivingEntity entity)
    {
        EntityEquipment equipment = entity.getEquipment();
        ItemStack boots = equipment.getBoots();
        ItemStack helmet = equipment.getHelmet();
        ItemStack chest = equipment.getChestplate();
        ItemStack pants = equipment.getLeggings();
        double red = 0.0;
        if(helmet != null) {
            if (helmet.getType() == Material.LEATHER_HELMET)
                red = red + 0.04;
            else
                if (helmet.getType() == Material.GOLD_HELMET)
                    red = red + 0.08;
                else
                    if (helmet.getType() == Material.CHAINMAIL_HELMET)
                        red = red + 0.08;
                    else
                        if (helmet.getType() == Material.IRON_HELMET)
                            red = red + 0.08;
                        else
                            if (helmet.getType() == Material.DIAMOND_HELMET)
                                red = red + 0.12;
        }
        //

        if(boots != null) {
            if (boots.getType() == Material.LEATHER_BOOTS)
                red = red + 0.04;
            else
                if (boots.getType() == Material.GOLD_BOOTS)
                    red = red + 0.04;
                else
                    if (boots.getType() == Material.CHAINMAIL_BOOTS)
                        red = red + 0.04;
                    else
                        if (boots.getType() == Material.IRON_BOOTS)
                            red = red + 0.08;
                        else
                            if (boots.getType() == Material.DIAMOND_BOOTS)
                                red = red + 0.12;
        }
        //
        if(pants != null) {
            if (pants.getType() == Material.LEATHER_LEGGINGS)
                red = red + 0.08;
            else
                if (pants.getType() == Material.GOLD_LEGGINGS)
                    red = red + 0.12;
                else
                    if (pants.getType() == Material.CHAINMAIL_LEGGINGS)
                        red = red + 0.16;
                    else
                        if (pants.getType() == Material.IRON_LEGGINGS)
                            red = red + 0.20;
                        else
                            if (pants.getType() == Material.DIAMOND_LEGGINGS)
                                red = red + 0.24;
        }
        //
        if(chest != null) {
            if (chest.getType() == Material.LEATHER_CHESTPLATE)
                red = red + 0.12;
            else
                if (chest.getType() == Material.GOLD_CHESTPLATE)
                    red = red + 0.20;
                else
                    if (chest.getType() == Material.CHAINMAIL_CHESTPLATE)
                        red = red + 0.20;
                    else
                        if (chest.getType() == Material.IRON_CHESTPLATE)
                            red = red + 0.24;
                        else
                            if (chest.getType() == Material.DIAMOND_CHESTPLATE)
                                red = red + 0.32;
        }
        return red;
    }

    public static Enchantment getEnchantment(final String s) {
        final Map<String, Enchantment> enchants = new HashMap<String, Enchantment>();
        enchants.put("power", Enchantment.ARROW_DAMAGE);
        enchants.put("flame", Enchantment.ARROW_FIRE);
        enchants.put("infinite", Enchantment.ARROW_INFINITE);
        enchants.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchants.put("sharp", Enchantment.DAMAGE_ALL);
        enchants.put("damage", Enchantment.DAMAGE_ALL);
        enchants.put("sharpness", Enchantment.DAMAGE_ALL);
        enchants.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("arthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchants.put("mining", Enchantment.DIG_SPEED);
        enchants.put("efficiency", Enchantment.DIG_SPEED);
        enchants.put("unbreaking", Enchantment.DURABILITY);
        enchants.put("fire", Enchantment.FIRE_ASPECT);
        enchants.put("kb", Enchantment.KNOCKBACK);
        enchants.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("loot", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("water", Enchantment.OXYGEN);
        enchants.put("waterbreathing", Enchantment.OXYGEN);
        enchants.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("explosive", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("explosions", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("protexplosives", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("protexplosions", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("explosiveprot", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("explosiveprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("fall", Enchantment.PROTECTION_FALL);
        enchants.put("feather", Enchantment.PROTECTION_FALL);
        enchants.put("falling", Enchantment.PROTECTION_FALL);
        enchants.put("featherfalling", Enchantment.PROTECTION_FALL);
        enchants.put("fireprot", Enchantment.PROTECTION_FIRE);
        enchants.put("fireprotection", Enchantment.PROTECTION_FIRE);
        enchants.put("projprot", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("projprotection", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("projectileprot", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("arrowprotection", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("silk", Enchantment.SILK_TOUCH);
        enchants.put("silktouch", Enchantment.SILK_TOUCH);
        enchants.put("watermine", Enchantment.WATER_WORKER);
        enchants.put("watermining", Enchantment.WATER_WORKER);
        enchants.put("arrowdamage", Enchantment.ARROW_DAMAGE);
        enchants.put("arrowfire", Enchantment.ARROW_FIRE);
        enchants.put("arrowinfinite", Enchantment.ARROW_INFINITE);
        enchants.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
        enchants.put("damageall", Enchantment.DAMAGE_ALL);
        enchants.put("damagearthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("damageundead", Enchantment.DAMAGE_UNDEAD);
        enchants.put("digspeed", Enchantment.DIG_SPEED);
        enchants.put("fireaspect", Enchantment.FIRE_ASPECT);
        enchants.put("lootbonusblocks", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("lootbonusmobs", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("protectionenviromental", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("protectionexplosions", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("protectionfall", Enchantment.PROTECTION_FALL);
        enchants.put("protectionfire", Enchantment.PROTECTION_FIRE);
        enchants.put("protectionprojectile", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("waterworker", Enchantment.WATER_WORKER);
        enchants.put("thorns", Enchantment.THORNS);
        return Enchantment.getByName(s) != null ? Enchantment.getByName(s.toUpperCase())
                : enchants.containsKey(s.toLowerCase().replace("_", ""))
                ? enchants.get(s.toLowerCase().replace("_", ""))
                : null;
    }


    public static Location randomLocation(final Location playerLoc, final double offset)
    {
        World world = playerLoc.getWorld();
        double playerLocX = playerLoc.getX();
        double playerLocZ = playerLoc.getZ();
        double playerLocY = playerLoc.getY();
        Location min = new Location(world, playerLocX-offset, playerLocY, playerLocZ-offset);
        Location max = new Location(world, playerLocX+offset, playerLocY, playerLocZ+offset);
        Location range = new Location(min.getWorld(), Math.abs(max.getX() - min.getX()), min.getY(), Math.abs(max.getZ() - min.getZ()));
        double xLoc = Math.random() * range.getX() + (min.getX() <= max.getX() ? min.getX() : max.getX());
        double zLoc = Math.random() * range.getZ() + (min.getZ() <= max.getZ() ? min.getZ() : max.getZ());
        double yLoc;
        if((new Location(world, xLoc, playerLocY, zLoc)).getBlock().getType().equals(Material.AIR))
            yLoc = playerLocY + (offset)*getRandomSign();
        else yLoc = world.getHighestBlockYAt((int)(long)Math.round(xLoc),(int)(long)Math.round(xLoc));
        return new Location(world, xLoc, yLoc, zLoc);
    }

    public static int getRandomSign() {
        Random rand = new Random();
        if(rand.nextBoolean())
            return -1;
        else
            return 1;
    }

    public static Object getPrivateField(final String fieldName, final Class clazz, final Object object)
    {
        Field field;
        Object o = null;

        try
        {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return o;
    }

    public static ItemStack getEquipment(ConfigurationSection section)
    {
        ItemStack item = new ItemStack(getMaterial(section.getString("item")));
        if(section.getKeys(false).contains("enchantments"))
        {
            for(String enchantmentName : section.getStringList("enchantments"))
            {
                Enchantment enchantment = getEnchantment(enchantmentName.split(":")[0]);
                int level = Integer.valueOf(enchantmentName.split(":")[1]);
                item.addUnsafeEnchantment(enchantment, level);
            }
        }
        return item;
    }

    public static LivingEntity getTarget(final Player player)
    {
        Location location = player.getLocation();
        Faction playersFaction = FPlayers.getInstance().getByPlayer(player).getFaction();

        List<LivingEntity> potentialTargets = player.getWorld().getLivingEntities();
        List<Player> playerTargets = new ArrayList<>();
        List<LivingEntity> entityTargets = new ArrayList<>();
        for(LivingEntity potentialTarget : potentialTargets)
        {
            if(potentialTarget.equals(player))
                continue;

            if(potentialTarget.getLocation().distanceSquared(location) > 225)
                continue;

            if(potentialTarget instanceof Player) {
                Player playetTarget = (Player) potentialTarget;
                Faction targetsFaction = FPlayers.getInstance().getByPlayer(playetTarget).getFaction();

                if (targetsFaction.isWilderness()) {
                    playerTargets.add(playetTarget);
                    continue;
                }

                if (!targetsFaction.equals(playersFaction))
                {
                    playerTargets.add(playetTarget);
                    continue;
                }
            }

            if(potentialTarget.getType() != EntityType.BAT)
                entityTargets.add(potentialTarget);
        }

        if(playerTargets.size() == 0)
        {
            if(entityTargets.size() == 0)
                return null;
            int index = ThreadLocalRandom.current().nextInt(entityTargets.size());
            return entityTargets.get(index);
        }
        int index = ThreadLocalRandom.current().nextInt(playerTargets.size());
        return playerTargets.get(index);
    }

    public static PotionEffectType getPotionEffect(final String s) {
        final Map<String, PotionEffectType> types = new HashMap<String, PotionEffectType>();
        types.put("hearts", PotionEffectType.ABSORPTION);
        types.put("blind", PotionEffectType.BLINDNESS);
        types.put("nausea", PotionEffectType.CONFUSION);
        types.put("resistence", PotionEffectType.DAMAGE_RESISTANCE);
        types.put("haste", PotionEffectType.FAST_DIGGING);
        types.put("fireresistence", PotionEffectType.FIRE_RESISTANCE);
        types.put("damage", PotionEffectType.HARM);
        types.put("health", PotionEffectType.HEALTH_BOOST);
        types.put("healthboost", PotionEffectType.HEALTH_BOOST);
        types.put("strength", PotionEffectType.INCREASE_DAMAGE);
        types.put("regen", PotionEffectType.REGENERATION);
        types.put("food", PotionEffectType.SATURATION);
        types.put("slowness", PotionEffectType.SLOW_DIGGING);
        types.put("miningfatigue", PotionEffectType.SLOW_DIGGING);
        types.put("waterbreathing", PotionEffectType.WATER_BREATHING);
        types.put("weak", PotionEffectType.WEAKNESS);
        types.put("damageresistence", PotionEffectType.DAMAGE_RESISTANCE);
        types.put("fastdigging", PotionEffectType.FAST_DIGGING);
        types.put("increasedamage", PotionEffectType.INCREASE_DAMAGE);
        types.put("nightvision", PotionEffectType.NIGHT_VISION);
        types.put("slowdigging", PotionEffectType.SLOW_DIGGING);
        return PotionEffectType.getByName(s.toUpperCase()) != null ? PotionEffectType.getByName(s.toUpperCase())
                : types.containsKey(s.toLowerCase().replace("_", "")) ? types.get(s.toLowerCase().replace("_", ""))
                : null;
    }

    public static PotionEffect getEffect(ConfigurationSection section)
    {
        PotionEffectType type = getPotionEffect(section.getName());
        int level = section.getInt("level")-1;
        int duration = section.getInt("duration")*20;
        PotionEffect effect = new PotionEffect(type, duration, level);
        System.out.println("[ZydoxWeapons] creating potion effect of type " + type.getName() + " lasts for duration " + duration);
        System.out.println("[ZydoxWeapons] confirming potion effect of type " + effect.getType().getName() + " lasts for duration " + effect.getDuration());
        if(type != null)
            return effect;
        else
            return new PotionEffect(PotionEffectType.NIGHT_VISION, duration, level);
    }
}

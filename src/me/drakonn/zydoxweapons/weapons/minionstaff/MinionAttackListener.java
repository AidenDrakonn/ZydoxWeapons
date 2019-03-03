package me.drakonn.zydoxweapons.weapons.minionstaff;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MinionAttackListener implements Listener {

    private MinionStaff minionStaff;
    public MinionAttackListener(MinionStaff minionStaff) {
        this.minionStaff = minionStaff;
    }

    public void onMinionAttack(EntityDamageByEntityEvent event)
    {
        Entity entity = event.getDamager();
        Entity entityTarget = event.getEntity();

        if(!(entityTarget instanceof Player))
            return;

        if(!minionStaff.minions.keySet().contains(entity))
            return;

        Player target = (Player)entityTarget;
        Player master = Bukkit.getPlayer(minionStaff.minions.get(entity));

        Faction targetFaction = FPlayers.getInstance().getByPlayer(target).getFaction();
        Faction masterFaction = FPlayers.getInstance().getByPlayer(master).getFaction();
        if((targetFaction.equals(masterFaction) && !targetFaction.isWilderness()) || target.equals(master))
            event.setCancelled(true);
    }
}

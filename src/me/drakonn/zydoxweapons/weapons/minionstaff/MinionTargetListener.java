package me.drakonn.zydoxweapons.weapons.minionstaff;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class MinionTargetListener implements Listener {

    private MinionStaff minionStaff;
    public MinionTargetListener(MinionStaff minionStaff) {
        this.minionStaff = minionStaff;
    }

    @EventHandler
    public void onMinionTaregt(EntityTargetEvent event)
    {
        Entity entity = event.getEntity();
        Entity entityTarget = event.getTarget();

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

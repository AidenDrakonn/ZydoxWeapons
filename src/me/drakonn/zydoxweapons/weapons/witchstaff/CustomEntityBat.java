package me.drakonn.zydoxweapons.weapons.witchstaff;

import me.drakonn.zydoxweapons.util.Util;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CustomEntityBat extends EntityBat {

    private BlockPosition a;
    public Player master;
    public LivingEntity target;

    public CustomEntityBat(World world, Player master)
    {
        super(((CraftWorld)world).getHandle());
        this.master = master;
        this.target = Util.getTarget(master);
    }


    @Override
    public void t_() {
        super.t_();
        if (this.isAsleep()) {
            this.motX = this.motY = this.motZ = 0.0D;
            this.locY = (double)MathHelper.floor(this.locY) + 1.0D - (double)this.length;
        } else {
            this.motY *= 0.6000000238418579D;
        }

    }

    @Override
    protected void E() {
        super.E();
        BlockPosition var1 = new BlockPosition(this);
        BlockPosition var2 = var1.up();
        if(target != null && !target.isDead())
        {
            double targetX = target.getLocation().getX();
            double targetY = target.getLocation().getY();
            double targetZ = target.getLocation().getZ();
            this.motX = getXTowards(targetX);
            this.motY = getYTowards(targetY);
            this.motZ = getZTowards(targetZ);
            float var9 = (float)(MathHelper.b(this.motZ, this.motX) * 180.0D / 3.1415927410125732D) - 90.0F;
            float var10 = MathHelper.g(var9 - this.yaw);
            this.ba = 0.5F;
            this.yaw += var10;
        }
        else
        {
            target = Util.getTarget(master);

            if (this.a != null && (!this.world.isEmpty(this.a) || this.a.getY() < 1)) {
                this.a = null;
            }

            if (this.a == null || this.random.nextInt(30) == 0 || this.a.c((double)((int)this.locX), (double)((int)this.locY), (double)((int)this.locZ)) < 4.0D) {
                this.a = new BlockPosition((int)this.locX + this.random.nextInt(7) - this.random.nextInt(7), (int)this.locY + this.random.nextInt(6) - 2, (int)this.locZ + this.random.nextInt(7) - this.random.nextInt(7));
            }

            double var3 = (double)this.a.getX() + 0.5D - this.locX;
            double var5 = (double)this.a.getY() + 0.1D - this.locY;
            double var7 = (double)this.a.getZ() + 0.5D - this.locZ;
            this.motX += (Math.signum(var3) * 0.5D - this.motX) * 0.10000000149011612D;
            this.motY += (Math.signum(var5) * 0.699999988079071D - this.motY) * 0.10000000149011612D;
            this.motZ += (Math.signum(var7) * 0.5D - this.motZ) * 0.10000000149011612D;
            float var9 = (float)(MathHelper.b(this.motZ, this.motX) * 180.0D / 3.1415927410125732D) - 90.0F;
            float var10 = MathHelper.g(var9 - this.yaw);
            this.ba = 0.5F;
            this.yaw += var10;
            if (this.random.nextInt(100) == 0 && this.world.getType(var2).getBlock().isOccluding()) {
                this.setAsleep(true);
            }
        }
    }

    private double getXTowards(double target)
    {
        if(target > locX)
            return 0.16;
        else
            return - 0.16;
    }

    private double getYTowards(double target)
    {
        if(target > locY)
            return 0.16;
        else
            return - 0.16;
    }

    private double getZTowards(double target)
    {
        if(target > locZ)
            return 0.16;
        else
            return - 0.16;
    }

    @Override
    protected boolean s_() {
        return true;
    }

}

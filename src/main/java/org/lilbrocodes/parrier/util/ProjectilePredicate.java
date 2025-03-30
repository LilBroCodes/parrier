package org.lilbrocodes.parrier.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.lilbrocodes.parrier.config.ParrierConfig;

import java.util.UUID;

public class ProjectilePredicate {

    public static boolean test(Entity entity, UUID testerUuid) {
        boolean ret = false;
        if (entity instanceof ArrowEntity arrowEntity) {
            ret = !arrowEntity.inGround;
        } else ret = entity instanceof ProjectileEntity projectileEntity && !projectileEntity.getVelocity().equals(new Vec3d(0,0,0));
        if (ParrierConfig.reParry) return ret;
        else return (!entity.getCommandTags().contains(testerUuid.toString()) && ret);
    }
}

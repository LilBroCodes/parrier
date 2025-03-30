package org.lilbrocodes.parrier.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class ProjectilePredicate implements Predicate<Entity> {

    @Override
    public boolean test(Entity entity) {
        if (entity instanceof ArrowEntity arrowEntity) {
            return !arrowEntity.inGround;
        } else return entity instanceof ProjectileEntity projectileEntity && !projectileEntity.getVelocity().equals(new Vec3d(0,0,0));
    }
}

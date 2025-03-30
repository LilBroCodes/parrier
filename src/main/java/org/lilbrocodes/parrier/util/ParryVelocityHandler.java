package org.lilbrocodes.parrier.util;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.lilbrocodes.parrier.config.ParrierConfig;

public class ParryVelocityHandler {
    public record ParryVelocityContext(ServerPlayerEntity player, ProjectileEntity projectile) {
    }

    public static void createFireball(ParryVelocityContext context) {
        Vec3d position = context.player.getPos().add(0, 1, 0);
        Vec3d velocity = context.projectile.getVelocity();

        if (ParrierConfig.backParry && context.projectile.getOwner() != null && context.projectile.getOwner() != context.player) {
            Vec3d ownerPos = context.projectile.getOwner().getEyePos();
            Vec3d directionToOwner = ownerPos.subtract(context.projectile.getPos()).normalize();
            velocity = directionToOwner.multiply(velocity.length()).normalize();
        } else {
            velocity = context.player.getRotationVector().normalize().multiply(velocity.length());
        }

        SmallFireballEntity fireball = new SmallFireballEntity(context.player.getWorld(), position.x, position.y, position.z, velocity.x, velocity.y, velocity.z);
        fireball.setOwner(context.player);

        multiplyPower(fireball, ParrierConfig.parryStrength);

        fireball.addCommandTag(context.player.getUuid().toString());
        context.player.getWorld().spawnEntity(fireball);
    }

    public static void multiplyPower(SmallFireballEntity fireball, float multiplier) {
        fireball.powerX *= multiplier;
        fireball.powerY *= multiplier;
        fireball.powerZ *= multiplier;
    }
}

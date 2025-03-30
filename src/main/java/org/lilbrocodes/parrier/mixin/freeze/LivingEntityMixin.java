package org.lilbrocodes.parrier.mixin.freeze;

import net.minecraft.entity.LivingEntity;
import org.lilbrocodes.parrier.Parrier;
import org.lilbrocodes.parrier.client.ParrierClient;
import org.lilbrocodes.parrier.config.ParrierConfig;
import org.lilbrocodes.parrier.util.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tickMove(CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.movementFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeMovement) ci.cancel();
        }
    }
}

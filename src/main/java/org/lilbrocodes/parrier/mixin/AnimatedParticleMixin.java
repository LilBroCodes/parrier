package org.lilbrocodes.parrier.mixin;

import net.minecraft.client.particle.AnimatedParticle;
import org.lilbrocodes.parrier.Parrier;
import org.lilbrocodes.parrier.client.ParrierClient;
import org.lilbrocodes.parrier.config.ParrierConfig;
import org.lilbrocodes.parrier.util.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimatedParticle.class)
public class AnimatedParticleMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tickMove(CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.particlesFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeParticles) ci.cancel();
        }
    }
}

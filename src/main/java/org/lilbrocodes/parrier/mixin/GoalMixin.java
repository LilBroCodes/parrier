package org.lilbrocodes.parrier.mixin;

import net.minecraft.entity.ai.goal.Goal;
import org.lilbrocodes.parrier.Parrier;
import org.lilbrocodes.parrier.client.ParrierClient;
import org.lilbrocodes.parrier.config.ParrierConfig;
import org.lilbrocodes.parrier.util.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Goal.class)
public class GoalMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void pauseTick(CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.aiFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeAi) ci.cancel();
        }
    }
}

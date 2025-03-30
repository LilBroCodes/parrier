package org.lilbrocodes.parrier.mixin.freeze;

import net.minecraft.client.texture.SpriteAtlasTexture;
import org.lilbrocodes.parrier.client.ParrierClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {
    @Inject(method = "tickAnimatedSprites", at = @At("HEAD"), cancellable = true)
    public void pauseAnimations(CallbackInfo ci) {
        if (ParrierClient.animationsFrozen) ci.cancel();
    }
}

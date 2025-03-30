package org.lilbrocodes.parrier.mixin.animation;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.lilbrocodes.parrier.client.ParrierClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    public void renderOtherArm(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!ParrierClient.screenFlashed) return;
        matrices.push();
        matrices.translate(-0.9, -1.9, 0);
        matrices.scale(1.5f, 1.5f, 1.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-30));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-120));
        boolean bl = fromHand(hand) == Arm.LEFT;
        float f = bl ? 1.0F : -1.0F;
        float g = MathHelper.sqrt(0);
        float h = -0.3F * MathHelper.sin(g * (float) Math.PI);
        float i = 0.4F * MathHelper.sin(g * (float) (Math.PI * 2));
        float j = -0.4F * MathHelper.sin(0 * (float) Math.PI);
        matrices.translate(f * (h + 0.64000005F), i + -0.6F + 0 * -0.6F, j + -0.71999997F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * 45.0F));
        float k = MathHelper.sin(0 * 0 * (float) Math.PI);
        float l = MathHelper.sin(g * (float) Math.PI);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * l * 70.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * k * -20.0F));
        AbstractClientPlayerEntity abstractClientPlayerEntity = this.client.player;
        RenderSystem.setShaderTexture(0, abstractClientPlayerEntity.getSkinTexture());
        matrices.translate(f * -1.0F, 3.6F, 3.5F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * 120.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(200.0F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * -135.0F));
        matrices.translate(f * 5.6F, 0.0F, 0.0F);
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer)this.entityRenderDispatcher
                .<AbstractClientPlayerEntity>getRenderer(abstractClientPlayerEntity);
        if (bl) {
            playerEntityRenderer.renderRightArm(matrices, vertexConsumers, light, abstractClientPlayerEntity);
        } else {
            playerEntityRenderer.renderLeftArm(matrices, vertexConsumers, light, abstractClientPlayerEntity);
        }
        matrices.pop();
    }

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    public void disableOffhandItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (ParrierClient.screenFlashed && fromLeftHanded(leftHanded) == MinecraftClient.getInstance().options.getMainArm().getValue().getOpposite()) ci.cancel();
    }

    @Inject(method = "renderMapInBothHands", at = @At("HEAD"), cancellable = true)
    public void disableMapRenderingA(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress, CallbackInfo ci) {
        if (ParrierClient.screenFlashed) ci.cancel();
    }

    @Inject(method = "renderMapInOneHand", at = @At("HEAD"), cancellable = true)
    public void disableMapRenderingB(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, Arm arm, float swingProgress, ItemStack stack, CallbackInfo ci) {
        if (ParrierClient.screenFlashed && arm == MinecraftClient.getInstance().options.getMainArm().getValue().getOpposite()) ci.cancel();
    }

    @Unique
    private Arm fromHand(Hand hand) {
        Arm dominantArm = MinecraftClient.getInstance().options.getMainArm().getValue();
        return hand.equals(Hand.MAIN_HAND) ? dominantArm : dominantArm.getOpposite();
    }

    @Unique Arm fromLeftHanded(boolean leftHanded) {
        return leftHanded ? Arm.LEFT : Arm.RIGHT;
    }
}

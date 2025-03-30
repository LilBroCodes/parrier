package org.lilbrocodes.parrier.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lilbrocodes.parrier.networking.FreezeEffectPayload;
import org.lilbrocodes.parrier.networking.ParryPayload;

import java.util.Map;

public class ParrierClient implements ClientModInitializer {
    public static final KeyBinding PARRY_KEYBIND = new KeyBinding(
            "key.parrier.parry",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_R,
            "category.parrier.parrier"
    );

    public static int parryTicks = 0;

    public static boolean screenFlashed = false;
    public static boolean aiFrozen = false;
    public static boolean movementFrozen = false;
    public static boolean animationsFrozen = false;
    public static boolean blocksFrozen = false;
    public static boolean particlesFrozen = false;

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(PARRY_KEYBIND);
        FreezeEffectPayload.registerReceiver();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean bl = false;
            while (PARRY_KEYBIND.wasPressed()) {
                if (client.player == null || screenFlashed || aiFrozen || movementFrozen || animationsFrozen) break;
                ClientPlayNetworking.send(new ParryPayload(true));
                parryTicks = 4;
                bl = true;
            }
            if (!bl && parryTicks > 0) {
                parryTicks --;
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> onHudRender(drawContext));
    }

    public static void onHudRender(DrawContext context) {
        if (screenFlashed) context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), 0x88ffffff);
    }

    public static void handleFreezePacket(MinecraftClient client, ClientPlayNetworkHandler playNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        Map<String, Boolean> freezeState = FreezeEffectPayload.read(packetByteBuf).state();
        screenFlashed = freezeState.get("flashScreen");
        aiFrozen = freezeState.get("freezeAi");
        movementFrozen = freezeState.get("freezeMovement");
        animationsFrozen = freezeState.get("freezeAnimations");
        blocksFrozen = freezeState.get("freezeBlocks");
        particlesFrozen = freezeState.get("freezeParticles");
    }
}

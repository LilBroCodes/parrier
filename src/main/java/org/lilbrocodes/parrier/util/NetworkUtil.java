package org.lilbrocodes.parrier.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import org.lilbrocodes.parrier.config.ParrierConfig;
import org.lilbrocodes.parrier.networking.FreezeEffectPayload;

import java.util.HashMap;
import java.util.Map;

public class NetworkUtil {

    public static void send(ServerPlayerEntity player, boolean flashScreen, boolean freezeMovement, boolean freezeAnimations, boolean freezeAi, boolean freezeBlocks, boolean freezeParticles) {
        Map<String, Boolean> data = new HashMap<>();
        data.put("flashScreen", flashScreen);
        data.put("freezeMovement", freezeMovement);
        data.put("freezeAnimations", freezeAnimations);
        data.put("freezeAi", freezeAi);
        data.put("freezeBlocks", freezeBlocks);
        data.put("freezeParticles", freezeParticles);
        ServerPlayNetworking.send(player, new FreezeEffectPayload(data));
    }

    public static void handleParryEvent(ServerPlayerEntity source) {
        send(source, ParrierConfig.flashScreen, ParrierConfig.freezeMovement, ParrierConfig.freezeAnimations, ParrierConfig.freezeAi, ParrierConfig.freezeBlocks, ParrierConfig.freezeParticles);
        for (ServerPlayerEntity player : source.getServerWorld().getPlayers()) {
            if (player.getUuid() != source.getUuid()) {
                send(player, false, ParrierConfig.freezeMovement, ParrierConfig.freezeAnimations, ParrierConfig.freezeAi, ParrierConfig.freezeBlocks, ParrierConfig.freezeParticles);
            }
        };
    }

    public static void unfreezeClients(ServerPlayerEntity source) {
        for (ServerPlayerEntity player : source.getServerWorld().getPlayers()) {
            send(player, false, false, false, false, false, false);
        };
    }
}

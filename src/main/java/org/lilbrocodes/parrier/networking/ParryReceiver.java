package org.lilbrocodes.parrier.networking;

import net.minecraft.server.network.ServerPlayerEntity;
import org.lilbrocodes.parrier.Parrier;

public class ParryReceiver {
    public void handle(ServerPlayerEntity player) {
        Parrier.parryTicksList.put(player.getUuid(), 4);
    }
}

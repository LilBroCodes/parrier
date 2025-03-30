package org.lilbrocodes.parrier.networking;

import net.minecraft.server.network.ServerPlayerEntity;
import org.lilbrocodes.parrier.Parrier;

public class ParryReceiver {
    public void handle(ServerPlayerEntity player) {
        if(Parrier.parryCooldowns.get(player.getUuid()) == 0) Parrier.parryTicksList.put(player.getUuid(), 4);
    }
}

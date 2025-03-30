package org.lilbrocodes.parrier.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.lilbrocodes.parrier.Parrier;
import org.lilbrocodes.parrier.client.ParrierClient;

import java.util.HashMap;
import java.util.Map;

public record FreezeEffectPayload(Map<String, Boolean> state) implements FabricPacket {
    public static final Identifier ID = Parrier.FREEZE_S2C_ID;
    public static final PacketType<FreezeEffectPayload> TYPE = PacketType.create(ID, FreezeEffectPayload::read);

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeVarInt(state.size());

        for (Map.Entry<String, Boolean> entry : state.entrySet()) {
            packetByteBuf.writeString(entry.getKey());
            packetByteBuf.writeBoolean(entry.getValue());
        }
    }

    public static FreezeEffectPayload read(PacketByteBuf packetByteBuf) {
        int size = packetByteBuf.readVarInt();

        Map<String, Boolean> state = new java.util.HashMap<>();

        for (int i = 0; i < size; i++) {
            String key = packetByteBuf.readString();
            Boolean value = packetByteBuf.readBoolean();
            state.put(key, value);
        }

        return new FreezeEffectPayload(state);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public static void registerReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, ParrierClient::handleFreezePacket);
    }
}

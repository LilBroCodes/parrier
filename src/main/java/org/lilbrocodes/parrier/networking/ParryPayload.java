package org.lilbrocodes.parrier.networking;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.lilbrocodes.parrier.Parrier;

public record ParryPayload(boolean bl) implements FabricPacket {
    public static final Identifier ID = Parrier.PARRY_C2S_ID;
    public static final PacketType<ParryPayload> TYPE = PacketType.create(ID, ParryPayload::read);

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(bl);
    }

    public static ParryPayload read(PacketByteBuf buf) {
        return new ParryPayload(buf.readBoolean());
    }

    public static void registerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> server.execute(() -> handle(player)));
    }

    private static void handle(ServerPlayerEntity player) {
        new ParryReceiver().handle(player);
    }
}

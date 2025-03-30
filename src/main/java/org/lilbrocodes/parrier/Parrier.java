package org.lilbrocodes.parrier;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.lilbrocodes.parrier.config.ParrierConfig;
import org.lilbrocodes.parrier.networking.ParryPayload;
import org.lilbrocodes.parrier.util.NetworkUtil;
import org.lilbrocodes.parrier.util.ParryVelocityHandler;
import org.lilbrocodes.parrier.util.ProjectilePredicate;

import java.util.*;

public class Parrier implements ModInitializer {
    public static final String MOD_ID = "parrier";
    public static final Identifier PARRY_C2S_ID = Identifier.of(MOD_ID, "parry_c2s");
    public static final Identifier FREEZE_S2C_ID = Identifier.of(MOD_ID, "freeze_s2c");
    public static final Map<UUID, Integer> parryTicksList = new HashMap<>();
    public static final Map<UUID, Integer> parryCooldowns = new HashMap<>();
    public static final Map<ParryVelocityHandler.ParryVelocityContext, Integer> queuedParries = new HashMap<>();

    private void fixMap(Map<UUID, Integer> map, MinecraftServer server) {
        map.entrySet().removeIf(entry -> server.getPlayerManager().getPlayer(entry.getKey()) == null && entry.getValue() == 0);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            map.putIfAbsent(player.getUuid(), 0);
        }
    }

    @Override
    public void onInitialize() {
        Sounds.register();
        ParryPayload.registerReceiver();
        MidnightConfig.init(MOD_ID, ParrierConfig.class);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            fixMap(parryCooldowns, server);
            fixMap(parryTicksList, server);
        });

        ServerTickEvents.START_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (parryTicksList.get(player.getUuid()) > 0 && parryCooldowns.get(player.getUuid()) == 0) {
                    Box box = player.getBoundingBox().expand(ParrierConfig.parryRange);
                    List<Entity> projectiles = world.getOtherEntities(player, box, new ProjectilePredicate());

                    if (!projectiles.isEmpty()) {
                        world.playSoundFromEntity(null, player, Sounds.PARRY, SoundCategory.PLAYERS, 2.0f, 1.0f);
                        NetworkUtil.handleParryEvent(player);
                        parryTicksList.put(player.getUuid(), 0);
                    } else {
                        parryTicksList.put(player.getUuid(), parryTicksList.get(player.getUuid()) - 1);
                    }

                    for (Entity entity : projectiles) {
                        ProjectileEntity projectile = (ProjectileEntity) entity;
                        queuedParries.put(new ParryVelocityHandler.ParryVelocityContext(player, projectile), ParrierConfig.gameFreezeTicks);
                        projectile.discard();
                    }
                    parryCooldowns.put(player.getUuid(), ParrierConfig.parryCooldown);
                } else if (parryCooldowns.get(player.getUuid()) > 0) parryCooldowns.put(player.getUuid(), parryCooldowns.get(player.getUuid()) - 1);
            }
            List<ParryVelocityHandler.ParryVelocityContext> lazyRemove = new ArrayList<>();
            for (Map.Entry<ParryVelocityHandler.ParryVelocityContext, Integer> entry : queuedParries.entrySet()) {
                if (entry.getValue() == 0) {
                    ParryVelocityHandler.createFireball(entry.getKey());
                    lazyRemove.add(entry.getKey());
                    NetworkUtil.unfreezeClients(entry.getKey().player());
                } else {
                    queuedParries.put(entry.getKey(), entry.getValue() - 1);
                }
            }
            lazyRemove.forEach(queuedParries::remove);
        });
    }
}

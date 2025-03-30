package org.lilbrocodes.parrier.mixin;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.lilbrocodes.parrier.Parrier;
import org.lilbrocodes.parrier.client.ParrierClient;
import org.lilbrocodes.parrier.config.ParrierConfig;
import org.lilbrocodes.parrier.util.Environment;
import org.lilbrocodes.parrier.util.MixinUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow protected abstract void tickBlock(BlockPos pos, Block block);

    @Shadow protected abstract void tickFluid(BlockPos pos, Fluid fluid);

    @Unique private List<MixinUtils.TickBlockCall> tickBlockCalls = new ArrayList<>();
    @Unique private boolean fixingTickBlockCalls;
    @Unique private List<MixinUtils.TickBlockCall> updateNeighborsCalls = new ArrayList<>();
    @Unique private boolean fixingUpdateNeighborsCalls;
    @Unique private List<MixinUtils.TickFluidCall> tickFluidCalls = new ArrayList<>();
    @Unique private boolean fixingTickFluidCalls;

    @Inject(method = "tickBlock", at = @At("HEAD"), cancellable = true)
    public void pauseBlockTicks(BlockPos pos, Block block, CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.blocksFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeBlocks) ci.cancel();
        }

        if (ci.isCancelled()) tickBlockCalls.add(new MixinUtils.TickBlockCall(pos, block));
        else if(!fixingTickBlockCalls) {
            fixingTickBlockCalls = true;
            tickBlockCalls.forEach(call -> tickBlock(call.pos(), call.block()));
            tickBlockCalls.clear();
            fixingTickBlockCalls = false;
        }
    }

    @Inject(method = "tickFluid", at = @At("HEAD"), cancellable = true)
    public void pauseFluidTicks(BlockPos pos, Fluid fluid, CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.blocksFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeBlocks) ci.cancel();
        }

        if (ci.isCancelled()) tickFluidCalls.add(new MixinUtils.TickFluidCall(pos, fluid));
        else if(!fixingTickFluidCalls) {
            fixingTickFluidCalls = true;
            tickFluidCalls.forEach(call -> tickFluid(call.pos(), call.fluid()));
            tickFluidCalls.clear();
            fixingTickFluidCalls = false;
        }
    }

    @Inject(method = "updateNeighbors", at = @At("HEAD"), cancellable = true)
    public void pauseNeighborUpdates4(BlockPos pos, Block block, CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.blocksFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeBlocks) ci.cancel();
        }

        if (ci.isCancelled()) updateNeighborsCalls.add(new MixinUtils.TickBlockCall(pos, block));
        else if(!fixingUpdateNeighborsCalls) {
            fixingUpdateNeighborsCalls = true;
            updateNeighborsCalls.forEach(call -> tickBlock(call.pos(), call.block()));
            updateNeighborsCalls.clear();
            fixingUpdateNeighborsCalls = false;
        }
    }
}

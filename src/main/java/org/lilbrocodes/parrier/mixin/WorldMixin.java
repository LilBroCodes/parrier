package org.lilbrocodes.parrier.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow public abstract void updateComparators(BlockPos pos, Block block);

    @Shadow public abstract void updateNeighborsAlways(BlockPos pos, Block sourceBlock);

    @Shadow public abstract void updateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction);

    @Shadow public abstract void updateNeighbor(BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify);
    @Shadow public abstract void updateNeighbor(BlockPos pos, Block sourceBlock, BlockPos sourcePos);

    @Unique private List<MixinUtils.TickBlockCall> updateComparatorsCalls = new ArrayList<>();
    @Unique private boolean fixingUpdateComparatorsCalls = false;
    @Unique private List<MixinUtils.TickBlockCall> updateNeighborsAlwaysCalls = new ArrayList<>();
    @Unique private boolean fixingUpdateNeighborsAlwaysCalls = false;
    @Unique private List<MixinUtils.UpdateNeighborsExceptCall> updateNeighborsExceptCalls = new ArrayList<>();
    @Unique private boolean fixingUpdateNeighborsExceptCalls = false;
    @Unique private List<MixinUtils.UpdateNeighborACall> updateNeighborACalls = new ArrayList<>();
    @Unique private boolean fixingUpdateNeighborACalls = false;
    @Unique private List<MixinUtils.UpdateNeighborBCall> updateNeighborBCalls = new ArrayList<>();
    @Unique private boolean fixingUpdateNeighborBCalls = false;

    @Inject(method = "updateComparators", at = @At("HEAD"), cancellable = true)
    public void pauseComparatorUpdates(BlockPos pos, Block block, CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.blocksFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeBlocks) ci.cancel();
        }

        if (ci.isCancelled()) updateComparatorsCalls.add(new MixinUtils.TickBlockCall(pos, block));
        else if (!fixingUpdateComparatorsCalls) {
            fixingUpdateComparatorsCalls = true;
            updateComparatorsCalls.forEach(call -> updateComparators(call.pos(), call.block()));
            updateComparatorsCalls.clear();
            fixingUpdateComparatorsCalls = false;
        }
    }

    @Inject(method = "updateNeighborsAlways", at = @At("HEAD"), cancellable = true)
    public void pauseNeighborUpdates1(BlockPos pos, Block block, CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.blocksFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeBlocks) ci.cancel();
        }

        if (ci.isCancelled()) updateNeighborsAlwaysCalls.add(new MixinUtils.TickBlockCall(pos, block));
        else if (!fixingUpdateNeighborsAlwaysCalls) {
            fixingUpdateNeighborsAlwaysCalls = true;
            updateNeighborsAlwaysCalls.forEach(call -> updateNeighborsAlways(call.pos(), call.block()));
            updateNeighborsAlwaysCalls.clear();
            fixingUpdateNeighborsAlwaysCalls = false;
        }
    }

    @Inject(method = "updateNeighborsExcept", at = @At("HEAD"), cancellable = true)
    public void pauseNeighborUpdates2(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.blocksFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeBlocks) ci.cancel();
        }

        if (ci.isCancelled()) updateNeighborsExceptCalls.add(new MixinUtils.UpdateNeighborsExceptCall(pos, sourceBlock, direction));
        else if (!fixingUpdateNeighborsExceptCalls) {
            fixingUpdateNeighborsExceptCalls = true;
            updateNeighborsExceptCalls.forEach(call -> updateNeighborsExcept(call.pos(), call.sourceBlock(), call.direction()));
            updateNeighborsExceptCalls.clear();
            fixingUpdateNeighborsExceptCalls = false;
        }
    }

    @Inject(method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"), cancellable = true)
    public void pauseNeighborUpdates3(BlockPos pos, Block sourceBlock, BlockPos sourcePos, CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.blocksFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeBlocks) ci.cancel();
        }

        if (ci.isCancelled()) updateNeighborACalls.add(new MixinUtils.UpdateNeighborACall(pos, sourceBlock, sourcePos));
        else if (!fixingUpdateNeighborACalls) {
            fixingUpdateNeighborACalls = true;
            updateNeighborACalls.forEach(call -> updateNeighbor(call.pos(), call.sourceBlock(), call.sourcePos()));
            updateNeighborACalls.clear();
            fixingUpdateNeighborACalls = false;
        }
    }

    @Inject(method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At("HEAD"), cancellable = true)
    public void pauseNeighborUpdates4(BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (Environment.isClient()) {
            if (ParrierClient.blocksFrozen) ci.cancel();
        } else {
            if (!Parrier.queuedParries.isEmpty() && ParrierConfig.freezeBlocks) ci.cancel();
        }

        if (ci.isCancelled()) updateNeighborBCalls.add(new MixinUtils.UpdateNeighborBCall(state, pos, sourceBlock, sourcePos, notify));
        else if (!fixingUpdateNeighborBCalls) {
            fixingUpdateNeighborBCalls = true;
            updateNeighborBCalls.forEach(call -> updateNeighbor(call.state(), call.pos(), call.sourceBlock(), call.sourcePos(), call.n()));
            updateNeighborBCalls.clear();
            fixingUpdateNeighborBCalls = false;
        }
    }
}

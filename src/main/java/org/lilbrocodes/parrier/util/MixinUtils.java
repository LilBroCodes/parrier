package org.lilbrocodes.parrier.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MixinUtils {
    public record UpdateNeighborsExceptCall(BlockPos pos, Block sourceBlock, Direction direction) {

    }

    public record UpdateNeighborACall(BlockPos pos, Block sourceBlock, BlockPos sourcePos) {

    }

    public record UpdateNeighborBCall(BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean n) {

    }

    public record TickBlockCall(BlockPos pos, Block block) {

    }

    public record TickFluidCall(BlockPos pos, Fluid fluid) {

    }
}

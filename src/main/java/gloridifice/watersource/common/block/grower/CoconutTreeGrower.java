package gloridifice.watersource.common.block.grower;

import gloridifice.watersource.registry.ConfiguredFeatureRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import javax.annotation.Nullable;
import java.util.Random;

public class CoconutTreeGrower extends AbstractTreeGrower {
    @Nullable
    protected ConfiguredFeature<TreeConfiguration, ?> getCoconutTreeFeatureConfig (){
        return ConfiguredFeatureRegistry.COCONUT_TREE;
    }

    @Override
    public boolean growTree(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockState state, Random random) {
        ConfiguredFeature<TreeConfiguration, ?> configuredfeature = this.getCoconutTreeFeatureConfig();
        if (configuredfeature == null) {
            return false;
        } else {
            //判定是否可以生长
            for (int i = 1; i <= 7; i++){
                for (int b = -1; b <= 1; b++){
                    if (!level.getBlockState(blockPos.above(i).east(b).north(b)).isAir()) return false;
                }
            }
            for (int a = -3; a <= 3; a++){
                for (int b = 5; b<= 8; b++){
                    if (!level.getBlockState(blockPos.above(b).east(a).north(a)).isAir()) return false;
                }
            }
            //
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 4);
            if (configuredfeature.place(level, chunkGenerator, random, blockPos)) {
                return true;
            } else {
                level.setBlock(blockPos, state, 4);
                return false;
            }
        }
    }

    @Nullable
    @Override
    protected ConfiguredFeature<net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration, ?> getConfiguredFeature(Random p_60014_, boolean p_60015_) {
        return ConfiguredFeatureRegistry.COCONUT_TREE;
    }
}

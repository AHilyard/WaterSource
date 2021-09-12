package gloridifice.watersource.common.block;


import gloridifice.watersource.WaterSource;
import gloridifice.watersource.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;


import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;


public class CoconutTreeHeadBlock extends Block implements IPlantable, BonemealableBlock {
    public CoconutTreeHeadBlock(String name, Properties properties) {
        super(properties);
        this.setRegistryName(name);
    }


    public boolean canGrow(ServerLevel level, BlockPos pos, BlockState state, boolean isClient) {
        if (BlockTags.getAllTags().getTag(new ResourceLocation(WaterSource.MODID, "coconut_tree_logs")).contains(level.getBlockState(pos.below()).getBlock())) {
            return true;
        }
        return false;
    }



    public void grow(ServerLevel level, Random rand, BlockPos pos, BlockState state) {
        int a = rand.nextInt(4);
        Direction direction = Direction.from2DDataValue(a);
        if (level.getBlockState(pos.offset(direction.getNormal())).isAir()) {
            level.setBlock(pos.offset(direction.getNormal()), BlockRegistry.BLOCK_NATURAL_COCONUT.defaultBlockState().setValue(NaturalCoconutBlock.AGE, 0).setValue(HORIZONTAL_FACING, direction), 3);
        }
    }


    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        if (canGrow(level, pos, state, false)) {
            if (!level.isAreaLoaded(pos, 1))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, rand.nextInt(26) == 0)) {
                grow(level, rand, pos, state);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter p_50897_, BlockPos p_50898_, BlockState p_50899_, boolean p_50900_) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level p_50901_, Random p_50902_, BlockPos p_50903_, BlockState p_50904_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, Random random, BlockPos pos, BlockState state) {
        grow(serverLevel, random, pos, state);
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return null;
    }
}

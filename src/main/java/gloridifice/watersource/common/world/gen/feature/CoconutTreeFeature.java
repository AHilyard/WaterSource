package gloridifice.watersource.common.world.gen.feature;

import com.mojang.serialization.Codec;
import gloridifice.watersource.WaterSource;
import gloridifice.watersource.common.block.NaturalCoconutBlock;
import gloridifice.watersource.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class CoconutTreeFeature extends Feature<TreeConfiguration> {
    public CoconutTreeFeature(Codec<TreeConfiguration> codec) {
        super(codec);
    }
    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        Random rand = context.random();
        BlockPos blockPos = context.origin();
        WorldGenLevel level = context.level();
        TreeConfiguration configIn = context.config();
        int a = rand.nextInt(4);
        int height = 7 + rand.nextInt(2);
        if (isSand(level, blockPos.below())) {
            BlockPos pos = blockPos;
            for (int i = 0; i < height / 2; i++) {
                pos = blockPos.above(i);
                setBlock(level, pos, configIn.trunkProvider.getState(rand, pos));
            }
            for (int i = 0; i < height / 2; i++) {
                pos = blockPos.above(height / 2 + i).offset(Direction.from2DDataValue(a + 2).getNormal());
                if (i == height / 2 - 1) {
                    setBlock(level, pos, BlockRegistry.BLOCK_COCONUT_TREE_HEAD.defaultBlockState());
                    for (int n = 2; n <= 5; n++) {
                        if (rand.nextInt(4) == 0) {
                            placeLeaves(level, pos.offset(Direction.from2DDataValue(n).getNormal()), BlockRegistry.BLOCK_NATURAL_COCONUT.defaultBlockState().setValue(NaturalCoconutBlock.AGE, 3).setValue(HORIZONTAL_FACING, Direction.from2DDataValue(n).getOpposite()));
                        }
                    }
                }
                else setBlock(level, pos, configIn.trunkProvider.getState(rand, pos));
            }
            //generate leaves
            pos = pos.above();
            placeLeaves(level, pos, configIn.foliageProvider.getState(rand, pos).setValue(LeavesBlock.DISTANCE, 1));
            for (int i = 1; i <= 5; i++) {
                for (int j = 2; j <= 5; j++) {
                    if (i <= 4) {
                        placeLeaves(level, pos.offset(Direction.from2DDataValue(j).getNormal().multiply(i)), configIn.foliageProvider.getState(rand, pos.west(i)).setValue(LeavesBlock.DISTANCE, 1));
                    }
                    if (i == 2) {
                        placeLeaves(level, pos.offset(Direction.from2DDataValue(j).getNormal().multiply(i)), configIn.foliageProvider.getState(rand, pos.west(i).above()).setValue(LeavesBlock.DISTANCE, 1));
                        for (int u = 2; u <= 5; u++) {
                            placeLeaves(level, pos.offset( Direction.from2DDataValue(j).getNormal().multiply(i)).offset(Direction.from2DDataValue(u).getNormal()), configIn.foliageProvider.getState(rand, pos.west(i).above()).setValue(LeavesBlock.DISTANCE, 1));
                        }
                    }
                    if (i == 4 || i == 2 || i == 5) {
                        placeLeaves(level, pos.offset(Direction.from2DDataValue(j).getNormal().multiply(i)).below(), configIn.foliageProvider.getState(rand, pos.west(i).below()).setValue(LeavesBlock.DISTANCE, 1));
                    }
                }
            }
            return true;
        }
        else return false;
    }


    protected static boolean isSand(LevelReader reader, BlockPos pos) {
        Tag<Block> tag = BlockTags.getAllTags().getTag(new ResourceLocation(WaterSource.MODID, "coconut_soil"));
        if (tag != null) return tag.contains(reader.getBlockState(pos).getBlock());
        return false;
    }

    protected boolean placeLeaves(WorldGenLevel generationReader, BlockPos pos, BlockState blockState) {
        if (isAir(generationReader, pos)) {
            setBlock(generationReader, pos, blockState);
            return true;
        }
        else return false;
    }

}

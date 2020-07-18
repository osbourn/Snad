package com.lazynessmind.snad.block;

import com.lazynessmind.snad.Configs;
import com.lazynessmind.snad.proxy.CommonProxy;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.Random;

public class SnadBlock extends FallingBlock {

    private final int color;

    public SnadBlock(String registryName, int color, MaterialColor materialColor) {
        super(Block.Properties.create(Material.SAND, materialColor).tickRandomly().hardnessAndResistance(0.5f).sound(SoundType.SAND));
        this.color = color;
        Item.Properties itemProps = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
        CommonProxy.ITEMS.register(registryName, () -> new BlockItem(this, itemProps));
    }

    @Override
    public int getDustColor(BlockState state, IBlockReader p_189876_2_, BlockPos p_189876_3_) {
        return this.color;
    }


    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.tick(state, world, pos, random);
        BlockState blockStateAbove = world.getBlockState(pos.up());
        Block blockAbove = blockStateAbove.getBlock();

        if (blockAbove instanceof SugarCaneBlock || blockAbove instanceof CactusBlock) {

            int height = 1;

            boolean isSameBlockType = true;

            while (isSameBlockType) {
                for (int i = 0; i < Configs.SPEED_INCREASE_DEFAULT_VALUE.get(); i++) {
                    if (i == 0 | canSustainPlant(world.getBlockState(pos), world, pos, null, (IPlantable) blockAbove)) {
                        world.getBlockState(pos.up(height)).getBlock().randomTick(world.getBlockState(pos.up(height)), world, pos.up(height), random);
                    }
                }
                height++;
                isSameBlockType = world.getBlockState(pos.up(height)).getBlock().getClass() == blockAbove.getClass();
            }
        } else if (blockAbove instanceof IPlantable) {
            blockStateAbove.randomTick(world, pos.up(), random);
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader blockReader, BlockPos pos, Direction direction, IPlantable iPlantable) {
        final BlockPos plantPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        final PlantType plantType = iPlantable.getPlantType(blockReader, plantPos);

        if (PlantType.DESERT.equals(plantType)) {
            return true;
        } else if (PlantType.WATER.equals(plantType)) {
            return blockReader.getBlockState(pos).getMaterial() == Material.WATER && blockReader.getBlockState(pos) == getDefaultState();
        } else if (PlantType.BEACH.equals(plantType)) {
            return checkIfWater(blockReader, pos.east()) || checkIfWater(blockReader, pos.west()) || checkIfWater(blockReader, pos.north()) || checkIfWater(blockReader, pos.south());
        }
        return false;
    }

    // Checks if the given position is a water block or has the Waterlogged property and that property is true
    private boolean checkIfWater(IBlockReader blockReader, BlockPos pos)
    {
        return blockReader.getBlockState(pos).getMaterial() == Material.WATER || (blockReader.getBlockState(pos).getValues().containsKey(BlockStateProperties.WATERLOGGED) && blockReader.getBlockState(pos).get(BlockStateProperties.WATERLOGGED));
    }
}

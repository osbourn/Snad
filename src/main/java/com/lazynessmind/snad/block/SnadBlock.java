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
    public int getDustColor(BlockState state) {
        return this.color;
    }


    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.tick(state, world, pos, random);

        Block blockAbove = world.getBlockState(pos.up()).getBlock();

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
            blockAbove.randomTick(world.getBlockState(pos.up()), world, pos.up(), random);
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader blockReader, BlockPos pos, Direction direction, IPlantable iPlantable) {
        final BlockPos plantPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        final PlantType plantType = iPlantable.getPlantType(blockReader, plantPos);

        switch (plantType) {
            case Desert: {
                return true;
            }
            case Water: {
                return blockReader.getBlockState(pos).getMaterial() == Material.WATER && blockReader.getBlockState(pos) == getDefaultState();
            }
            case Beach: {
                return ((blockReader.getBlockState(pos.east()).getMaterial() == Material.WATER || blockReader.getBlockState(pos.east()).has(BlockStateProperties.WATERLOGGED))
                        || (blockReader.getBlockState(pos.west()).getMaterial() == Material.WATER || blockReader.getBlockState(pos.west()).has(BlockStateProperties.WATERLOGGED))
                        || (blockReader.getBlockState(pos.north()).getMaterial() == Material.WATER || blockReader.getBlockState(pos.north()).has(BlockStateProperties.WATERLOGGED))
                        || (blockReader.getBlockState(pos.south()).getMaterial() == Material.WATER || blockReader.getBlockState(pos.south()).has(BlockStateProperties.WATERLOGGED)));
            }
        }
        return false;
    }
}

package com.lazynessmind.snad.common.block;

import com.lazynessmind.snad.common.item.ModItems;
import com.lazynessmind.snad.common.registry.ModConfigs;
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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.Random;

public class SnadBlock extends FallingBlock {

    private final int color;

    public SnadBlock(String name, int color, MaterialColor materialColor) {
        super(Block.Properties.create(Material.SAND, materialColor).tickRandomly().hardnessAndResistance(0.5f).sound(SoundType.SAND));

        this.color = color;

        setRegistryName(name);

        ModBlocks.BLOCKS.add(this);
        Item.Properties properties = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
        ModItems.ITEMS.add(new BlockItem(this, properties).setRegistryName(name));
    }


    @Override
    public int getDustColor(BlockState state) {
        return this.color;
    }



    @Override
    public void func_225534_a_(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.func_225534_a_(state, world, pos, random);

        Block blockAbove = world.getBlockState(pos.up()).getBlock();

        if (blockAbove instanceof SugarCaneBlock || blockAbove instanceof CactusBlock) {
            boolean isSameBlockType = true;
            int height = 1;

            while (isSameBlockType) {
                if (world.getBlockState(pos.up(height)).getBlock() != Blocks.AIR) {
                    Block nextPlantBlock = world.getBlockState(pos.up(height)).getBlock();
                    if (nextPlantBlock.getClass() == blockAbove.getClass()) {
                        for (int growthAttempts = 0; growthAttempts < ModConfigs.SPEED_INCREASE_DEFAULT_VALUE.get();
                             growthAttempts++) {
                            if (growthAttempts == 0 | canSustainPlant(world.getBlockState(pos), world, pos, null, (IPlantable) blockAbove)) {
                                nextPlantBlock.func_225534_a_(world.getBlockState(pos.up(height)), world, pos.up(height), random);
                            }
                        }
                        height++;
                    } else {
                        isSameBlockType = false;
                    }
                } else {
                    isSameBlockType = false;
                }
            }
        } else if (blockAbove instanceof IPlantable) {
            blockAbove.func_225534_a_(world.getBlockState(pos.up()), world, pos.up(), random);
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

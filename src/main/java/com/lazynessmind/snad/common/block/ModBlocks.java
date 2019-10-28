package com.lazynessmind.snad.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static List<Block> BLOCKS = new ArrayList<>();

    @ObjectHolder("snad:snad")
    public static SnadBlock SNAD = new SnadBlock("snad", -2370656, MaterialColor.SAND);

    @ObjectHolder("snad:red_snad")
    public static SnadBlock SNAD_RED = new SnadBlock("red_snad", -5679071, MaterialColor.ADOBE);
}

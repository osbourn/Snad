package com.lazynessmind.snad.proxy;

import com.lazynessmind.snad.Snad;
import com.lazynessmind.snad.block.SnadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonProxy {

    public static DeferredRegister<Block> BLOCKS = new DeferredRegister<Block>(ForgeRegistries.BLOCKS, Snad.MODID);
    public static DeferredRegister<Item> ITEMS = new DeferredRegister<Item>(ForgeRegistries.ITEMS, Snad.MODID);
    public static RegistryObject<SnadBlock> SNAD_BLOCK = BLOCKS.register("snad", () -> new SnadBlock("snad", -2370656, MaterialColor.SAND));
    public static RegistryObject<SnadBlock> RED_SNAD_BLOCK = BLOCKS.register("red_snad", () -> new SnadBlock("red_snad", -5679071, MaterialColor.ADOBE));

    public void init() {
        this.addToBus();
    }

    public void addToBus() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
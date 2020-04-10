package com.lazynessmind.snad.common;

import com.lazynessmind.snad.client.proxy.ClientProxy;
import com.lazynessmind.snad.common.interfaces.IProxy;
import com.lazynessmind.snad.common.registry.ModConfigs;
import com.lazynessmind.snad.server.proxy.ServerProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Const.MODID)
public class Snad {

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public Snad() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_CONFIG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModConfigs.load(ModConfigs.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("snad-client.toml"));
        ModConfigs.load(ModConfigs.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("snad-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        proxy.init();
    }
}

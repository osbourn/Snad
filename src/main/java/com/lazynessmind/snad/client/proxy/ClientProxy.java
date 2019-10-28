package com.lazynessmind.snad.client.proxy;

import com.lazynessmind.snad.common.interfaces.IProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {

    }

    @Override
    public World getClientWorld() {
        return null;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return null;
    }
}

package com.flashbackinventory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FlashbackInventoryAddon implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ReplayPayloadHandler.register();
    }
}

package io.github.atilist.hostilefactions.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class MessageBroadcast {
    public static void sendMessage(String message) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            if (FabricLoader.getInstance().getGameInstance() instanceof MinecraftServer server) {
                server.playerManager.broadcast(message);
            }
        } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (FabricLoader.getInstance().getGameInstance() instanceof Minecraft minecraft) {
                if (minecraft.player != null) {
                    minecraft.player.sendMessage(message);
                }
            }
        }
    }
}

package com.teampotato.quiet.network;

import com.teampotato.quiet.Quiet;
import com.teampotato.quiet.network.s2c.QuietDataS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Quiet.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        CHANNEL.registerMessage(packetId++, QuietDataS2C.class, QuietDataS2C::encode, QuietDataS2C::decode, QuietDataS2C::handle);
    }
}

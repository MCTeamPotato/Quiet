package com.teampotato.quiet.network;


import com.teampotato.quiet.network.s2c.QuietDataS2C;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {

    public static void register(RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar = event.registrar("1.0.0");
        registrar.playToClient(
                QuietDataS2C.TYPE,
                QuietDataS2C.STREAM_CODEC,
                QuietDataS2C::handle
        );
    }
}

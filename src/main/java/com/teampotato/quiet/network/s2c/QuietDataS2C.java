package com.teampotato.quiet.network.s2c;

import com.teampotato.quiet.util.QuietData;
import com.teampotato.quiet.util.QuietDataUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class QuietDataS2C {
    private final Component name;
    private final UUID uuid;
    private final byte strength;
    private final long timeStamp;

    public QuietDataS2C(QuietData quietData) {
        this.name = Component.literal(quietData.getName());
        this.uuid = quietData.getUuid();
        this.strength = quietData.getStrength();
        this.timeStamp = quietData.getTimeStamp();
    }

    public QuietDataS2C(Component name, UUID uuid, byte strength, long timeStamp) {
        this.name = name;
        this.uuid = uuid;
        this.strength = strength;
        this.timeStamp = timeStamp;
    }

    public static void encode(QuietDataS2C msg, FriendlyByteBuf buf) {
        buf.writeComponent(msg.name);
        buf.writeUUID(msg.uuid);
        buf.writeByte(msg.strength);
        buf.writeLong(msg.timeStamp);
    }

    public static QuietDataS2C decode(FriendlyByteBuf buf) {
        Component name = buf.readComponent();
        UUID uuid = buf.readUUID();
        byte strength = buf.readByte();
        long timeStamp = buf.readLong();
        return new QuietDataS2C(name, uuid, strength, timeStamp);
    }

    public static void handle(QuietDataS2C msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            QuietData quietData = new QuietData(msg.name.getString(), msg.uuid, msg.strength, msg.timeStamp);
            QuietDataUtil.addQuietData(quietData);
        });
        context.get().setPacketHandled(true);
    }
}

package com.teampotato.quiet.network.s2c;

import com.teampotato.quiet.Quiet;
import com.teampotato.quiet.util.QuietData;
import com.teampotato.quiet.util.QuietDataUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public record QuietDataS2C(String name, UUID uuid, byte strength, long timeStamp) implements CustomPacketPayload {

    public QuietDataS2C(QuietData quietData) {
        this(quietData.getName(), quietData.getUuid(), quietData.getStrength(), quietData.getTimeStamp());
    }

    public QuietDataS2C(String name, UUID uuid, byte strength, long timeStamp) {
        this.name = name;
        this.uuid = uuid;
        this.strength = strength;
        this.timeStamp = timeStamp;
    }

    public static final StreamCodec<ByteBuf,QuietDataS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,QuietDataS2C::name,
            UUIDUtil.STREAM_CODEC, QuietDataS2C::uuid,
            ByteBufCodecs.BYTE, QuietDataS2C::strength,
            ByteBufCodecs.VAR_LONG, QuietDataS2C::timeStamp,
            QuietDataS2C::new
    );

    public static void handle(QuietDataS2C msg, IPayloadContext context) {
        context.enqueueWork(() -> {
            QuietData quietData = new QuietData(msg.name, msg.uuid, msg.strength, msg.timeStamp);
            QuietDataUtil.addQuietData(quietData);
        });

    }

    public static final Type<QuietDataS2C> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Quiet.MODID,"quiet_data"));

    @Override
    public @NotNull Type<QuietDataS2C> type() {
        return TYPE;
    }
}

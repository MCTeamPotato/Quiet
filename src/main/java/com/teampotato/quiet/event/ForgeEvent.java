package com.teampotato.quiet.event;

import com.teampotato.quiet.Quiet;
import com.teampotato.quiet.command.QuietCommand;
import com.teampotato.quiet.util.QuietData;
import com.teampotato.quiet.util.QuietDataUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Quiet.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        QuietCommand.register(event.getDispatcher());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void cancelSendChat(ClientChatEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer sender = minecraft.player;
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        byte size = QuietDataUtil.dataFind(sender.getUUID(), sender.getScoreboardName());

        if (size == -1) return;
        QuietData quietData = quietDataList.get(size);
        if (!quietData.timeOver(minecraft.level)) return;

        byte strength = quietData.getStrength();
        if (strength >= 4) {
            minecraft.player.sendSystemMessage(Component.translatable("message.quiet.quiet"));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void hideChat(ClientChatReceivedEvent.Player event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer receiver = minecraft.player;
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        byte size = QuietDataUtil.dataFind(event.getSender());

        if (size == -1) return;
        QuietData quietData = quietDataList.get(size);
        if (!quietData.timeOver(minecraft.level) || quietData.sameUuid(receiver)) return;

        byte strength = quietData.getStrength();
        if (strength >= 2) {
            event.setCanceled(true);
        } else if (strength == 1) {
            String originText = "<%s> ".formatted(quietData.getName()) + event.getPlayerChatMessage().signedContent();
            Style style = Style.EMPTY
                    .withHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Component.translatable("message.quiet.click_show")
                    ))
                    .withClickEvent(new ClickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND,
                            originText
                    ));
            event.setMessage(Component.translatable("message.quiet.hide").setStyle(style));
        }
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public static void cancelServerChat(ServerChatEvent event) {
        ServerPlayer serverPlayer = event.getPlayer();
        ServerLevel serverLevel = serverPlayer.serverLevel();
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        byte size = QuietDataUtil.dataFind(serverPlayer.getUUID(), serverPlayer.getScoreboardName());

        if (size == -1) return;
        QuietData quietData = quietDataList.get(size);

        if (!quietData.timeOver(serverLevel)) return;
        if (quietData.getStrength() >= 3) {
            event.setCanceled(true);
        }
    }
}

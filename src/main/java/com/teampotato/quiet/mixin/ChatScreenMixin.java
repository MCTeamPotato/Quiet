package com.teampotato.quiet.mixin;

import com.teampotato.quiet.util.QuietData;
import com.teampotato.quiet.util.QuietDataUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Shadow private String initial;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initialChat(String pInitial, CallbackInfo ci){
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer sender = minecraft.player;
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        byte size = QuietDataUtil.dataFind(sender.getUUID(), sender.getScoreboardName());

        if (size == -1) return;
        QuietData quietData = quietDataList.get(size);
        if (!quietData.timeOver(minecraft.level)) return;

        byte strength = quietData.getStrength();
        if (strength >= 5) {
            this.initial = "禁言中";
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void cancelPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir){
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer sender = minecraft.player;
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        byte size = QuietDataUtil.dataFind(sender.getUUID(), sender.getScoreboardName());

        if (size == -1) return;
        QuietData quietData = quietDataList.get(size);
        if (!quietData.timeOver(minecraft.level)) return;

        byte strength = quietData.getStrength();
        if (strength >= 5) {
            if (pKeyCode == 256) {
                minecraft.setScreen(null);
            }
            cir.setReturnValue(true);
        }
    }
}

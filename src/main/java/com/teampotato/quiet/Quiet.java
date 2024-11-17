package com.teampotato.quiet;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import com.teampotato.quiet.network.NetworkHandler;
import com.teampotato.quiet.util.QuietData;
import com.teampotato.quiet.util.QuietDataUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod(Quiet.MODID)
public class Quiet {
    public static final String MODID = "quiet";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Quiet() {
        NetworkHandler.register();
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        if (quietDataList == null) {
            quietDataList = new ArrayList<>();
            QuietDataUtil.saveQuietData(quietDataList);
        }
    }
}

package com.teampotato.quiet;

import com.mojang.logging.LogUtils;
import com.teampotato.quiet.network.NetworkHandler;
import com.teampotato.quiet.util.QuietData;
import com.teampotato.quiet.util.QuietDataUtil;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Quiet.MODID)
public class Quiet {
    public static final String MODID = "quiet";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Quiet(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(NetworkHandler::register);
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        if (quietDataList == null) {
            quietDataList = new ArrayList<>();
            QuietDataUtil.saveQuietData(quietDataList);
        }
    }
}

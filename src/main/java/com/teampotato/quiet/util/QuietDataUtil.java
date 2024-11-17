package com.teampotato.quiet.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuietDataUtil {
    private static final Gson gson = new Gson();

    public static void addQuietData(QuietData quietData) {
        List<QuietData> quietDataList = loadQuietData();
        byte size = dataFind(quietDataList, quietData.getUuid(), quietData.getName());

        if (size != -1) {
            quietDataList.get(size).setName(quietData.getName());
            quietDataList.get(size).setUuid(quietData.getUuid());
            quietDataList.get(size).setStrength(quietData.getStrength());
            quietDataList.get(size).setTimeStamp(quietData.getTimeStamp());
        } else {
            quietDataList.add(quietData);
        }
        saveQuietData(quietDataList);
    }

    public static byte dataFind(String name) {
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        return dataFind(quietDataList, null, name);
    }

    public static byte dataFind(UUID compare) {
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        return dataFind(quietDataList, compare, null);
    }

    public static byte dataFind(List<QuietData> quietDataList, String name) {
        return dataFind(quietDataList, null, name);
    }

    public static byte dataFind(List<QuietData> quietDataList, UUID compare) {
        return dataFind(quietDataList, compare, null);
    }

    public static byte dataFind(UUID compare, String name) {
        List<QuietData> quietDataList = QuietDataUtil.loadQuietData();
        return dataFind(quietDataList, compare, name);
    }

    public static byte dataFind(List<QuietData> quietDataList, UUID compare, String name) {
        byte find = -1;
        for (byte i = 0; i < quietDataList.size(); i++) {
            if (quietDataList.get(i).getName().equals(name)) {
                find = i;
                break;
            }
            if (quietDataList.get(i).getUuid().equals(compare)) {
                find = i;
                break;
            }
        }
        return find;
    }


    public static void saveQuietData(List<QuietData> quietData) {
        File gameDir = new File(".");
        File file = new File(gameDir, "quiet_list.json");

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(quietData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<QuietData> loadQuietData() {
        File gameDir = new File(".");
        Type listType = new TypeToken<List<QuietData>>() {}.getType();
        File file = new File(gameDir, "quiet_list.json");

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                return gson.fromJson(reader, listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<QuietData> quietDataList = new ArrayList<>();
        QuietDataUtil.saveQuietData(quietDataList);
        return quietDataList;
    }
}

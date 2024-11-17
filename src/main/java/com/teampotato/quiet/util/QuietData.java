package com.teampotato.quiet.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class QuietData {
    private String name;
    private UUID uuid;
    private byte strength;
    private long timeStamp;
    public QuietData(String name, UUID uuid, byte strength, long timeStamp) {
        this.name = name;
        this.uuid = uuid;
        this.strength = strength;
        this.timeStamp = timeStamp;
    }
    public static QuietData getQuietData() {
        return new QuietData(null, null, (byte) 0, 0);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean sameUuid(Player compare) {
        return this.uuid.equals(compare.getUUID());
    }

    public byte getStrength() {
        return strength;
    }

    public void setStrength(byte strength) {
        this.strength = strength;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean timeOver(Level level) {
        long gameTime = level.getGameTime();
        return gameTime < timeStamp;
    }
}

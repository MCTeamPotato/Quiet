package com.teampotato.quiet.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.teampotato.quiet.network.NetworkHandler;
import com.teampotato.quiet.network.s2c.QuietDataS2C;
import com.teampotato.quiet.util.QuietData;
import com.teampotato.quiet.util.QuietDataUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;

public class QuietCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("quiet").requires((sourceStack) -> sourceStack.hasPermission(0))
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.players()).executes(ctx -> {
                                            return quietPlayer(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (byte) 3, 30);
                                        })
                                        .then(Commands.argument("time", TimeArgument.time()).executes(ctx -> {
                                                            return quietPlayer(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (byte) 3, IntegerArgumentType.getInteger(ctx, "time"));
                                                        })
                                                        .then(Commands.literal("LOWEST").executes(ctx -> {
                                                                    return quietPlayer(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (byte) 1, IntegerArgumentType.getInteger(ctx, "time"));
                                                                })
                                                        )
                                                        .then(Commands.literal("LOW").executes(ctx -> {
                                                                    return quietPlayer(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (byte) 2, IntegerArgumentType.getInteger(ctx, "time"));
                                                                })
                                                        )
                                                        .then(Commands.literal("NORMAL").executes(ctx -> {
                                                                    return quietPlayer(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (byte) 3, IntegerArgumentType.getInteger(ctx, "time"));
                                                                }).requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                                                        )
                                                        .then(Commands.literal("HIGH").executes(ctx -> {
                                                                    return quietPlayer(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (byte) 4, IntegerArgumentType.getInteger(ctx, "time"));
                                                                }).requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                                                        )
                                                        .then(Commands.literal("HIGHEST").executes(ctx -> {
                                                                    return quietPlayer(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (byte) 5, IntegerArgumentType.getInteger(ctx, "time"));
                                                                }).requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                                                        )
                                        )
                        )
                )
        );
    }

    public static int quietPlayer(CommandSourceStack commandSource, Collection<ServerPlayer> players, byte strength, float time) {
        ServerLevel serverLevel = commandSource.getLevel();
        players.forEach(serverPlayer -> {
            QuietData quietData = new QuietData(serverPlayer.getScoreboardName(), serverPlayer.getUUID(), strength, (long) (serverLevel.getGameTime() + time));
            QuietDataUtil.addQuietData(quietData);
            PacketDistributor.sendToPlayer(serverPlayer, new QuietDataS2C(quietData));
            if (strength >= 3) serverPlayer.sendSystemMessage(Component.translatable("message.quiet.mute", time / 20));
        });
        return 0;
    }
}

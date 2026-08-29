package de.tsgscraft.advancedclothing.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.tsgscraft.advancedclothing.client.screen.ClothingSelectionScreen;
import de.tsgscraft.advancedclothing.network.SetClothingPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClothingCommand {
    public static final LiteralArgumentBuilder<CommandSourceStack> Command = Commands.literal("advancedclothing")
            .then(Commands.literal("setmap")
                    .then(Commands.argument("key", StringArgumentType.string())
                            .suggests((context, builder) -> {
                                for (String key : ClothingRegistry.getInstance().getClothingTypes()) {
                                    builder.suggest("\"" + key + "\"");
                                }
                                return builder.buildFuture();
                            })
                            .then(Commands.argument("value", StringArgumentType.string())
                                    .suggests((context, builder) -> {
                                        String key = StringArgumentType.getString(context, "key");
                                        for (String value : ClothingRegistry.getInstance().getClothingTypeToIdMap().get(key)) {
                                            builder.suggest("\"" + value + "\"");
                                        }
                                        return builder.buildFuture();
                                    })
                                    .executes(context -> {
                                        if (context.getSource().getEntity() instanceof Player) {
                                            String key = StringArgumentType.getString(context, "key");
                                            String value = StringArgumentType.getString(context, "value");
                                            PacketDistributor.sendToServer(
                                                    new SetClothingPayload(key, value)
                                            );
                                        }
                                        return 1;
                                    })
                            )
                    )
            ).then(Commands.literal("removemap")
                    .then(Commands.argument("key", StringArgumentType.string())
                            .suggests((context, builder) -> {
                                for (String key : ClothingRegistry.getInstance().getClothingTypes()) {
                                    builder.suggest("\"" + key + "\"");
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                if (context.getSource().getEntity() instanceof Player) {
                                    String key = StringArgumentType.getString(context, "key");
                                    PacketDistributor.sendToServer(
                                            new SetClothingPayload(key, "")
                                    );
                                }
                                return 1;
                            })
                    )
            ).then(Commands.literal("screen")
                    .executes(context -> {
                        Minecraft.getInstance().setScreen(new ClothingSelectionScreen());
                        return 1;
                    })
            );
}

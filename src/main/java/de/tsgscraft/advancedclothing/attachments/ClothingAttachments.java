package de.tsgscraft.advancedclothing.attachments;

import com.mojang.serialization.Codec;
import de.tsgscraft.advancedclothing.REFERENCE;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClothingAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(
                    NeoForgeRegistries.ATTACHMENT_TYPES,
                    REFERENCE.MODID
            );

    public static final Codec<Map<String, String>> CLOTHING_DATA_CODEC =
            Codec.unboundedMap(
                    Codec.STRING,
                    Codec.STRING
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, String>>
            CLOTHING_DATA_STREAM_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.STRING_UTF8,
                    ByteBufCodecs.STRING_UTF8
            );

    public static final Supplier<AttachmentType<Map<String, String>>> CLOTHING_DATA =
            ATTACHMENTS.register(
                    "clothing_data",
                    () -> AttachmentType.<Map<String, String>>builder(
                                    () -> new HashMap<>()
                            )
                            .serialize(CLOTHING_DATA_CODEC)
                            .sync(CLOTHING_DATA_STREAM_CODEC)
                            .copyOnDeath()
                            .build()
            );
}
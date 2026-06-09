package net.pawjwp.varkin_system.mixin;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.pawjwp.varkin_system.VarkinSystemRemapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

// Redirect block state parser to return remapped blocks
@Mixin(BlockStateParser.class)
public class BlockStateParserMixin {
    @Redirect(
            method = "readBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/HolderLookup;get(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;")
    )
    private Optional<Holder.Reference<Block>> varkin_system$remapLegacyBlock(
            HolderLookup<Block> lookup, ResourceKey<Block> key) {
        Optional<Holder.Reference<Block>> result = lookup.get(key);
        if (result.isEmpty()) {
            ResourceLocation remapped = VarkinSystemRemapping.remap(key.location());
            if (remapped != null) {
                return lookup.get(ResourceKey.create(Registries.BLOCK, remapped));
            }
        }
        return result;
    }
}

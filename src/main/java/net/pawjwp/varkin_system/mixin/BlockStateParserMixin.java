package net.pawjwp.varkin_system.mixin;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
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
        // HolderLookup extends HolderGetter, so the shared helper applies
        return VarkinSystemRemapping.getOrRemap(lookup, key);
    }
}

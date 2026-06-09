package net.pawjwp.varkin_system.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.pawjwp.varkin_system.VarkinSystemRemapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

// Replaces NBT reads with remapped block IDs
@Mixin(NbtUtils.class)
public class NbtUtilsMixin {
    @Redirect(
            method = "readBlockState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/HolderGetter;get(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;")
    )
    private static Optional<Holder.Reference<Block>> varkin_system$remapLegacyBlock(
            HolderGetter<Block> getter, ResourceKey<Block> key) {
        Optional<Holder.Reference<Block>> result = getter.get(key);
        if (result.isEmpty()) {
            ResourceLocation remapped = VarkinSystemRemapping.remap(key.location());
            if (remapped != null) {
                return getter.get(ResourceKey.create(Registries.BLOCK, remapped));
            }
        }
        return result;
    }
}

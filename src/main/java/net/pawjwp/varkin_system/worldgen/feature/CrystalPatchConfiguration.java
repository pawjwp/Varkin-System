package net.pawjwp.varkin_system.worldgen.feature;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Configuration for the crystal patch feature.
 *
 * @param targets        Each entry's rule test determines which blocks budding
 *                       blocks can replace, and the state is the budding block
 *                       to place, handled in the same way as ores
 * @param crystalSize    Number of standalone crystal cluster spawn attempts
 * @param buddingSize    Number of budding block spawn attempts
 * @param xzSpread       Horizontal spread radius from origin
 * @param ySpread        Vertical spread radius from origin
 * @param smallCrystal   Block used for small buds on budding block faces
 * @param mediumCrystal  Block used for medium buds on budding block faces
 * @param largeCrystal   Block used for large buds on budding block faces
 * @param crystalCluster Block used for standalone crystal clusters
 */

public record CrystalPatchConfiguration(
                List<OreConfiguration.TargetBlockState> targets,
                int crystalSize,
                int buddingSize,
                int xzSpread,
                int ySpread,
                Optional<Block> smallCrystal,
                Optional<Block> mediumCrystal,
                Optional<Block> largeCrystal,
                Optional<Block> crystalCluster) implements FeatureConfiguration {

        public static final Codec<CrystalPatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance
                        .group(
                                        Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(CrystalPatchConfiguration::targets),
                                        Codec.intRange(0, 64).fieldOf("crystal_size").forGetter(CrystalPatchConfiguration::crystalSize),
                                        Codec.intRange(0, 64).fieldOf("budding_size").forGetter(CrystalPatchConfiguration::buddingSize),
                                        Codec.intRange(0, 16).fieldOf("xz_spread").forGetter(CrystalPatchConfiguration::xzSpread),
                                        Codec.intRange(0, 16).fieldOf("y_spread").forGetter(CrystalPatchConfiguration::ySpread),
                                        ForgeRegistries.BLOCKS.getCodec().optionalFieldOf("small_crystal").forGetter(CrystalPatchConfiguration::smallCrystal),
                                        ForgeRegistries.BLOCKS.getCodec().optionalFieldOf("medium_crystal").forGetter(CrystalPatchConfiguration::mediumCrystal),
                                        ForgeRegistries.BLOCKS.getCodec().optionalFieldOf("large_crystal").forGetter(CrystalPatchConfiguration::largeCrystal),
                                        ForgeRegistries.BLOCKS.getCodec().optionalFieldOf("crystal_cluster").forGetter(CrystalPatchConfiguration::crystalCluster))
                        .apply(instance, CrystalPatchConfiguration::new));
}

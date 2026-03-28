package net.pawjwp.varkin_system.worldgen.surfacerules;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.pawjwp.varkin_system.mixin.SurfaceRulesContextAccessor;
import net.pawjwp.varkin_system.mixin.SurfaceSystemAccessor;

import java.util.Arrays;
import java.util.WeakHashMap;

public enum VolcanicBandlands implements SurfaceRules.RuleSource {
    INSTANCE;

    static final KeyDispatchDataCodec<VolcanicBandlands> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));

    public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
        return CODEC;
    }

    private static final WeakHashMap<SurfaceSystem, BandData> CACHE = new WeakHashMap<>();
    private record BandData(BlockState[] volcanicBands, NormalNoise volcanicBandsOffsetNoise) {}

    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context pContext) {
        SurfaceRulesContextAccessor accessor = (SurfaceRulesContextAccessor) (Object) pContext;
        SurfaceSystem surfacesystem = accessor.getSystem();
        RandomState randomstate = accessor.getRandomState();

        BandData banddata = CACHE.get(surfacesystem);
        if (banddata == null) {
            PositionalRandomFactory positionalrandomfactory = ((SurfaceSystemAccessor) surfacesystem).getNoiseRandom();
            RandomSource randomsource = positionalrandomfactory.fromHashOf(
                    ResourceLocation.fromNamespaceAndPath("varkin_system", "volcanic_bands")
            );
            BlockState[] ablockstate = generateVolcanicBands(randomsource);
            NormalNoise normalnoise = randomstate.getOrCreateNoise(Noises.CLAY_BANDS_OFFSET);
            banddata = new BandData(ablockstate, normalnoise);
            CACHE.put(surfacesystem, banddata);
        }

        final BlockState[] volcanicBands = banddata.volcanicBands;
        final NormalNoise volcanicBandsOffsetNoise = banddata.volcanicBandsOffsetNoise;

        return (pX, pY, pZ) -> {
            int i = (int) Math.round(volcanicBandsOffsetNoise.getValue(pX, 0.0, pZ) * 4.0);
            return volcanicBands[(pY + i + volcanicBands.length) % volcanicBands.length];
        };
    }

    private static final BlockState BLACKSTONE    = Blocks.BLACKSTONE.defaultBlockState();
    private static final BlockState SMOOTH_BASALT = Blocks.SMOOTH_BASALT.defaultBlockState();
    private static final BlockState ANDESITE      = Blocks.ANDESITE.defaultBlockState();
    private static final BlockState TUFF          = Blocks.TUFF.defaultBlockState();

    private static BlockState conditionalModBlock(String pNamespace, String pPath) {
        if (!ModList.get().isLoaded(pNamespace)) return null;
        Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.fromNamespaceAndPath(pNamespace, pPath));
        return block != null ? block.defaultBlockState() : null;
    }


    private static BlockState[] generateVolcanicBands(RandomSource pRandom) {
        // Mod-conditional blocks resolved here, after all mods have registered their blocks
        BlockState searedstone   = conditionalModBlock("tconstruct", "seared_stone");
        BlockState scorchedstone = conditionalModBlock("tconstruct", "scorched_stone");
        BlockState slagblock     = conditionalModBlock("thermal",    "slag_block");
        BlockState richslagblock = conditionalModBlock("thermal",    "rich_slag_block");
        BlockState eruptionrock  = conditionalModBlock("create",     "scoria");
        BlockState eruptioncrust = conditionalModBlock("create",     "scorchia");
        if (eruptionrock  == null) eruptionrock  = Blocks.SOUL_SAND.defaultBlockState();
        if (eruptioncrust == null) eruptioncrust = Blocks.COAL_BLOCK.defaultBlockState();

        int layerCount = 192;
        BlockState[] ablockstate = new BlockState[layerCount];
        Arrays.fill(ablockstate, BLACKSTONE);

        makeBands(pRandom, ablockstate, 1, 3, SMOOTH_BASALT, 8, 12);
        makeBands(pRandom, ablockstate, 1, 3, ANDESITE, 6, 10);
        if (searedstone   != null) makeBands(pRandom, ablockstate, 1, 2, searedstone,   1, 1);
        if (scorchedstone != null) makeBands(pRandom, ablockstate, 1, 1, scorchedstone, 1, 1);
        if (slagblock     != null) makeBands(pRandom, ablockstate, 1, 1, slagblock,     1, 2);
        if (richslagblock != null) makeBands(pRandom, ablockstate, 1, 1, richslagblock, 0, 1);

        // random eruption count
        int i = pRandom.nextIntBetweenInclusive(8, 12);
        // eruption layer distance
        int eruptionMinDistance = (layerCount/i)-8;
        int eruptionMaxDistance = (layerCount/i)+8;
        int j = 0;

        // randomly places eruption layer patterns
        for (int currentBand = 0; j < i && currentBand < ablockstate.length; currentBand += pRandom.nextIntBetweenInclusive(eruptionMinDistance, eruptionMaxDistance)) {
            // randomly places scoria or scorchia as the starting layer
            ablockstate[currentBand] = pRandom.nextBoolean() ? eruptionrock : eruptioncrust;

            int top = currentBand;
            int bottom = currentBand;

            // 50% chance to place scorchia above, iterates top
            if (currentBand + 1 < ablockstate.length && pRandom.nextBoolean()) {
                ablockstate[currentBand + 1] = eruptioncrust;
                top = currentBand + 1;
            }

            // 50% chance to place scorchia below, iterates bottom
            if (currentBand - 1 >= 0 && pRandom.nextBoolean()) {
                ablockstate[currentBand - 1] = eruptioncrust;
                bottom = currentBand - 1;
            }

            // places 1-2 tuff blocks above the top
            int tuffCount = 1 + pRandom.nextInt(2);
            for (int k1 = 1; k1 <= tuffCount; k1++) {
                if (top + k1 < ablockstate.length) {
                    ablockstate[top + k1] = TUFF;
                }
            }

            // 25% chance of placing 1-2 layers of smooth basalt or andesite
            if (pRandom.nextInt(4) == 0) {
                int l1 = 1 + pRandom.nextInt(2);
                BlockState blockstate = pRandom.nextBoolean() ? SMOOTH_BASALT : ANDESITE;
                for (int i2 = 1; i2 <= l1; i2++) {
                    if (bottom - i2 >= 0) {
                        ablockstate[bottom - i2] = blockstate;
                    }
                }
            }
            ++j;
        }

        return ablockstate;
    }

    private static void makeBands(RandomSource pRandom, BlockState[] pBands, int pMinSize, int pMaxSize, BlockState pState, int pMinCount, int pMaxCount) {
        int i = pRandom.nextIntBetweenInclusive(pMinCount, pMaxCount);
        for (int j = 0; j < i; ++j) {
            int k = pRandom.nextIntBetweenInclusive(pMinSize, pMaxSize);
            int l = pRandom.nextInt(pBands.length);
            for (int i1 = 0; l + i1 < pBands.length && i1 < k; ++i1) {
                pBands[l + i1] = pState;
            }
        }
    }
}

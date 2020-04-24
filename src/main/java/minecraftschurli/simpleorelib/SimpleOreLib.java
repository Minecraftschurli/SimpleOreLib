package minecraftschurli.simpleorelib;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 * @author Minecraftschurli
 * @version 2020-01-20
 */
@Mod(SimpleOreLib.MODID)
public final class SimpleOreLib {
    public static final String MODID = "simpleorelib";

    static final List<Ore> ORES = new ArrayList<>();

    public static OreFeatureConfig.FillerBlockType END_STONE = OreFeatureConfig.FillerBlockType.create("END_STONE", "end_stone", new BlockMatcher(Blocks.END_STONE));

    private static final Map<BiomeFilter, OreFeatureConfig.FillerBlockType> fillers = new HashMap<>(new ImmutableMap.Builder<BiomeFilter, OreFeatureConfig.FillerBlockType>().put(BiomeFilter.NETHER, OreFeatureConfig.FillerBlockType.NETHERRACK).put(BiomeFilter.THE_END, END_STONE).build());


    public SimpleOreLib() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(this::registerOres);
    }

    private void registerOres() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            for (Ore ore : ORES) {
                if (ore.hasBiomeFilter() && !ore.getBiomeFilter().apply(biome)) continue;

                CountRangeConfig placementConfig = new CountRangeConfig(
                        ore.getChance(),
                        ore.getMinHeight(),
                        ore.getMinHeight(),
                        ore.getMaxHeight());

                ConfiguredPlacement<CountRangeConfig> placement = Placement.COUNT_RANGE.func_227446_a_(placementConfig);

                OreFeatureConfig featureConfig = new OreFeatureConfig(
                        fillerForBiome(biome),
                        ore.getGenerationState(biome),
                        ore.getVeinSize());

                ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(featureConfig).func_227228_a_(placement);

                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
            }
        }
    }

    /**
     * Registeres a {@link net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType} for the given {@link BiomeFilter}
     *
     * @param filter the {@link BiomeFilter} to register the {@link net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType filler} for
     * @param filler the {@link net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType filler} to register
     */
    public static void addFiller(BiomeFilter filter, OreFeatureConfig.FillerBlockType filler) {
        fillers.putIfAbsent(filter, filler);
    }

    /**
     * Gets the {@link net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType filler} for the given {@link Biome}
     *
     * @param biome the {@link Biome} to get the {@link net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType filler} for
     * @return the {@link net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType filler} for the given {@link Biome}
     */
    public static OreFeatureConfig.FillerBlockType fillerForBiome(Biome biome) {
        return fillers.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparingInt(BiomeFilter::getPriority)))
                .filter(entry -> entry.getKey().test(biome))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(OreFeatureConfig.FillerBlockType.NATURAL_STONE);
    }
}

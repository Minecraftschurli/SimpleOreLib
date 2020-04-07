package minecraftschurli.simpleorelib;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.archivers.zip.UnsupportedZipFeatureException;

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


    public SimpleOreLib (){
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ForgeRegistries.BIOMES.forEach(biome -> {
            for (Ore ore : ORES) {
                if (ore.hasBiomeFilter() && !ore.getBiomeFilter().apply(biome))
                    continue;
                biome.addFeature(
                        GenerationStage.Decoration.UNDERGROUND_ORES,
                        Feature.ORE
                                .withConfiguration(
                                        new OreFeatureConfig(
                                                fillerForBiome(biome),
                                                ore.getGenerationState(biome),
                                                ore.getVeinSize()))
                                .func_227228_a_(
                                        Placement.COUNT_RANGE
                                                .func_227446_a_(
                                                        new CountRangeConfig(
                                                                ore.getChance(),
                                                                ore.getMinHeight(),
                                                                ore.getMinHeight(),
                                                                ore.getMaxHeight()))));
            }
        });
    }

    public static void addFiller(BiomeFilter filter, OreFeatureConfig.FillerBlockType filler){
        fillers.putIfAbsent(filter, filler);
    }

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

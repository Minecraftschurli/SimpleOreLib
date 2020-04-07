package minecraftschurli.simpleorelib;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;

import static minecraftschurli.simpleorelib.SimpleOreLib.ORES;

/**
 * @author Minecraftschurli
 * @version 2019-10-15
 */
public class Ore extends Block {

    private BiomeFilter biomeFilter;
    private int veinSize;
    private int chance;
    private int minHeight;
    private int maxHeight;

    public Ore(Properties properties, OreSpawnProperties oreProperties) {
        super(properties);

        this.veinSize = oreProperties.veinSize;
        this.chance = oreProperties.chance;
        this.minHeight = oreProperties.minHeight;
        this.maxHeight = oreProperties.maxHeight;
        this.biomeFilter = oreProperties.biomeFilter;

        ORES.add(this);
    }

    public int getVeinSize() {
        return veinSize;
    }

    public int getChance() {
        return chance;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public boolean hasBiomeFilter() {
        return this.biomeFilter != BiomeFilter.ALL;
    }

    public BiomeFilter getBiomeFilter() {
        return biomeFilter;
    }

    public BlockState getGenerationState(Biome biome) {
        return getDefaultState();
    }

    public static class OreSpawnProperties {
        private BiomeFilter biomeFilter;
        private int veinSize;
        private int chance;
        private int minHeight;
        private int maxHeight;

        private OreSpawnProperties(int veinSize, int chance, int minHeight, int maxHeight) {
            this.veinSize = veinSize;
            this.chance = chance;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.biomeFilter = BiomeFilter.ALL;
        }

        public static OreSpawnProperties create(int veinSize, int chance, int minHeight, int maxHeight) {
            return new OreSpawnProperties(veinSize, chance, minHeight, maxHeight);
        }

        public OreSpawnProperties addFilter(BiomeFilter filter) {
            if (this.biomeFilter == BiomeFilter.ALL) {
                this.biomeFilter = filter;
            } else {
                this.biomeFilter = this.biomeFilter.and(filter);
            }
            return this;
        }
    }

}

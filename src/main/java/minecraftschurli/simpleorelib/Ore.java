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

    private final BiomeFilter biomeFilter;
    private final int veinSize;
    private final int chance;
    private final int minHeight;
    private final int maxHeight;

    /**
     * Creates a new ore block with the given {@link Block.Properties} and {@link OreSpawnProperties}
     * @param properties    the properties of this {@link Block}
     * @param oreProperties the spawning properties for this {@link Ore}
     */
    public Ore(Properties properties, OreSpawnProperties oreProperties) {
        super(properties);

        this.veinSize = oreProperties.veinSize;
        this.chance = oreProperties.chance;
        this.minHeight = oreProperties.minHeight;
        this.maxHeight = oreProperties.maxHeight;
        this.biomeFilter = oreProperties.biomeFilter;

        ORES.add(this);
    }

    /**
     * Gets the vein size for this {@link Ore}
     * @return the vein size for this {@link Ore}
     */
    public int getVeinSize() {
        return veinSize;
    }

    /**
     * Gets the spawn chance for this {@link Ore}
     * @return the spawn chance for this {@link Ore}
     */
    public int getChance() {
        return chance;
    }

    /**
     * Gets the minimum height this {@link Ore} should spawn at
     * @return the minimum height this {@link Ore} should spawn at
     */
    public int getMinHeight() {
        return minHeight;
    }

    /**
     * Gets the maximum height this {@link Ore} should spawn at
     * @return the maximum height this {@link Ore} should spawn at
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Checks if this {@link Ore} has a {@link BiomeFilter}
     * @return {@code true} if this {@link Ore} has a {@link BiomeFilter}
     */
    public boolean hasBiomeFilter() {
        return this.biomeFilter != BiomeFilter.ALL;
    }

    /**
     * Gets the {@link BiomeFilter} for this {@link Ore}
     * @return the {@link BiomeFilter} for this {@link Ore}
     */
    public BiomeFilter getBiomeFilter() {
        return biomeFilter;
    }

    /**
     * Gets the {@link BlockState} that should spawn in the given biome
     * @param biome the biome for which to get the {@link BlockState} for
     * @return the {@link BlockState} to generate in the given {@link Biome}; defaults to {@link Block#getDefaultState()}
     */
    public BlockState getGenerationState(Biome biome) {
        return getDefaultState();
    }

    /**
     * A class that holds information for spawning the ores
     */
    public static class OreSpawnProperties {
        private BiomeFilter biomeFilter;
        private final int veinSize;
        private final int chance;
        private final int minHeight;
        private final int maxHeight;

        private OreSpawnProperties(int veinSize, int chance, int minHeight, int maxHeight) {
            this.veinSize = veinSize;
            this.chance = chance;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.biomeFilter = BiomeFilter.ALL;
        }

        /**
         * Factory method for creating new {@link OreSpawnProperties}
         * @param veinSize  the vein size for the ores that this {@link OreSpawnProperties} instance is passed to
         * @param chance    the spawn chance for the ores that this {@link OreSpawnProperties} instance is passed to
         * @param minHeight the minimum height the ores that this {@link OreSpawnProperties} instance is passed to should spawn at
         * @param maxHeight the maximum height the ores that this {@link OreSpawnProperties} instance is passed to should spawn at
         * @return a new {@link OreSpawnProperties} instance
         */
        public static OreSpawnProperties create(int veinSize, int chance, int minHeight, int maxHeight) {
            return new OreSpawnProperties(veinSize, chance, minHeight, maxHeight);
        }

        /**
         * Adds a given {@link BiomeFilter} to this {@link OreSpawnProperties} instance to filter in which {@link Biome} the ores that this {@link OreSpawnProperties} instance is passed to should spawn in
         * @param filter a {@link BiomeFilter} to add to this {@link OreSpawnProperties} instance
         * @param useAnd {@code true} if the given filter should use the "and" method to add.
         *               {@code false} if the given filter should use the "or" method to add.
         * @return this {@link OreSpawnProperties} instance
         */
        public OreSpawnProperties addFilter(BiomeFilter filter, boolean useAnd) {
            if (this.biomeFilter == BiomeFilter.ALL) {
                this.biomeFilter = filter;
            } else {
                if (useAnd) {
                    this.biomeFilter = this.biomeFilter.and(filter);
                } else {
                    this.biomeFilter = this.biomeFilter.or(filter);
                }
            }
            return this;
        }
    }

}

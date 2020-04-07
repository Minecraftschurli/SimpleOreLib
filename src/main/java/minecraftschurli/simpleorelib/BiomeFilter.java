package minecraftschurli.simpleorelib;

import com.google.common.base.Predicates;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Minecraftschurli
 * @version 2020-01-18
 */
public class BiomeFilter implements Predicate<Biome>, Function<Biome, Boolean> {
    public static final BiomeFilter ALL = new BiomeFilter(-1);

    public static final BiomeFilter OCEAN = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN));
    public static final BiomeFilter RIVER = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER));
    public static final BiomeFilter SANDY = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY));
    public static final BiomeFilter NETHER = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER));
    public static final BiomeFilter OVERWORLD = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD));
    public static final BiomeFilter THE_END = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.END));

    public static final BiomeFilter DESERT = new BiomeFilter(
            Biomes.DESERT,
            Biomes.DESERT_HILLS,
            Biomes.DESERT_LAKES
    );

    public static final BiomeFilter BADLANDS = new BiomeFilter(
            Biomes.BADLANDS,
            Biomes.BADLANDS_PLATEAU,
            Biomes.ERODED_BADLANDS,
            Biomes.MODIFIED_BADLANDS_PLATEAU,
            Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU,
            Biomes.WOODED_BADLANDS_PLATEAU
    );
    public static final BiomeFilter MOUNTAINS = new BiomeFilter(
            Biomes.MOUNTAINS,
            Biomes.SNOWY_MOUNTAINS,
            Biomes.WOODED_MOUNTAINS,
            Biomes.GRAVELLY_MOUNTAINS,
            Biomes.TAIGA_MOUNTAINS,
            Biomes.SNOWY_TAIGA_MOUNTAINS,
            Biomes.MODIFIED_GRAVELLY_MOUNTAINS
    );

    private final Predicate<Biome> predicate;
    private int priority;

    public BiomeFilter(Biome... biomes) {
        this(Predicates.in(Arrays.asList(biomes)));
    }

    public BiomeFilter(Predicate<Biome> biomePredicate) {
        this(0, biomePredicate);
    }

    public BiomeFilter(int priority, Biome... biomes) {
        this(priority, Predicates.in(Arrays.asList(biomes)));
    }

    public BiomeFilter(int priority, Predicate<Biome> biomePredicate) {
        this.predicate = biomePredicate;
        this.priority = priority;
    }

    @Override
    public BiomeFilter negate() {
        return new BiomeFilter(predicate.negate());
    }

    public BiomeFilter or(BiomeFilter other) {
        return new BiomeFilter(predicate.or(other.predicate));
    }

    public BiomeFilter and(BiomeFilter other) {
        return new BiomeFilter(predicate.and(other.predicate));
    }

    @Override
    public BiomeFilter and(Predicate<? super Biome> other) {
        return new BiomeFilter(predicate.and(other));
    }

    @Override
    public BiomeFilter or(Predicate<? super Biome> other) {
        return new BiomeFilter(predicate.or(other));
    }

    @Override
    public boolean test(@Nullable Biome biome) {
        if (biome == null) return false;
        if (this == ALL) return true;
        return predicate.test(biome);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiomeFilter that = (BiomeFilter) o;
        return priority == that.priority &&
                predicate.equals(that.predicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicate, priority);
    }

    public int getPriority() {
        return priority;
    }

    public BiomeFilter setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public Boolean apply(Biome biome) {
        return test(biome);
    }
}

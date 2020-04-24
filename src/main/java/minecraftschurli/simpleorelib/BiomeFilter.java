package minecraftschurli.simpleorelib;

import com.google.common.base.Predicates;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link BiomeFilter} is a concept for filtering {@link Biome Biomes}.<br>
 * This class also provides some default {@link BiomeFilter BiomeFilters}
 *
 * @author Minecraftschurli
 * @version 2020-01-18
 */
@SuppressWarnings("unused")
public class BiomeFilter implements Predicate<Biome>, Function<Biome, Boolean> {
    /**
     * A {@link BiomeFilter} matching all {@link Biome Biomes}
     */
    public static final BiomeFilter ALL = new BiomeFilter(-1);

    /**
     * A {@link BiomeFilter} matching all ocean {@link Biome Biomes}
     */
    public static final BiomeFilter OCEAN = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN));

    /**
     * A {@link BiomeFilter} matching all river {@link Biome Biomes}
     */
    public static final BiomeFilter RIVER = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER));

    /**
     * A {@link BiomeFilter} matching all sandy {@link Biome Biomes}
     */
    public static final BiomeFilter SANDY = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY));

    /**
     * A {@link BiomeFilter} matching all nether {@link Biome Biomes}
     */
    public static final BiomeFilter NETHER = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER));

    /**
     * A {@link BiomeFilter} matching all overworld {@link Biome Biomes}
     */
    public static final BiomeFilter OVERWORLD = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD));

    /**
     * A {@link BiomeFilter} matching all end {@link Biome Biomes}
     */
    public static final BiomeFilter THE_END = new BiomeFilter(biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.END));

    /**
     * A {@link BiomeFilter} matching all vanilla desert {@link Biome Biomes}
     */
    public static final BiomeFilter DESERT = new BiomeFilter(
            Biomes.DESERT,
            Biomes.DESERT_HILLS,
            Biomes.DESERT_LAKES
    );

    /**
     * A {@link BiomeFilter} matching all vanilla badlands {@link Biome Biomes}
     */
    public static final BiomeFilter BADLANDS = new BiomeFilter(
            Biomes.BADLANDS,
            Biomes.BADLANDS_PLATEAU,
            Biomes.ERODED_BADLANDS,
            Biomes.MODIFIED_BADLANDS_PLATEAU,
            Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU,
            Biomes.WOODED_BADLANDS_PLATEAU
    );

    /**
     * A {@link BiomeFilter} matching all vanilla mountain {@link Biome Biomes}
     */
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

    /**
     * Creates a {@link BiomeFilter} that matches all provided {@link Biome Biomes}
     * @param biomes the {@link Biome Biomes} the {@link BiomeFilter} should match
     */
    public BiomeFilter(@Nonnull Biome... biomes) {
        this(Predicates.in(Arrays.asList(biomes)));
    }

    /**
     * Creates a {@link BiomeFilter} that matches every {@link Biome} where biomePredicate evaluates to true
     * @param biomePredicate the {@link Predicate} that is used to filter the {@link Biome Biomes}
     */
    public BiomeFilter(@Nonnull Predicate<Biome> biomePredicate) {
        this(0, biomePredicate);
    }

    /**
     * Creates a {@link BiomeFilter} that matches all provided {@link Biome Biomes}
     * @param priority the priority for the associated {@link net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType FillerBlockType} if there is any
     * @param biomes   the {@link Biome Biomes} the {@link BiomeFilter} should match
     */
    public BiomeFilter(int priority, @Nonnull Biome... biomes) {
        this(priority, Predicates.in(Arrays.asList(biomes)));
    }

    /**
     * Creates a {@link BiomeFilter} that matches every {@link Biome} where biomePredicate evaluates to true
     * @param priority       the priority for the associated {@link net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType FillerBlockType} if there is any
     * @param biomePredicate the {@link Predicate} that is used to filter the {@link Biome Biomes}
     */
    public BiomeFilter(int priority, @Nonnull Predicate<Biome> biomePredicate) {
        this.predicate = biomePredicate;
        this.priority = priority;
    }

    /**
     * Negates this {@link BiomeFilter}
     * @return the negated {@link BiomeFilter}
     */
    @Nonnull
    @Override
    public BiomeFilter negate() {
        return new BiomeFilter(predicate.negate());
    }

    /**
     * Combines this {@link BiomeFilter} with another {@link BiomeFilter} using the "or" operator
     * @param other the {@link BiomeFilter} to combine with
     * @return the new "or" combined {@link BiomeFilter}
     */
    @Nonnull
    public BiomeFilter or(@Nonnull BiomeFilter other) {
        return new BiomeFilter(predicate.or(other.predicate));
    }

    /**
     * Combines this {@link BiomeFilter} with another {@link BiomeFilter} using the "and" operator
     * @param other the {@link BiomeFilter} to combine with
     * @return the new "and" combined {@link BiomeFilter}
     */
    @Nonnull
    public BiomeFilter and(@Nonnull BiomeFilter other) {
        return new BiomeFilter(predicate.and(other.predicate));
    }

    /**
     * Combines this {@link BiomeFilter} with a {@link Predicate} using the "or" operator
     * @param other the {@link Predicate} to combine with
     * @return the new "or" combined {@link BiomeFilter}
     */
    @Nonnull
    @Override
    public BiomeFilter or(@Nonnull Predicate<? super Biome> other) {
        return new BiomeFilter(predicate.or(other));
    }

    /**
     * Combines this {@link BiomeFilter} with a {@link Predicate} using the "and" operator
     * @param other the {@link Predicate} to combine with
     * @return the new "and" combined {@link BiomeFilter}
     */
    @Nonnull
    @Override
    public BiomeFilter and(@Nonnull Predicate<? super Biome> other) {
        return new BiomeFilter(predicate.and(other));
    }

    /**
     * Tests if the given {@link Biome} matches this {@link BiomeFilter}
     * @param biome the {@link Biome} to test for
     * @return {@code true} if the given {@link Biome} matches this {@link BiomeFilter}; {@code false} otherwise.
     */
    @Override
    public boolean test(@Nullable Biome biome) {
        if (biome == null) return false;
        if (this == ALL) return true;
        return predicate.test(biome);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param   other   the reference object with which to compare.
     * @return  {@code true} if this object is the same as the obj
     *          argument; {@code false} otherwise.
     * @see     #hashCode()
     * @see     java.util.HashMap
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof BiomeFilter)) return false;
        BiomeFilter that = (BiomeFilter) other;
        return priority == that.priority &&
                predicate.equals(that.predicate);
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     *     method, then calling the {@code hashCode} method on each of
     *     the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link java.lang.Object#equals(java.lang.Object)}
     *     method, then calling the {@code hashCode} method on each of the
     *     two objects must produce distinct integer results.  However, the
     *     programmer should be aware that producing distinct integer results
     *     for unequal objects may improve the performance of hash tables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by
     * class {@code Object} does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java&trade; programming language.)
     *
     * @return  a hash code value for this object.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.lang.System#identityHashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(predicate, priority);
    }

    /**
     * Gets the ordering priority for this {@link BiomeFilter}
     * @return the ordering priority for this {@link BiomeFilter}; defaults to 0
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the ordering priority for this {@link BiomeFilter}
     * @param priority the ordering priority for this {@link BiomeFilter}
     * @return {@code this}
     */
    @Nonnull
    public BiomeFilter setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Alias for {@link BiomeFilter#test(Biome)}
     * @see BiomeFilter#test(Biome)
     */
    @Override
    public Boolean apply(@Nullable Biome biome) {
        return test(biome);
    }
}

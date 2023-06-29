package io.devpl.sdk;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.LongSupplier;

/**
 * A class that actually generates ULIDs.
 * <p>
 * This class is used by {@link ULID}.
 * <p>
 * You can use this class if you need to use a specific random generator
 * strategy. However, most people just need {@link ULID}.
 * <p>
 * Instances of this class can behave in one of two ways: monotonic(单调的) or
 * non-monotonic (default).
 * <p>
 * If the factory is monotonic, the random component is incremented by 1 If more
 * than one ULID is generated within the same millisecond.
 * <p>
 * The maximum ULIDs that can be generated per millisecond is 2^80.
 */
public final class UlidFactory {

    private final Clock clock; // for tests
    private final LongFunction<ULID> ulidFunction;

    /**
     * Default constructor.
     */
    public UlidFactory() {
        this(new UlidFunction(new ByteRandom()));
    }

    private UlidFactory(LongFunction<ULID> ulidFunction) {
        this(ulidFunction, null);
    }

    private UlidFactory(LongFunction<ULID> ulidFunction, Clock clock) {
        this.ulidFunction = ulidFunction;
        this.clock = clock != null ? clock : Clock.systemUTC();
    }

    /**
     * Returns a new factory.
     * <p>
     * It is equivalent to {@code new UlidFactory()}.
     * @return {@link UlidFactory}
     */
    public static UlidFactory newInstance() {
        return new UlidFactory(new UlidFunction(new ByteRandom()));
    }

    /**
     * Returns a new factory.
     * @param random a {@link Random} generator
     * @return {@link UlidFactory}
     */
    public static UlidFactory newInstance(Random random) {
        return new UlidFactory(new UlidFunction(IRandom.newInstance(random)));
    }

    /**
     * Returns a new factory.
     * <p>
     * The given random function must return a long value.
     * @param randomFunction a random function that returns a long value
     * @return {@link UlidFactory}
     */
    public static UlidFactory newInstance(LongSupplier randomFunction) {
        return new UlidFactory(new UlidFunction(IRandom.newInstance(randomFunction)));
    }

    /**
     * Returns a new factory.
     * <p>
     * The given random function must return a byte array.
     * @param randomFunction a random function that returns a byte array
     * @return {@link UlidFactory}
     */
    public static UlidFactory newInstance(IntFunction<byte[]> randomFunction) {
        return new UlidFactory(new UlidFunction(IRandom.newInstance(randomFunction)));
    }

    /**
     * Returns a new monotonic factory.
     * @return {@link UlidFactory}
     */
    public static UlidFactory newMonotonicInstance() {
        return new UlidFactory(new MonotonicFunction(new ByteRandom()));
    }

    /**
     * Returns a new monotonic factory.
     * @param random a {@link Random} generator
     * @return {@link UlidFactory}
     */
    public static UlidFactory newMonotonicInstance(Random random) {
        return new UlidFactory(new MonotonicFunction(IRandom.newInstance(random)));
    }

    /**
     * Returns a new monotonic factory.
     * <p>
     * The given random function must return a long value.
     * @param randomFunction a random function that returns a long value
     * @return {@link UlidFactory}
     */
    public static UlidFactory newMonotonicInstance(LongSupplier randomFunction) {
        return new UlidFactory(new MonotonicFunction(IRandom.newInstance(randomFunction)));
    }

    /**
     * Returns a new monotonic factory.
     * <p>
     * The given random function must return a byte array.
     * @param randomFunction a random function that returns a byte array
     * @return {@link UlidFactory}
     */
    public static UlidFactory newMonotonicInstance(IntFunction<byte[]> randomFunction) {
        return new UlidFactory(new MonotonicFunction(IRandom.newInstance(randomFunction)));
    }

    /**
     * Returns a new monotonic factory.
     * <p>
     * The given random function must return a long value.
     * @param randomFunction a random function that returns a long value
     * @param clock          a custom clock instance for tests
     * @return {@link UlidFactory}
     */
    static UlidFactory newMonotonicInstance(LongSupplier randomFunction, Clock clock) {
        return new UlidFactory(new MonotonicFunction(IRandom.newInstance(randomFunction)), clock);
    }

    /**
     * Returns a new monotonic factory.
     * <p>
     * The given random function must return a byte array.
     * @param randomFunction a random function that returns a byte array
     * @param clock          a custom clock instance for tests
     * @return {@link UlidFactory}
     */
    static UlidFactory newMonotonicInstance(IntFunction<byte[]> randomFunction, Clock clock) {
        return new UlidFactory(new MonotonicFunction(IRandom.newInstance(randomFunction)), clock);
    }

    // ******************************
    // Public methods
    // ******************************

    /**
     * Returns a UUID.
     * @return a ULID
     */
    public synchronized ULID create() {
        return this.ulidFunction.apply(clock.millis());
    }

    /**
     * Returns a UUID with a specific time.
     * @param time a number of milliseconds since 1970-01-01 (Unix epoch).
     * @return a ULID
     */
    public synchronized ULID create(final long time) {
        return this.ulidFunction.apply(time);
    }

    // ******************************
    // Package-private inner classes
    // ******************************

    /**
     * Function that creates ULIDs.
     */
    static final class UlidFunction implements LongFunction<ULID> {

        private final IRandom random;

        public UlidFunction(IRandom random) {
            this.random = random;
        }

        @Override
        public ULID apply(final long time) {
            if (this.random instanceof ByteRandom) {
                return new ULID(time, this.random.nextBytes(ULID.RANDOM_BYTES));
            } else {
                final long msb = (time << 16) | (this.random.nextLong() & 0xffffL);
                final long lsb = this.random.nextLong();
                return new ULID(msb, lsb);
            }
        }
    }

    /**
     * Function that creates Monotonic ULIDs.
     */
    static final class MonotonicFunction implements LongFunction<ULID> {

        private ULID lastUlid;

        private final IRandom random;

        // Used to preserve monotonicity when the system clock is
        // adjusted by NTP after a small clock drift or when the
        // system clock jumps back by 1 second due to leap second.
        private static final int CLOCK_DRIFT_TOLERANCE = 10_000;

        public MonotonicFunction(IRandom random) {
            this.random = random;
            // initialize internal state
            this.lastUlid = new ULID(0L, this.random.nextBytes(ULID.RANDOM_BYTES));
        }

        @Override
        public synchronized ULID apply(final long time) {

            final long lastTime = lastUlid.getTime();

            // Check if the current time is the same as the previous time or has moved
            // backwards after a small system clock adjustment or after a leap second.
            // Drift tolerance = (previous_time - 10s) < current_time <= previous_time
            if ((time > lastTime - CLOCK_DRIFT_TOLERANCE) && (time <= lastTime)) {
                this.lastUlid = this.lastUlid.increment();
            } else {
                if (this.random instanceof ByteRandom) {
                    this.lastUlid = new ULID(time, this.random.nextBytes(ULID.RANDOM_BYTES));
                } else {
                    final long msb = (time << 16) | (this.random.nextLong() & 0xffffL);
                    final long lsb = this.random.nextLong();
                    this.lastUlid = new ULID(msb, lsb);
                }
            }

            return new ULID(this.lastUlid);
        }
    }

    interface IRandom {

        /**
         * 生成随机的long型数据
         * @return
         */
        long nextLong();

        byte[] nextBytes(int length);

        static IRandom newInstance() {
            return new ByteRandom();
        }

        static IRandom newInstance(Random random) {
            if (random == null) {
                return new ByteRandom();
            } else {
                if (random instanceof SecureRandom) {
                    return new ByteRandom(random);
                } else {
                    return new LongRandom(random);
                }
            }
        }

        static IRandom newInstance(LongSupplier randomFunction) {
            return new LongRandom(randomFunction);
        }

        static IRandom newInstance(IntFunction<byte[]> randomFunction) {
            return new ByteRandom(randomFunction);
        }
    }

    static class LongRandom implements IRandom {

        private final LongSupplier randomFunction;

        public LongRandom() {
            this(newRandomFunction(null));
        }

        public LongRandom(Random random) {
            this(newRandomFunction(random));
        }

        public LongRandom(LongSupplier randomFunction) {
            this.randomFunction = randomFunction != null ? randomFunction : newRandomFunction(null);
        }

        @Override
        public long nextLong() {
            return randomFunction.getAsLong();
        }

        @Override
        public byte[] nextBytes(int length) {
            int shift = 0;
            long random = 0;
            final byte[] bytes = new byte[length];
            for (int i = 0; i < length; i++) {
                if (shift < Byte.SIZE) {
                    shift = Long.SIZE;
                    random = randomFunction.getAsLong();
                }
                shift -= Byte.SIZE; // 56, 48, 40...
                bytes[i] = (byte) (random >>> shift);
            }
            return bytes;
        }

        static LongSupplier newRandomFunction(Random random) {
            final Random entropy = random != null ? random : new SecureRandom();
            return entropy::nextLong;
        }
    }

    static class ByteRandom implements IRandom {

        private final IntFunction<byte[]> randomFunction;

        public ByteRandom() {
            this(newRandomFunction(null));
        }

        public ByteRandom(Random random) {
            this(newRandomFunction(random));
        }

        public ByteRandom(IntFunction<byte[]> randomFunction) {
            this.randomFunction = randomFunction != null ? randomFunction : newRandomFunction(null);
        }

        @Override
        public long nextLong() {
            long number = 0;
            byte[] bytes = this.randomFunction.apply(Long.BYTES);
            for (int i = 0; i < Long.BYTES; i++) {
                number = (number << 8) | (bytes[i] & 0xff);
            }
            return number;
        }

        @Override
        public byte[] nextBytes(int length) {
            return this.randomFunction.apply(length);
        }

        static IntFunction<byte[]> newRandomFunction(Random random) {
            final Random entropy = random != null ? random : new SecureRandom();
            return (final int length) -> {
                final byte[] bytes = new byte[length];
                entropy.nextBytes(bytes);
                return bytes;
            };
        }
    }
}

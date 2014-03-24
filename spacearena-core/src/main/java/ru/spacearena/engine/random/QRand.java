package ru.spacearena.engine.random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-24-03
 */
public class QRand {

    private long u;
    private long v;
    private long w;

    public QRand() {
        setSeed(System.nanoTime());
    }

    public QRand(long seed) {
        setSeed(seed);
    }

    public void setSeed(long seed) {
        u = 0;
        v = 4101842887655102017L;
        w = seed;
    }

    public long nextLong() {
        u = u * 2862933555777941757L + 7046029254386353087L;
        v ^= v >>> 17;
        v ^= v << 31;
        v ^= v >>> 8;
        w = 4294957665L * w + (w >>> 32);
        long x = u ^ (u << 21);
        x ^= x >>> 35;
        x ^= x << 4;
        return (x + v) ^ w;
    }

    public int nextInt() {
        final long l = nextLong();
        return (int)(l >> 32) ^ (int)l;
    }

    public int nextInt(int mod) {
        final int v = nextInt() % mod;
        return v < 0 ? mod + v : v;
    }

    public float nextFloat() {
        return (float)(nextLong() & 0xffffff) / ((float)(1<<24));
    }

    public float nextFloatBetween(float min, float max) {
        return min + nextFloat() * (max - min);
    }


}

package hash;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Classe Singleton per il calcolo di funzioni hash per Bloom Filter
 *  mediante funzione Murmur3.
 * Basata su Same Performance Less Hashing.
 *
 * g_i(x) = h1(x) + i*h2(x)
 *
 * @author Marco Costa
 */
public class Murmur3_Hash implements BloomHash {
    private static final Murmur3_Hash Instance;

    private final HashFunction hash1;

    private static final int SEED_1 = 0;

    static {
        Instance = new Murmur3_Hash();
    }

    private Murmur3_Hash() {
        hash1 = Hashing.murmur3_128(SEED_1);
    }

    public static Murmur3_Hash getInstance() { return Instance; };

    /**
     * Conversione di un oggetto in array di byte.
     *
     * @param in l'oggetto da convertire
     * @return l'array di byte
     * @throws IOException se non è possibile convertire l'oggetto
     */
    private static byte[] objectToBytes(Object in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (ObjectOutput out = new ObjectOutputStream(bos);)
        {
            out.writeObject(in);
            out.flush();
        }
        catch (IOException ex) {
            throw new IOException();
        }

        return bos.toByteArray();
    }

    /**
     * Computazione delle k posizioni risultanti dall'operazione di combinazione
     *  g_i(x) = h1(x) + ih2(x)
     *
     * @param h1 la prima f. hash
     * @param h2 la seconda f. hash
     * @param k il numero di iterazioni (o posizioni da computare)
     * @param bound limite superiore per ogni posizione
     * @return il vettore di posizioni
     */
    private int[] computeHash(int h1, int h2, int k, int bound) {
        int[] out = new int[k];

        for(int i = 0; i < k; i++)
        {
            out[i] = h1 + (i + 1) * h2;
            if(out[i] < 0)
                out[i] = ~out[i];

            out[i] = out[i] % bound;
        }

        return out;
    }

    /**
     * Effettua l'hash di un oggetto serializzabile e ne restituisce il vettore
     *  di k posizioni, tale che ogni posizione sia < bound.
     *
     * @param o l'oggetto
     * @param k il numero di posizioni
     * @param bound il limite superiore per ogni posizione
     * @return il vettore di posizioni
     */
    public int[] hashObject(Object o, int k, int bound) {
        if(!(o instanceof Serializable))
            throw new IllegalArgumentException("l'oggetto deve essere serializzabile");

        byte[] byteObject;

        try {
            byteObject = Murmur3_Hash.objectToBytes(o);
        } catch (IOException e) {
            throw new IllegalArgumentException("l'oggetto non può essere convertito in byte");
        }

        long hash64 = hash1.hashBytes(byteObject).asLong();
        int h1 = (int) hash64;
        int h2 = (int) (hash64 >>> 32);

        return computeHash(h1, h2, k, bound);
    }

    /**
     * Effettua l'hash di una stringa UTF-8 e ne restituisce il vettore
     *  di k posizioni, tale che ogni posizione sia < bound.
     *
     * @param s la stringa UTF-8
     * @param k il numero di posizioni
     * @param bound il limite superiore per ogni posizione
     * @return il vettore di posizioni
     */
    public int[] hashObject(String s, int k, int bound) {
        long hash64 = hash1.hashString(s, StandardCharsets.UTF_8).asLong();
        int h1 = (int) hash64;
        int h2 = (int) (hash64 >>> 32);

        return computeHash(h1, h2, k, bound);
    }

}

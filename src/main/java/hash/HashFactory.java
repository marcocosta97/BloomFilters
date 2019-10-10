package hash;

/**
 * @author Marco Costa
 */
public class HashFactory {
    public static BloomHash getHashFunction() {
        return Murmur3_Hash.getInstance();
    }
}

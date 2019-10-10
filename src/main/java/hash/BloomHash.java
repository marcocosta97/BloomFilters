package hash;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Marco Costa
 */
public interface BloomHash {

    public int[] hashObject(Object o, int k, int bound);
    public int[] hashObject(String s, int k, int bound);
}

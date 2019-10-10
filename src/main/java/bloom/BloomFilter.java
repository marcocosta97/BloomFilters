package bloom;

import java.io.Serializable;

/**
 * Interfaccia astratta per l'implementazione di BloomFilter.
 * Espone metodi per il calcolo delle stime teoriche ottimali.
 *
 * @author Marco Costa
 */
public abstract class BloomFilter implements Serializable {
    private static final double ln2 = Math.log(2);

    private static final double logbase2(double x) {
        return Math.log(x) / ln2;
    }

    /**
     * Calcola il valore ottimo di K a partire dalla probabilità pf.
     *
     * @param pf la probabilità di falso positivo in (0, 1)
     * @return il valore ottimo di K
     */
    public static final int computeK(double pf) {
        /* K = -log_2(pf) */
        return new Double(Math.round(-logbase2(pf))).intValue();
    }

    /**
     * Calcola il valore ottimo di M a partire dalla probabilità pf e dal
     *  numero di inserimenti previsti N.
     *
     * @param n il numero di inserimenti previsti
     * @param pf la probabilità di falso positivo in (0, 1)
     * @return il valore ottimo di M
     */
    public static final int computeM(int n, double pf) {
        /* M = (n * -log_2(pf))/(ln(2)) */
        return new Double(Math.ceil((n * -logbase2(pf)) / ln2)).intValue();
    }

    /* metodi per il settaggio di bit a 1 */
    abstract public void set(int index);
    abstract public void set(int[] index);

    /* metodi per la verifica di bit con valore 1 */
    abstract public boolean isSet(int index);
    abstract public boolean isSet(int[] index);

    /* metodi per la gestione dell'array */
    abstract public void clear();
    abstract public int length();
    abstract public int size();
    abstract public boolean isEmpty();
}

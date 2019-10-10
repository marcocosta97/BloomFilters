package bloom;

import java.util.BitSet;

/**
 * Implementazione dell'interfaccia astratta BloomFilter mediante BitSet array.
 *
 * @see BloomFilter
 * @author Marco Costa
 */
public class BitSetBloomFilter extends BloomFilter {
    private final BitSet array;
    private final int size;

    /**
     * Creazione di un nuovo BloomFilter implementato mediante BitSet di dimensione size bit.
     *
     * @param size la dimensione in bit
     */
    public BitSetBloomFilter(int size) {
        this.size = size;
        array = new BitSet(size); /* bit settati a 0 */
    }

    /**
     * Verifica che pos sia un indice valido del BloomFilter.
     *
     * @param pos l'indice
     * @throws IndexOutOfBoundsException se pos non è un indice valido
     */
    private void checkPosition(int pos) throws IndexOutOfBoundsException {
        if ((pos < 0) || (pos > size))
            throw new IndexOutOfBoundsException("Indice " + pos + " non valido");
    }

    /**
     * Imposta a true l'indice index del Bloom Filter.
     *
     * @param index l'indice
     * @throws IndexOutOfBoundsException se index non è un indice valido
     */
    public void set(int index) throws IndexOutOfBoundsException {
        checkPosition(index);
        array.set(index);
    }

    /**
     * Imposta a true tutti gli indici in index del Bloom Filter.
     * Se uno degli indici contenuti in index non è valido l'operazione
     *  non viene eseguita.
     *
     * @param index il vettore di indici
     * @throws IndexOutOfBoundsException se index contiene un indice non valido
     */
    public void set(int[] index) throws IndexOutOfBoundsException {
        for(int i : index)
            checkPosition(i);
        for(int i : index)
            array.set(i);
    }

    /**
     * Restituisce true sse l'indice index è settato a true.
     *
     * @param index l'indice della struttura
     * @return tt sse array[index] = 1
     * @throws IndexOutOfBoundsException se index non è un indice valido
     */
    public boolean isSet(int index) throws IndexOutOfBoundsException {
        checkPosition(index);
        return array.get(index);
    }

    /**
     * Restituisce true sse tutti gli indici in index sono settati a true.
     *
     * @param index il vettore di indici
     * @return tt sse forall i in index -> array[i] = 1
     * @throws IndexOutOfBoundsException se index contiene un indice non valido
     */
    public boolean isSet(int[] index) throws IndexOutOfBoundsException {
        for (int i : index)
            if (!isSet(i))
                return false;

        return true;
    }

    /**
     * Reimposta tutti i bit della struttura a false.
     */
    public void clear() {
        array.clear();
    }

    /**
     * Restituisce la dimensione effettiva in bit in memoria della struttura. Può non coincidere con la
     * dimensione impostata.
     *
     * @return la dimensione in bit in memoria
     */
    public int size() {
        return array.size();
    }

    /**
     * Restituisce la dimensione logica della struttura. L'indice dell'ultimo bit più uno.
     *
     * @return la dimensione logica della struttura
     */
    public int length() {
        return array.length();
    }


    /**
     * Restituisce true sse tutti gli indici della struttura sono settati a false.
     *
     * @return tt sse forall i in size -> array[i] = 0
     */
    public boolean isEmpty() {
        return array.isEmpty();
    }
}

package bloom;

import hash.BloomHash;
import hash.HashFactory;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Optimal Bloom Set.
 * Set di elementi di tipo E realizzato mediante Bloom Filter e
 *  calcolo di stime teoriche ottimali.
 *
 * @param <E> il tipo dell'elemento, deve essere Serializzabile
 * @author Marco Costa
 */
public class BloomSet<E extends Serializable> extends AbstractSet<E>
        implements Set<E>, Cloneable, Serializable {
    private final BloomFilter array;

    private final int k;
    private final int m;

    private int n = 0;

    private final BloomHash hash = HashFactory.getHashFunction();

    /**
     * Creazione di un nuovo Bloom Filter con numero di elementi n e probabilità
     *  di falso positivo pf.
     * La dimensione in memoria e il numero di funzioni Hash sono calcolate a partire
     *  dai due parametri.
     *
     * @param n ordine del numero di elementi dell'insieme
     * @param pf probabilità di falso positivo richiesta in (0, 1)
     */
    public BloomSet(int n, double pf) {
        if((pf <= 0) || (pf >= 1))
            throw new IllegalArgumentException("pf deve essere compreso tra 0 e 1");
        if(n <= 0)
            throw new IllegalArgumentException("n deve essere maggiore di 0");

        k = BloomFilter.computeK(pf);
        m = BloomFilter.computeM(n, pf);

        array = new BitSetBloomFilter(m);
    }

    /**
     * Aggiunta di un elemento al Set.
     *
     * @param e l'elemento da aggiungere
     * @return tt
     */
    @Override
    public boolean add(E e) {
        array.set(hash.hashObject(e, k, m));
        n++;
        return true;
    }

    /**
     * Aggiunta di una collezione di elementi al Set.
     *
     * @param c la collezione da aggiungere
     * @return tt
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for(E item : c)
            add(item);
        return true;
    }

    /**
     * Restituisce true se l'elemento è possibilmente nel set, false
     *  se l'elemento è definitivamente non nel set.
     * La probabilità di falso positivo è pf.
     *
     * @param o l'elemento da verificare
     * @return true se l'elemento è possibilmente nel set,
     *         false se l'elemento è definitivamente non nel set
     */
    @Override
    public boolean contains(Object o) {
        return array.isSet(hash.hashObject(o, k, m));
    }

    /**
     * Restituisce true se la collezione di elementi è possibilmente nel set,
     * false se la collezione è definitivamente non nel set.
     * La probabilità di falso positivo è pf ^ c.size().
     *
     * @param c la collezione da verificare
     * @return true se la collezione è possibilmente nel set,
     *         false se la collezione è definitivamente non nel set
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c)
            if(!contains(o)) return false;

        return true;
    }

    /**
     * Il BloomFilter non supporta iterazione.
     * Lancia una UnsupportedOperationException.
     *
     * @return
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("La struttura non supporta iterazione");
    }

    /**
     * Il BloomFilter non supporta rimozione.
     * Lancia una UnsupportedOperationException.
     *
     * @param o
     * @return
     */
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("La struttura non supporta la rimozione");
    }

    /**
     * Restituisce il numero di elementi aggiunti al Set.
     *
     * @return
     */
    @Override
    public int size() {
        return n;
    }

    /**
     * Restituisce true se nessun elemento è stato aggiunto al Set.
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    /**
     * Reimposta la struttura come vuota.
     */
    @Override
    public void clear() {
        array.clear();
    }

    /**
     * Restituisce la dimensione fisica in memoria.
     *
     * @return
     */
    public int dimension() {
        return array.size();
    }

}

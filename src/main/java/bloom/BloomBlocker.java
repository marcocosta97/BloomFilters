package bloom;

import hash.BloomHash;
import hash.HashFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * BloomBlocker.
 *
 * @author Marco Costa
 */
public class BloomBlocker {
    private final BloomSet<String> set;

    /**
     * Restituisce la lista dei domini contenuti all'interno del file "filename".
     *
     * @param filename il nome del file di host
     * @return la lista dei domini
     * @throws IOException se il file non esiste
     */
    public static final ArrayList<String> loadHostfile(String filename) throws IOException {
        ArrayList<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if(line.startsWith("0.0.0.0")) /* 0.0.0.0 web.domain.com */
                {
                    final String[] split = line.split("0.0.0.0");
                    list.add(split[1].trim());
                }
            }
        }

        return list;
    }

    /**
     * Costruisce un nuovo BloomBlocker a partire da una lista di url malevoli
     *  e la probabilità di falso positivo desiderata.
     *
     * @param hostnames la lista di host
     * @param pf la probabilità di falso positivo
     */
    public BloomBlocker(ArrayList<String> hostnames, float pf) {
        set = new BloomSet<>(hostnames.size(), pf);
        set.addAll(hostnames);
    }

    /**
     * Costruisce un nuovo BloomBlocker a partire da un file host
     *  e la probabilità di falso positivo desiderata.
     *
     * @param filename il nome del file host
     * @param pf la probabilità di falso positivo
     * @throws IOException se il file non esiste
     */
    public BloomBlocker(String filename, float pf) throws IOException {
        this(loadHostfile(filename), pf);
    }

    /**
     * Verifica la presenza di un dominio all'interno del file.
     *
     * @param domain il dominio da verificare
     * @return tt se è nel set con probabilità (1 - pf)
     *         ff se non è nel set co probabilità 1
     */
    public boolean checkDomain(String domain) {
        return set.contains(domain);
    }

    public int dimension() {
        return set.dimension();
    }


}

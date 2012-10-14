package uk.org.opencomment.lang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Vector;

/**
 * Stop word removal tool to process a List of Strings.
 * @author fherrmann
 * @version $Revision: 34 $
 */
public class StopTermRemoval {
    /**
     * Disallowed symbols. Term containing any of these smbols will be removed.
     */
    public static final char[] SYMBOLS = new char[] {'0', '1', '2', '3', '4',
        '5', '6', '7', '8', '9', '"', '#', '.'};
    private List<String> stopTerms;

    /**
     * Construct a  StopTermRemoval instance using a file of stop words.
     * Each line of the file contains a word to be removed.
     * @param filename a file name <code>String</code>.
     */
    public StopTermRemoval(String filename) throws Exception {
        initialise(new FileReader(filename));
    }

    /**
     * @param input
     * @throws Exception
     */
    public StopTermRemoval(InputStream input) throws Exception {
        initialise(new InputStreamReader(input));
    }

    /**
     * @param source
     */
    private void initialise(Reader source) {
        stopTerms = new Vector<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(source);
            String line = reader.readLine();
            while(line != null) {
                stopTerms.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove stop words from a list of terms.
     * @param termlist is a <code>List</code> of terms.
     * @return a <code>List</code> of terms as convenience
     * for chaining.
     */
    public List<String> removeStopTerms(List<String> termlist) {
        /*
         * Cannot use iterator as terms may be removed from the list
         * destroying the integrity of the iterator.
         */
        for(int i = 0; i < termlist.size(); i++ ) {
            String term = termlist.get(i);
            if(stopTerms.contains(term))
                termlist.remove(i);
        }
        return termlist;
    }

    /**
     *
     * @param termlist is a <code>List</code> of terms.
     * @param symbols a <code>char[]</code> with forbidden symbols.
     * @return a <code>List</code> of terms as convenience
     * for chaining.
     */
    public List<String> removeSymbolTerms(List<String> termlist, char[] symbols) {
        /*
         * Cannot use iterator as terms may be removed from the list
         * destroying the integrity of the iterator.
         */
        for(int i = 0; i < termlist.size(); i++ ) {
            String term = termlist.get(i);
            boolean terminate = false;
            char[] chars = new String(term).toCharArray();
            for(int n = 0; n < chars.length && ! terminate; n++) {
                for(int m = 0; m < symbols.length && ! terminate; m++) {
                    if(chars[n] == symbols[m]) {
                        terminate = true;
                        break;
                    }
                }
            }
            if(terminate)
                termlist.remove(i);
        }
        return termlist;
    }

    /**
     * Remove a number of default symbols.
     * @param terms
     * @return  a <code>List</code> of terms as convenience
     * for chaining.
     */
    public List<String> removeSymbolTerms(List<String> terms) {
        terms = removeSymbolTerms(terms, SYMBOLS);
        return terms;
    }

}

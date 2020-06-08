package org.bowssf.util;

import it.uniroma1.lcl.babelnet.*;
import it.uniroma1.lcl.babelnet.data.BabelGloss;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.jlt.util.Language;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class encapsulates all required information to support Babelfy and
 * Babelnet queryies
 *
 * @author Alejandro Borrajo Viéitez
 */
public class BabelUtils {



    /**
     * An instance of BabelNet object required to query BabelNet
     */
    private static final BabelNet bn = BabelNet.getInstance();




    /**
     * The default language
     */
    private static final Language DEFAULT_LANGUAGE = Language.EN;

    /**
     * The language to search
     */
    private static Language lang = DEFAULT_LANGUAGE;

    /**
     * Returns the language to be used;
     * @return the specified language
     */
    public static Language getLang() {
        return lang;
    }

    /**
     * Changes the value of the language
     * @param lang the language to change
     */
    public static void setLang(Language lang) {
        BabelUtils.lang = lang;
    }

    /**
     * If the synset has children return a set with the children, if synset doesn't have children return a empty set.
     * @param synset the synset to search children
     * @return a Set with the children from a synset
     */
    public static Set<String> getChildNodes(String synset) {
        BabelSynset by = bn.getSynset(new BabelSynsetID(synset));


        Set<String> childNodes = new HashSet<>();
        by.getOutgoingEdges(BabelPointer.HYPONYM)
                .stream()
                .filter(bsr -> bsr.getLanguage()
                        .equals(DEFAULT_LANGUAGE))
                .collect(Collectors.toSet())
                .forEach(babelSynsetRelation -> childNodes.add(babelSynsetRelation.getBabelSynsetIDTarget().toString()));


        return childNodes;
    }

    /**
     * Return the word from the synset.
     * @param synset the synset to get the word.
     * @return a String containing the word
     */
    public static String getWord(String synset) {
        Optional<BabelSense> optionalBabelSense = bn.getSynset(new BabelSynsetID(synset)).getMainSense(lang);
        if(optionalBabelSense.isPresent()) {
            return optionalBabelSense.get().getFullLemma();
        }
        else {
            return bn.getSynset(new BabelSynsetID(synset)).getMainSense(DEFAULT_LANGUAGE).get().getFullLemma();
        }
    }

    /**
     * Return the description from a synset
     * @param synset the synset to get the gloss
     * @return the description of the synset
     */
    public static String getGloss(String synset) {

        Optional<BabelGloss> gloss = bn.getSynset(new BabelSynsetID(synset)).getMainGloss(lang);
        if (gloss.isPresent()) {
            return gloss.get().getGloss();
        } else {
            return  bn.getSynset(new BabelSynsetID(synset)).getMainGloss(DEFAULT_LANGUAGE).get().getGloss();
        }
    }

    /**
     * Return a Set with the synonyms from one synset
     * @param synset the synset to get the synonyms
     * @return a Set with all the synonyms
     */
    public static Set<String> getSynonyms(String synset){
        Set<String> synonyms = new HashSet<>();
        bn.getSynset(new BabelSynsetID(synset)).getSenses(lang).forEach(babelSense -> {
            synonyms.add(babelSense.getFullLemma());
        });
        return synonyms;
    }




}

package org.bowssf.util;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.data.BabelGloss;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.jlt.util.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * For logging purpopses
     */
    private static final Logger logger = LogManager.getLogger(BabelUtils.class);

    /**
     * An instance of BabelNet object required to query BabelNet
     */
    private static final BabelNet bn = BabelNet.getInstance();

    /**
     * A start synset to navigate in Babelnet hierarchy. The synset means entity
     */
    private final String startSynset = "bn:00031027n";
    /**
     * The default language
     */
    private static final Language DEFAULT_LANGUAGE = Language.EN;

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        BabelUtils.lang = lang;
    }

    /**
     *  The language to search
     */
    private static Language lang = DEFAULT_LANGUAGE;


    public static Set<String> getChildNodes(String synset){
        BabelSynset by = bn.getSynset(new BabelSynsetID(synset));
        Set<String> childNodes = new HashSet<>();
        by.getOutgoingEdges(BabelPointer.HYPONYM)
                .stream()
                .filter(bsr -> bsr.getLanguage()
                .equals(lang))
                .collect(Collectors.toSet())
                .forEach(babelSynsetRelation -> childNodes.add(babelSynsetRelation.getBabelSynsetIDTarget().toString()));
        return childNodes;
    }

    public static String getSynsetStringWord(String synset){
        return bn.getSynset(new BabelSynsetID(synset)).getMainSense(lang).get().getFullLemma();
    }
    public static String getSynsetStringGloss(String synset){
        String defaultString = "";
        Optional<BabelGloss> gloss =  bn.getSynset(new BabelSynsetID(synset)).getMainGloss();
        if (gloss.isPresent()){
            return gloss.get().getGloss();
        }else{
            return defaultString;
        }
    }




}

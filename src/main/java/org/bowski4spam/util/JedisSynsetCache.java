package org.bowssf.util;

import it.uniroma1.lcl.jlt.util.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import java.util.*;

/**
 * This class gets all data from jedis or babelnet
 *
 * @author Alejandro Borrajo Viéitez
 */
public class JedisSynsetCache {

    /**
     * For logging purpopses
     */
    private static final Logger logger = LogManager.getLogger(JedisSynsetCache.class);

    /**
     * A start synset to navigate in Jedis or Babelnet hierarchy. The synset means entity
     */
    public final String START_SYNSET = "bn:00031027n";
    public final Language DEFAULT_LANGUAGE = Language.EN;
    public final String WORDS = "words";
    public final String SYNSET_TO_WORDS = "synsetToWords";
    public final String SYNSET_TO_GLOSS = "synsetToGloss";
    private static JedisSynsetCache instance;
    private Jedis jedis;
    private boolean forceBabelnet = false;
    private boolean forceJedis = true;
    private Language language;

    /**
     * Constructor of the class
     */
    private JedisSynsetCache(){
        jedis = new Jedis();
        language = DEFAULT_LANGUAGE;
    }

    /**
     * Gets the instance from the class
     * @return an object from the class
     */
    public static JedisSynsetCache getInstance()  {
        if (instance == null) {
            instance = new JedisSynsetCache();
        }
        return instance;
    }


    /**
     * Sets a language
     * @param language the desired language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Gets the force babel net value
     * @return a boolean with the value
     */
    public boolean isForceBabelnet() {
        return this.forceBabelnet;
    }

    /**
     * Set the force babel net value
     * @param forceBabelnet the desired value
     */
    public void setForceBabelnet(boolean forceBabelnet) {
        this.forceBabelnet = forceBabelnet;
    }

    /**
     * Gets if jedis is checked.
     * @return a boolean value
     */
    public boolean isForceJedis() {
        return forceJedis;
    }

    /**
     * Sets a boolean value
     * @param forceJedis a boolean value
     */
    public void setForceJedis(boolean forceJedis) {
        this.forceJedis = forceJedis;
    }

    /**
     * Gets all synsets from the same word
     * @param word
     * @return a List with all the synsets
     */
    public List<String> getAllSynsetsWithSameWord(String word){
        List<String> synsets = new ArrayList<>();
        String totalWords = WORDS + this.language.toString();
        String words = this.jedis.hget(totalWords, word);
        if(words.contains("|")){
            String[] splitWords = words.split("\\|");
            synsets.addAll(Arrays.asList(splitWords));
        }else{
            synsets.add(words);
        }
        return synsets;
    }

    /**
     * Gets a synset from a word and a description
     * @param word the word of the synset
     * @param gloss the description of the synset
     * @return a String with the synset
     */
    public String getSynsetFromWord(String word,String gloss) {
        List<String> synsets = this.getAllSynsetsWithSameWord(word);
        String synset = synsets.get(0);
        if (synsets.size() == 1) {
            return synset;
        } else {
            int i = 0;
            boolean stop = false;
            while (!stop && i < synsets.size()) {
                if (this.getGlossFromSynset(synsets.get(i)).contains(gloss)) {
                    stop = true;
                    synset = synsets.get(i);
                }
                i++;
            }
            return synset;
        }
    }

    /**
     * Gets the synonyms from a synset
     * @param synset the desired synset
     * @return the synonyms from the synset
     */
    public Set<String> getSynonyms(String synset){
        return BabelUtils.getSynonyms(synset);
    }

    /**
     * Get a word from a synset
     * @param synset the desired synset
     * @return the word from the synset
     */
    public String getWordFromSynset(String synset){
        String keyWord = SYNSET_TO_WORDS + this.language.toString();
        String word;
        if (!this.jedis.hexists(keyWord, synset) || isForceBabelnet()) {

            word = BabelUtils.getWord(synset);
            this.jedis.hset(keyWord, synset, word);
        } else {
            word = this.jedis.hget(keyWord, synset);
        }
        return word;
    }

    /**
     * Gets the description from the synset
     * @param synset the desired synset
     * @return the description from the synset
     */
    public String getGlossFromSynset(String synset){
        String keyGloss = SYNSET_TO_GLOSS + this.language.toString();
        String gloss;
        if (!this.jedis.hexists(keyGloss, synset) || isForceBabelnet()) {
            gloss = BabelUtils.getGloss(synset);
            this.jedis.hset(keyGloss, synset, gloss);
        } else {
            gloss = this.jedis.hget(keyGloss, synset);
        }
        return gloss;
    }

    /**
     * Gets the children from a synset
     * @param synset the desired synset
     * @return a Set with the children
     */
    public Set<String> getChildNodes(String synset){

        Set<String> childNodes = new HashSet<>();
        try {
            if (!this.isForceBabelnet() && this.jedis.exists(synset)) {
                List<String> childs = new LinkedList<>(this.jedis.lrange(synset, 0, this.jedis.llen(synset)));
                if (childs.size() > 1 || (childs.size() == 1 && !childs.get(0).equals(""))) {
                    childNodes.addAll(childs);
                }
            } else {
                if (!this.isForceJedis()) {
                    childNodes = BabelUtils.getChildNodes(synset);
                    if (childNodes.size() == 0) {
                        this.jedis.lpush(synset, "");
                    } else {
                        childNodes.forEach(node -> this.jedis.lpush(synset, node));
                    }
                }
            }
            this.setWordsAndGlossToJedis(synset);

        } catch (JedisException je) {
            je.printStackTrace();
        }
        this.jedis.close();

        return childNodes;

    }

    /**
     * Sets the word and description in jedis
     * @param synset the synset to store
     */
    private void setWordsAndGlossToJedis(String synset){
        //Set of the string value of the synset id
        String word = this.getWordFromSynset(synset);
        String totalWords = WORDS + this.language.toString();


        if (!jedis.hexists(totalWords, word)) {
            this.jedis.hset(totalWords, word, synset);
        } else {
            String value = this.jedis.hget(totalWords, word);
            if (!value.contains(synset)) {
                value += "|" + synset;
                this.jedis.hset(totalWords, word, value);
            }

        }
    }


}

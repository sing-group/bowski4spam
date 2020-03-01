package org.bowssf.util;



import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

 public class JedisSynsetCache {


    private static Jedis jedis = new Jedis();
    private String gloss;

    public static boolean isForceBabelnet() {
        return forceBabelnet;
    }

    public static void setForceBabelnet(boolean forceBabelnet) {
        JedisSynsetCache.forceBabelnet = forceBabelnet;
    }

    private static boolean forceBabelnet = false;

    public static Set<String> getChildNodes(String synset) {

        Set<String> childNodes = new HashSet<>();
        try {
            if (!forceBabelnet & jedis.exists(synset)) {
                List<String> childs = new LinkedList<>(jedis.lrange(synset, 0, jedis.llen(synset)));
                if (childs.size() > 1 || (childs.size() == 1 && !childs.get(0).equals(""))) {
                    childNodes.addAll(childs);
                }
            } else {
                childNodes = BabelUtils.getChildNodes(synset);
                if (childNodes.size() == 0) {
                    jedis.lpush(synset, "");
                } else {
                    System.out.println(synset);
                    childNodes.forEach(node -> jedis.lpush(synset, node));
                }

            }
            //Set of the string value of the synset id
            String word;
            String gloss;
            if (!jedis.hexists("synsetToWords", synset)) {

                word = BabelUtils.getSynsetStringWord(synset);
                jedis.hset("synsetToWords", synset, word);
            } else {
                word = jedis.hget("synsetToWords", synset);
            }


            if (!jedis.hexists("words", word)) {
                gloss = BabelUtils.getSynsetStringGloss(synset);
                jedis.hset("words", word, synset + "," + gloss);
            } else {
                String value = jedis.hget("words", word);
                if(!value.contains(synset)){
                    gloss = BabelUtils.getSynsetStringGloss(synset);
                    value += "|"+synset+","+gloss;
                    jedis.hset("words", word, value);
                }


            }
        } catch (JedisException je) {
            je.printStackTrace();
        }
        jedis.close();
        return childNodes;

    }

    public static String getSynsetFromWord(String word) {
        return jedis.hget("words", word);
    }
    public static String getWordFromSynset(String synset) {return jedis.hget("synsetToWords",synset);}


}

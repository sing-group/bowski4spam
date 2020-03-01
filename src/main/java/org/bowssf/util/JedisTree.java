package org.bowssf.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class JedisTree {
    /**
     * For logging purpopses
     */
    private static final Logger logger = LogManager.getLogger(JedisTree.class);



    /**
     * A start synset to navigate in Jedis or Babelnet hierarchy. The synset means entity
     */
    private static final String startSynset = "bn:00031027n";

    public static String getStartSynset() {
        return startSynset;
    }

    public static Set<String> getHyponymsFromStart(int levels) {
        logger.info("Get hyponyms started");
        return getHyponymsFromStart(startSynset, levels);
    }

    private static Set<String> getHyponymsFromStart(String nextSynset, int levels) {
        Set<String> allHyponymsFromStart = new HashSet<>();
        allHyponymsFromStart.add(nextSynset);
        if (levels == 0){
            return allHyponymsFromStart;
        }
        try {
            Set<String> elementsInHyponym = JedisSynsetCache.getChildNodes(nextSynset);
            elementsInHyponym.forEach(node -> {
                Set<String> results = getHyponymsFromStart(node, levels - 1);
                allHyponymsFromStart.addAll(results);
            });
        } catch (Exception e) {
            logger.error("Hyponym search problem. The synset " + nextSynset + " does not exists." + e.getMessage());
        }
        return allHyponymsFromStart;
    }
}

package org.bowssf;



import it.uniroma1.lcl.jlt.util.Pair;
import org.bowssf.util.JedisSynsetCache;
import org.bowssf.util.JsonUtils;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ModelConstructor extends SpamDtector {
    public static void main(String[] args) {

        if(args.length != 1 ){
            System.exit(1);
        }else {
            try {
                JedisSynsetCache jedisSynsetCache = JedisSynsetCache.getInstance();
                Map<String, List<String>> interestMap = JsonUtils.read(args[0]);
                List<String> allSynsets = interestMap.get("notInterested");
                allSynsets.addAll(interestMap.get("interested"));
                Map<String, Pair<Integer, Integer>> wordsCount = new HashMap<>();
                Map<String, Double> stadistics = new HashMap<>();
                allSynsets.forEach(synset -> {
                    Set<String> synonyms = jedisSynsetCache.getSynonyms(synset);
                    String mainSense = jedisSynsetCache.getWordFromSynset(synset);
                    synonyms.add(mainSense);
                    synonyms.forEach(word -> {
                        Pair<Integer, Integer> pair = new Pair<>(0, 0);
                        boolean containsWord = interestMap.get("interested").contains(synset);
                        if (wordsCount.containsKey(word)) pair = wordsCount.get(word);
                        int first = pair.getFirst();
                        int second = pair.getSecond();
                        if (containsWord) first++;
                        second++;
                        pair = new Pair<>(first,second);
                        wordsCount.put(word, pair);

                    });

                });
                wordsCount.forEach((key, pair) -> {
                    stadistics.put(key, calculate(pair));
                });
                JsonUtils.save(stadistics, "modelData.json");


            }catch (JedisConnectionException je){
                System.err.println("Conexión jedis no establecida");
            }
        }
    }

    /**
     * Calculate Naive Bayes
     * @param pair the pair to calculate
     * @return return the value calculated
     */
    public static double calculate(Pair<Integer,Integer> pair){
        return ((double) pair.getFirst()+1d) / ((double) pair.getSecond()+ 2d);
    }
}

package org.bowssf.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.*;

/**
 * This class gets the json values stored on disk
 *
 * @author Alejandro Borrajo Viéitez
 */
public class JsonUtils {

    public static final String ROUTE = "interest.json";


    public static <T,G> void save(Map<T,G> interestMap){
        save(interestMap,ROUTE);
    }

    /**
     * Saves the data in a .json
     * @param interestMap the data
     * @param file the file route
     * @param <T> the first class
     * @param <G> the second class
     */
    public static <T,G> void save(Map<T,G> interestMap,String file){
        JSONObject interest = new JSONObject();
        interestMap.forEach(interest::put);
        try(PrintWriter pw = new PrintWriter(file)){
            pw.write(interest.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all the data from a .json
     * @param file the route of the file
     * @param <T> the class of the parameter 1
     * @param <G> the class of the parameter 2
     * @return a Map with all data
     */
    public static <T,G> Map<T, G> read(String file){
        Map<T, G> interestMap = new HashMap<>();
        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject =  (JSONObject) obj;
            //interestMap.put("interested", (List<String>) jsonObject.get("interested"));
            //interestMap.put("notInterested", (List<String>) jsonObject.get("notInterested"));
            jsonObject.keySet().forEach(key -> {
                interestMap.put((T) key, (G) jsonObject.get(key));
            });


        } catch (IOException | ParseException e) {
        }

        return interestMap;
    }
}

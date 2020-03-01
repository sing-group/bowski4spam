package org.bowssf;

import org.bowssf.gui.BowssfGUI;
import org.bowssf.util.BabelUtils;
import org.bowssf.util.JedisTree;


import javax.swing.*;
import java.io.*;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {



        //System.out.println(JedisSynsetCache.getSynsetFromWord("thing"));

        /*BabelNet bn = BabelNet.getInstance();
        BabelSynset by = bn.getSynset(new WordNetSynsetID("wn:00047243a"));
        System.out.println(by.getID());*/

        /*File archivo = new File("wordnetID.txt");
        FileReader fr = new FileReader (archivo);
        FileWriter writer = new FileWriter("wordnetIDBabel.txt");
        BufferedReader br = new BufferedReader(fr);

        BabelNet bn = BabelNet.getInstance();
        // Lectura del fichero
        String linea;
        while((linea=br.readLine())!=null) {
            String pos = linea.substring(0,1);
            String babel = String.format("wn:%08d%s", Integer.parseInt(linea.substring(2)),pos);
            BabelSynset by = bn.getSynset(new WordNetSynsetID(babel));
            writer.write(String.format("%s | %s",linea,by.getID()));
            writer.write("\r\n");
        }
        writer.close();

        //Loa patterns del moncho
        //patternsynsetMoncho="^[^,]*(?=,)";
        //patterndefinitionMoncho="(?<=,).*$";

        /*System.out.println(JedisSynsetCache.getSynsetFromWord());
        String patternString = ",";
        Pattern pattern = Pattern.compile(patternString);

        String[] split = pattern.split(JedisSynsetCache.getWordFromSynset());

        System.out.println("split.length = " + split.length);

        //BabelUtils.setSynsetWordValue("hola");

        /*String text = "A ,Separators";


        ;*/

    }
}

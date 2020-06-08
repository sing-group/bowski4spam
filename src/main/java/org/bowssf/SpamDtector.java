package org.bowssf;

import org.apache.commons.cli.*;
import org.bowssf.util.JsonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SpamDtector {
    public static void main(String[] args) {


        Options options = new Options();
        Option input = new Option("d", "directory", true, "directory file path");
        input.setRequired(false);
        options.addOption(input);

        Option output = new Option("s", "stadistic", false, "show percentage");
        output.setRequired(false);
        options.addOption(output);

        Option help = new Option("h", "help", false, "show help");
        help.setRequired(false);
        options.addOption(help);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        boolean directoyOption = false;
        String directory = null;

        try {
            cmd = parser.parse(options, args);
            directory =cmd.getOptionValue("directory");
            directoyOption = cmd.getOptionValue("directory") != null;
            if (cmd.hasOption("help")){
                formatter.printHelp("utility-name",options);
                System.exit(1);
            }
            if( !directoyOption && cmd.getArgs().length != 1){
                throw new MissingArgumentException("Falta un fichero");
            }
            File file = null;
            if(directoyOption){
                if(cmd.getArgs().length != 0) throw new IOException("No se admiten tanto argumentos");
                file = new File(directory);
                if (!file.isDirectory()) throw new IOException(directory + " no es un directorio");
            }else{
               file = new File(cmd.getArgs()[0]);
                if(!file.isFile()) throw new IOException(cmd.getArgs()[0] +" no es un fichero");
            }

        } catch (MissingArgumentException | IOException e){
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);

        }
        Map<String,Double> map = JsonUtils.read("prueba.json");

        List<String> files = new ArrayList<>();
        if(!directoyOption) files.add(cmd.getArgs()[0]);
        else{
            try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(path -> files.add(path.toString()));
            } catch (FileSystemException ignored) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(files);

        for (String pathFile : files) {
            List<String> mensaje = new ArrayList<>();
            Scanner sc2 = null;
            try {
                sc2 = new Scanner(new File(pathFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (sc2.hasNextLine()) {
                Scanner s2 = new Scanner(sc2.nextLine());
                while (s2.hasNext()) {
                    String s = s2.next();
                    mensaje.add(s);
                }
            }

            double numerador;
            final double cspam = 0.5;
            double productorioNum = 1.0;
            List <String> keys = new ArrayList<>(map.keySet());

            //numerador
            for (int i = 0; i< keys.size();i++){

                boolean isContained = mensaje.contains(keys.get(i));
                if(!isContained)
                    continue;

                double dContained = isContained?1.0:0.0;
                double value = map.get(keys.get(i));


                double probSpam = Math.pow(1-value,dContained);
                double probHam = Math.pow(value,1-dContained);
                productorioNum *= probSpam * probHam;


            }

            numerador = cspam * productorioNum;

            //productorio cs
            double productorioCHDe = 1.0;
            double denom;

            //productorio ch
            for (int i = 0; i< keys.size();i++){
                boolean isContained = mensaje.contains(keys.get(i));
                if(!isContained)
                    continue;
                double dContained = isContained?1.0:0.0;
                double value = map.get(keys.get(i));
                double probHam = Math.pow(value,dContained);
                double probSpam = Math.pow(1-value,1-dContained);

                productorioCHDe *= probHam *probSpam;

            }
            productorioCHDe *= 1-cspam;

            denom = productorioCHDe + numerador;

            double result = numerador/denom;

            StringBuilder out = new StringBuilder();
            out.append("Fichero: ").append(pathFile);
            if(cmd.hasOption("stadistic")){
                out.append(" Probabilidad: ").append(result);
            }
            if(result >= 0.5){
                out.append(" Spam");
            }else{
                out.append(" Ham");
            }

            System.out.println(out.toString());


        }







    }
}

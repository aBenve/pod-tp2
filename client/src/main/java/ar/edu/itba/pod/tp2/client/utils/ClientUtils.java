package ar.edu.itba.pod.tp2.client.utils;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Supplier;

public class ClientUtils {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(ClientUtils.class);

    public static Optional<String> getProperty(InputProperty label, Supplier<String> errorMessage){
        String property = System.getProperty(label.getLabel());
        if(property == null){
            logger.error(errorMessage.get());
            System.exit(1);
            return Optional.empty();
        }
        return Optional.of(property);
    }


    public static void writeQuery1(String outPath, List<Map.Entry<String, Integer>> data) {
        StringBuilder answer = new StringBuilder();

        answer.append("station;trips\n");
        data.forEach((entry) -> {
            answer.append(entry.getKey()).append(";")
                    .append(entry.getValue()).append("\n");
        });


        createFile(outPath, answer.toString(), "query1.csv");
    }


    /*
        start_station;end_station;start_date;end_date;distance;speed
        Parc du Pélican (1ère avenue / Masson);Marché Atwater;21/10/2021 16:27:24;21/10/2021 16:29:45;7.23;184.43
        St-Hubert / Duluth;de Mentana / Marie-Anne;09/08/2022 17:24:40;09/08/2022 17:24:47;7.22;184.43
     */
    public static void writeQuery2(String outPath, List<Map.Entry<String, SecondQueryOutputData>> data) {
        StringBuilder answer = new StringBuilder();

        answer.append("start_station;end_station;start_date;end_date;distance;speed\n");
        data.forEach((entry) -> {
            answer.append(entry.getKey()).append(";")
                    .append(entry.getValue().destination()).append(";")
                    .append(entry.getValue().startDate()).append(";")
                    .append(entry.getValue().endDate()).append(";")
                    .append(entry.getValue().distance()).append(";")
                    .append(entry.getValue().velocity()).append("\n");
        });



        createFile(outPath, answer.toString(), "query2.csv");
    }

    private static void createFile(String outPath, String answer, String fileName) {
        File outFile = new File(outPath + fileName);
        try {
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            writer.write(answer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while creating file");
        }
    }
}
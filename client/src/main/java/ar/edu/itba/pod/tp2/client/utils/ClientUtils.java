package ar.edu.itba.pod.tp2.client.utils;

import ar.edu.itba.pod.tp2.Models.SecondQueryOutputData;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
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


    public static void writeQuery1(String outPath, Map<String, Integer> data) {
        StringBuilder answer = new StringBuilder();

        answer.append("station;trips\n");
        data.forEach((key, value) -> {
            answer.append(key).append(";")
                    .append(value).append("\n");
        });

        createFile(outPath, answer.toString());
    }


    /*
        start_station;end_station;start_date;end_date;distance;speed
        Parc du Pélican (1ère avenue / Masson);Marché Atwater;21/10/2021 16:27:24;21/10/2021 16:29:45;7.23;184.43
        St-Hubert / Duluth;de Mentana / Marie-Anne;09/08/2022 17:24:40;09/08/2022 17:24:47;7.22;184.43
     */
    public static void writeQuery2(String outPath, Map<String, SecondQueryOutputData> data) {
        StringBuilder answer = new StringBuilder();

        answer.append("start_station;end_station;start_date;end_date;distance;speed\n");
        data.forEach((key, value) -> {
            answer.append(key).append(";")
                    .append(value.destination()).append(";")
                    .append(value.startDate()).append(";")
                    .append(value.endDate()).append(";")
                    .append(value.distance()).append(";")
                    .append(value.velocity()).append("\n");
        });

        createFile(outPath, answer.toString());
    }

    private static void createFile(String outPath, String answer) {
        File outFile = new File(outPath + "out.csv");
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
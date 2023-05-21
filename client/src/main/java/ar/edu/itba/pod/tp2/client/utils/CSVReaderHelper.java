package ar.edu.itba.pod.tp2.client.utils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CSVReaderHelper {

    private static final String BIKES = "bikes.csv";
    private static final String STATIONS = "stations.csv";

    private final String path;
    private final char delimiter;
    public CSVReaderHelper(String path, char delimiter) {
        this.path = path;
        this.delimiter = delimiter;
    }


    public List<String[]> getBikesData(){
        return getData(this.path + BIKES);
    }

    public List<String[]> getStationsData(){
        return getData(this.path + STATIONS);
    }

    private List<String[]> getData(String path){
        FileReader filereader = null;
        try {
            filereader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(this.delimiter)
                .build();
        try(CSVReader csvReader = new CSVReaderBuilder(filereader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build()) {
            return csvReader.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file");
        }
    }

    public String getPath() {
        return path;
    }

    public static String getBikesPath() {
        return BIKES;
    }

    public static String getStationsPath() {
        return STATIONS;
    }


}

package ar.edu.itba.pod.tp2.client.utils;

import ar.edu.itba.pod.tp2.Models.Bike;
import ar.edu.itba.pod.tp2.Models.Coordinates;
import ar.edu.itba.pod.tp2.Models.Station;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReaderHelper {

    private static final String BIKES = "bikes.csv";
    private static final String STATIONS = "stations.csv";

    private static final Integer BIKES_LENGTH = 5;
    private static final Integer STATIONS_LENGTH = 4;
    private final String path;

    private final char delimiter;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CSVReaderHelper(String path, char delimiter) {
        this.path = path;
        this.delimiter = delimiter;
    }


    public Map<Integer, Bike> getBikesData(){
        List<String[]> data =  getData(this.path + BIKES);
        Map<Integer, Bike> bikes = new HashMap<>();

        final Integer[] index = {0};
        data.forEach((row) -> {
            if(row.length != BIKES_LENGTH)
                throw new RuntimeException("Invalid csv length");

            bikes.put(
                    index[0],
                    new Bike(
                        LocalDateTime.parse(row[0],formatter).toLocalDate(),
                        LocalDateTime.parse(row[2],formatter).toLocalDate(),
                        Integer.parseInt(row[1]),
                        Integer.parseInt(row[3]),
                        Boolean.parseBoolean(row[4])
                    )
            );
            index[0]++;
        });

        return bikes;
    }

    public Map<Integer, Station> getStationsData(){
        List<String[]> data =  getData(this.path + STATIONS);
        Map<Integer, Station> stations = new HashMap<>();

        data.forEach((row) -> {
            if(row.length != STATIONS_LENGTH)
                throw new RuntimeException("Invalid csv length");

            stations.put(
                    Integer.parseInt(row[0])
                    ,new Station(
                        Integer.parseInt(row[0]),
                        row[1],
                        new Coordinates(Double.parseDouble(row[2]), Double.parseDouble(row[3]))
                    )
            );
        });

        return stations;
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
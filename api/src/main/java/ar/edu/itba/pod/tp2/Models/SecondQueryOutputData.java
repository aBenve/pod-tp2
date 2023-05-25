package ar.edu.itba.pod.tp2.Models;

import java.io.Serializable;
import java.time.LocalDateTime;

public record SecondQueryOutputData(String destination, Double distance, Double velocity, LocalDateTime startDate,
                                    LocalDateTime endDate) implements Serializable {
}

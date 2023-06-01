package ar.edu.itba.pod.tp2.Models;

import java.io.Serializable;

public record TravelIdStationsAndMember(int id, Integer origin, Integer destination, boolean member) implements Serializable {

}

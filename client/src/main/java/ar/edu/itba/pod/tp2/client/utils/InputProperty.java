package ar.edu.itba.pod.tp2.client.utils;

public enum InputProperty {
    ADDRESSES("addresses"),
    IN_PATH("inPath"),
    OUT_PATH("outPath"),

    AMOUNT_OF_STATIONS("n"),
    ;

    private final String label;
    InputProperty(String label) {
        this.label = label;
    }

    String getLabel() {
        return this.label;
    }
}

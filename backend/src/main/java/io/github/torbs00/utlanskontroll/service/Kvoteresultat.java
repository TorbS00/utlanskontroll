package io.github.torbs00.utlanskontroll.service;

public record Kvoteresultat(
        String segment,
        int antallLan,
        long totalVolum,
        long bruddVolum,
        double bruddAndel,
        double grense,
        boolean overGrense
) {
    public Kvoteresultat(String segment, int antallLan, long totalVolum,
                         long bruddVolum, double bruddAndel, double grense) {
        this(segment, antallLan, totalVolum, bruddVolum, bruddAndel, grense,
                bruddAndel > grense);
    }
}
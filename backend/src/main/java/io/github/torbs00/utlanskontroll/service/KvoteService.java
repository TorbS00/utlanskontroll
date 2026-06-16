package io.github.torbs00.utlanskontroll.service;

import io.github.torbs00.utlanskontroll.domain.Lan;
import io.github.torbs00.utlanskontroll.repository.LanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KvoteService {

    private static final double GRENSE_OSLO = 0.08;
    private static final double GRENSE_ELLERS = 0.10;

    private final LanRepository repository;

    public KvoteService(LanRepository repository) {
        this.repository = repository;
    }

    public List<Lan> finnAlleLan() {
        return repository.finnAlle();
    }

    public List<Kvoteresultat> beregnKvoter() {
        Map<Boolean, List<Lan>> etterOmrade = repository.finnAlle().stream()
                .collect(Collectors.partitioningBy(Lan::oslo));

        return List.of(
                beregnSegment("Oslo", etterOmrade.get(true), GRENSE_OSLO),
                beregnSegment("Resten av landet", etterOmrade.get(false), GRENSE_ELLERS)
        );
    }

    private Kvoteresultat beregnSegment(String navn, List<Lan> lan, double grense) {
        long totalVolum = lan.stream()
                .mapToLong(Lan::bevilgetBelop)
                .sum();

        long bruddVolum = lan.stream()
                .filter(Lan::harBrudd)
                .mapToLong(Lan::bevilgetBelop)
                .sum();

        double andel = totalVolum == 0 ? 0.0 : (double) bruddVolum / totalVolum;
        return new Kvoteresultat(navn, lan.size(), totalVolum, bruddVolum, andel, grense);
    }
}
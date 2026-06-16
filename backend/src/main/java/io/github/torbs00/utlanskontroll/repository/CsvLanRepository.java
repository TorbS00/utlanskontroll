package io.github.torbs00.utlanskontroll.repository;

import io.github.torbs00.utlanskontroll.domain.Lan;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class CsvLanRepository implements LanRepository {

    private static final DateTimeFormatter DATO =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final List<Lan> lan;

    public CsvLanRepository() {
        this.lan = lastFraCsv();
    }

    @Override
    public List<Lan> finnAlle() {
        return lan;
    }

    private List<Lan> lastFraCsv() {
        try (var input = getClass().getResourceAsStream("/utlan_q2_2026.csv");
             var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .skip(1)
                    .map(this::parse)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Klarte ikke å lese utlånsdata", e);
        }
    }

    private Lan parse(String linje) {
        String[] f = linje.split(",");
        return new Lan(
                Integer.parseInt(f[0]),
                LocalDate.parse(f[1], DATO),
                Long.parseLong(f[2]),
                f[3].equals("1"),
                f[4].equals("1"),
                f[5].equals("1"),
                f[6].equals("1"),
                f[7].equals("1")
        );
    }
}

package io.github.torbs00.utlanskontroll.domain;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

public record Lan (
        int kundenr,
        LocalDate bevilgetDato,
        long bevilgetBelop,
        boolean bruddLikviditet,
        boolean bruddGjeldsgrad,
        boolean bruddLTV,
        boolean bruddAvdrag,
        boolean oslo
) {
    public boolean harBrudd() {
        return bruddLikviditet || bruddGjeldsgrad || bruddLTV || bruddAvdrag;
    }

    public Set<Bruddtype> bruddTyper() {
        var typer = EnumSet.noneOf(Bruddtype.class);
        if (bruddLikviditet) typer.add(Bruddtype.LIKVIDITET);
        if (bruddGjeldsgrad) typer.add(Bruddtype.GJELDSGRAD);
        if (bruddLTV)        typer.add(Bruddtype.LTV);
        if (bruddAvdrag)     typer.add(Bruddtype.AVDRAG);
        return typer;
    }
}


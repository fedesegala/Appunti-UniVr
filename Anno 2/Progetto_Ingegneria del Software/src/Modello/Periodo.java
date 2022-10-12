package Modello;

import java.time.LocalDate;

public class Periodo {
    private final LocalDate inizio;
    private final LocalDate fine;

    public Periodo(LocalDate inizio, LocalDate fine) {
        this.inizio = inizio;
        this.fine = fine;
    }

    public LocalDate getInizio() {
        return inizio;
    }

    public LocalDate getFine() {
        return fine;
    }

    @Override
    public String toString() {
        return inizio.toString() + "/" + fine.toString();
    }
}


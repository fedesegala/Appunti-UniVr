package Modello;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Ricerca {
    private final ArrayList<String> nome;
    private final ArrayList<String> cognome;
    private final ObservableList<Lingua> lingue;
    private final LocalDate inizio;
    private final LocalDate fine;
    private final String luogo;
    private final ArrayList<String> mansione;
    private final ObservableList<Patente> patente;
    private final boolean automunito;
    private final boolean ricercaAutomunito;
    private final boolean ricercaAnd;

    public Ricerca(String nome, String cognome, ObservableList<Lingua> lingue, LocalDate inizio, LocalDate fine,
                   String luogo, String mansione, ObservableList<Patente> patenti, boolean automunito, boolean ricercaAutomunito, boolean ricercaAnd) {

        if (!nome.equals("")) {
            this.nome = new ArrayList<>();
            Collections.addAll(this.nome, nome.split(","));
        } else {
            this.nome = null;
        }

        if (!cognome.equals("")) {
            this.cognome = new ArrayList<>();
            Collections.addAll(this.cognome, cognome.split(","));
        } else {
            this.cognome = null;
        }

        if (!lingue.isEmpty()) {
            this.lingue = FXCollections.observableList(lingue);
        }
        else {
            this.lingue = null;
        }

        if (inizio != null) {
            this.inizio = LocalDate.of(inizio.getYear(), inizio.getMonth(), inizio.getDayOfMonth());
        } else {
            this.inizio = null;
        }

        if (fine != null) {
            this.fine = LocalDate.of(fine.getYear(), fine.getMonth(), fine.getDayOfMonth());
        } else {
            this.fine = null;
        }

        if (!luogo.equals("")) {
            this.luogo = luogo;
        } else {
            this.luogo = null;
        }

        if (!mansione.equals("")) {
            this.mansione = new ArrayList<>();
            Collections.addAll(this.mansione, mansione.split(","));
        } else {
            this.mansione = null;
        }

        if (!patenti.isEmpty()) {
            this.patente = FXCollections.observableArrayList(patenti);
        } else {
            this.patente = null;
        }

        this.ricercaAnd = ricercaAnd;
        this.ricercaAutomunito = ricercaAutomunito;
        this.automunito = automunito;


    }

    public ArrayList<String> getNome() {
        return nome;
    }


    public ArrayList<String> getCognome() {
        return cognome;
    }


    public ObservableList<Lingua> getLingue() {
        return lingue;
    }


    public LocalDate getInizio() { return inizio; }

    public LocalDate getFine() { return fine; }


    public String getLuogo() {
        return luogo;
    }


    public ArrayList<String> getMansione() {
        return mansione;
    }


    public ObservableList<Patente> getPatente() {
        return patente;
    }


    public boolean isAutomunito() {
        return automunito;
    }

    public boolean isRicercaAnd() {
        return ricercaAnd;
    }

    public boolean isRicercaAutomunito() {
        return ricercaAutomunito;
    }
}

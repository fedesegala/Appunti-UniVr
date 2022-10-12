package Vista;

import Modello.Disponibilita;

/**
 * Classe per visualizzare la disponibilità nella tabella
 */
public class DisponibilitaTableView {
    private String inizio;
    private String fine;
    private String comune;
    private Disponibilita ID;

    public DisponibilitaTableView(String inizio, String fine, String comune, Disponibilita ID) {
        this.inizio = inizio;
        this.fine = fine;
        this.comune = comune;
        this.ID = ID;
    }

    public String getInizio() {
        return inizio;
    }

    public void setInizio(String inizio) {
        this.inizio = inizio;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public Disponibilita getID() {
        return ID;
    }

    public void setID(Disponibilita ID) {
        this.ID = ID;
    }
}

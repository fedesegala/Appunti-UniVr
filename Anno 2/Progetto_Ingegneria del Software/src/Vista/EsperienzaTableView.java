package Vista;


import Modello.EsperienzaLavorativa;

/**
 * classe per la visualizzazione delle esperienze nella tabella
 */
public class EsperienzaTableView {
    private final String inizio;
    private final String fine;
    private final String azienda;
    private final String mansioni;
    private final String retribuzioneLorda;
    private final String retribuzioneGiornaliera;
    private final String comunesvolgimento;
    private final EsperienzaLavorativa ID;

    public EsperienzaTableView(String inizio, String fine, String azienda, String mansioni, String retribuzioneLorda, String retribuzioneGiornaliera, String comunesvolgimento, EsperienzaLavorativa ID) {
        this.inizio = inizio;
        this.fine = fine;
        this.azienda = azienda;
        this.mansioni = mansioni;
        this.retribuzioneLorda = retribuzioneLorda;
        this.retribuzioneGiornaliera = retribuzioneGiornaliera;
        this.comunesvolgimento = comunesvolgimento;
        this.ID = ID;
    }

    public String getInizio() {
        return inizio;
    }

    public String getFine() {
        return fine;
    }

    public String getAzienda() {
        return azienda;
    }

    public String getMansioni() {
        return mansioni;
    }

    public String getRetribuzioneLorda() {
        return retribuzioneLorda;
    }

    public String getRetribuzioneGiornaliera() {
        return retribuzioneGiornaliera;
    }

    public String getComunesvolgimento() {
        return comunesvolgimento;
    }

    public EsperienzaLavorativa getID() {
        return ID;
    }
}

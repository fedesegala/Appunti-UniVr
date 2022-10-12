package Modello;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Lavoratore extends Persona {
    private Comune comuneResidenza;
    private String indirizzo;
    private String recapitoTelefonico;
    private String specializzazioni;
    private boolean automunito;
    private List<EsperienzaLavorativa> esperienzeLavorative;
    private List<Patente> patenti;
    private List<Lingua> lingueParlate;
    private List<Disponibilita> disponibilita;
    private List<ContattoEmergenza> conttattiEmergenza;


    public Lavoratore(String nome, String cognome, String email, Comune luogoNascita, LocalDate dataNascita,
                      String nazionalita, Comune comuneResidenza,String indirizzo, String recapitoTelefonico,
                      String specializzazioni, boolean automunito, List<EsperienzaLavorativa> esperienzeLavorative,
                      List<Patente> patenti,List<Lingua> lingueParlate,List<Disponibilita> disponibilita, List<ContattoEmergenza> conttattiEmergenza) {

        super(nome, cognome, email, luogoNascita, dataNascita, nazionalita);

        this.comuneResidenza = comuneResidenza;
        this.indirizzo = indirizzo;
        this.recapitoTelefonico = recapitoTelefonico;
        this.specializzazioni = specializzazioni;
        this.automunito = automunito;

        this.esperienzeLavorative = new ArrayList<EsperienzaLavorativa>();
        if (esperienzeLavorative != null) {
            for (EsperienzaLavorativa e : esperienzeLavorative){
                this.esperienzeLavorative.add(e);
            }
        }

        this.lingueParlate = new ArrayList<Lingua>();
        if (lingueParlate != null){
            for (Lingua l : lingueParlate) {
                this.lingueParlate.add(l);
            }
        }

        this.patenti = new ArrayList<Patente>();
        if (patenti != null) {
            for (Patente p : patenti) {
                this.patenti.add(p);
            }
        }

        this.conttattiEmergenza = new ArrayList<ContattoEmergenza>();
        if (conttattiEmergenza != null) {
            for (ContattoEmergenza c : conttattiEmergenza) {
                this.conttattiEmergenza.add(c);
            }
        }

        this.disponibilita = new ArrayList<Disponibilita>();
        if (disponibilita != null) {
            for (Disponibilita d : disponibilita) {
                this.disponibilita.add(d);
            }
        }

    }

    public Lavoratore () {
        super();
    }

    public Comune getComuneResidenza() {
        return comuneResidenza;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getRecapitoTelefonico() {
        return recapitoTelefonico;
    }

    public List<Lingua> getLingueParlate() {
        return lingueParlate;
    }

    public List<Disponibilita> getDisponibilita() {
        return disponibilita;
    }

    public String getSpecializzazioni() {
        return specializzazioni;
    }

    public List<EsperienzaLavorativa> getEsperienzeLavorative() {
        return esperienzeLavorative;
    }

    public List<Patente> getPatenti() {
        return patenti;
    }

    public List<ContattoEmergenza> getConttattiEmergenza() {
        return conttattiEmergenza;
    }

    public boolean isAutomunito() {
        return automunito;
    }

    public void setComuneResidenza(Comune comuneResidenza) {
        this.comuneResidenza = comuneResidenza;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setRecapitoTelefonico(String recapitoTelefonico) {
        this.recapitoTelefonico = recapitoTelefonico;
    }

    public void setLingueParlate(List<Lingua> lingueParlate) {
        this.lingueParlate = lingueParlate;
    }

    public void setDisponibilita(List<Disponibilita> disponibilita) {
        this.disponibilita = disponibilita;
    }

    public void setSpecializzazioni(String specializzazioni) {
        this.specializzazioni = specializzazioni;
    }

    public void setEsperienzeLavorative(List<EsperienzaLavorativa> esperienzeLavorative) {
        this.esperienzeLavorative = esperienzeLavorative;
    }

    public void setPatenti(List<Patente> patenti) {
        this.patenti = patenti;
    }

    public void setAutomunito(boolean automunito) {
        this.automunito = automunito;
    }

    public void setConttattiEmergenza(List<ContattoEmergenza> conttattiEmergenza) {
        this.conttattiEmergenza = conttattiEmergenza;
    }

    @Override
    public String toString() {
        String result = super.toString() + ";" + comuneResidenza.toString() + ";" + indirizzo + ";" + recapitoTelefonico + ";" + ((automunito)? "true" : "false") + ";";
        if (!lingueParlate.isEmpty()){
            for (Lingua l : lingueParlate) {
                result += l.toString() + "/";
            }
        } else {
            result += "null";
        }
        result += ";";
        if (!patenti.isEmpty()) {
            for (Patente p : patenti) {
                result += p.toString() + "/";
            }
        } else {
            result += "null";
        }
        result += "\n";

        return result;
    }

}
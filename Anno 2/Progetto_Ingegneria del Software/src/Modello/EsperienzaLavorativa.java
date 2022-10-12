package Modello;

import java.util.ArrayList;
import java.util.List;

public class EsperienzaLavorativa {
    private Periodo periodoSvolgimento;
    private String azienda;
    private List<Mansione> mansioni;
    private Comune luogoSvolgimento;
    private float retribuzioneLorda;
    private float retribuzioneNetta;

    public EsperienzaLavorativa(Periodo periodoSvolgimento, String azienda, List<Mansione> mansioni, Comune luogoSvolgimento, float retribuzioneLorda, float retribuzioneNetta) {
        this.periodoSvolgimento = periodoSvolgimento;
        this.azienda = azienda;
        this.luogoSvolgimento = luogoSvolgimento;
        this.retribuzioneLorda = retribuzioneLorda;
        this.retribuzioneNetta = retribuzioneNetta;
        this.mansioni = new ArrayList<Mansione>();
        for (Mansione m : mansioni) {
            this.mansioni.add(m);
        }
    }
    public EsperienzaLavorativa(EsperienzaLavorativa esp) {
        this.periodoSvolgimento = esp.getPeriodoSvolgimento();
        this.azienda = esp.getAzienda();
        this.mansioni = new ArrayList<Mansione>();
        this.mansioni.addAll(esp.getMansioni());
        this.luogoSvolgimento = esp.getLuogoSvolgimento();
        this.retribuzioneLorda = esp.getRetribuzioneLorda();
        this.retribuzioneNetta = esp.getRetribuzioneNetta();
    }

    public Periodo getPeriodoSvolgimento() {
        return periodoSvolgimento;
    }

    public String getAzienda() {
        return azienda;
    }

    public List<Mansione> getMansioni() {
        return mansioni;
    }

    public Comune getLuogoSvolgimento() {
        return luogoSvolgimento;
    }

    public float getRetribuzioneLorda() {
        return retribuzioneLorda;
    }

    public float getRetribuzioneNetta() {
        return retribuzioneNetta;
    }

    public void setPeriodoSvolgimento(Periodo periodoSvolgimento) {
        this.periodoSvolgimento = periodoSvolgimento;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    public void setMansioni(List<Mansione> mansioni) {
        this.mansioni = mansioni;
    }

    public void setLuogoSvolgimento(Comune luogoSvolgimento) {
        this.luogoSvolgimento = luogoSvolgimento;
    }

    public void setRetribuzioneLorda(float retribuzioneLorda) {
        this.retribuzioneLorda = retribuzioneLorda;
    }


    public void setRetribuzioneGiornaliera(float retribuzioneGiornaliera) {
        this.retribuzioneNetta = retribuzioneGiornaliera;
    }

    @Override
    public String toString() {
        String result = periodoSvolgimento.toString() + ";" + azienda + ";";
        for (Mansione m : mansioni){
            result += m.toString() + "/";
        }
        result += ";" + luogoSvolgimento.toString() + ";" + retribuzioneLorda + ";" + retribuzioneNetta + "\n";
        return result;
    }
}

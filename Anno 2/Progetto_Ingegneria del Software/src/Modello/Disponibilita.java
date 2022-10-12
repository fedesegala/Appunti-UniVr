package Modello;

public class Disponibilita {
    private Periodo periodoDisponibilita;
    private Comune luogoDisponibilita;

    public Disponibilita(Periodo periodoDisponibilita, Comune luogoDisponibilita) {
        this.periodoDisponibilita = periodoDisponibilita;
        this.luogoDisponibilita = luogoDisponibilita;
    }
    public Disponibilita(Disponibilita d) {
        this.periodoDisponibilita = d.getPeriodoDisponibilita();
        this.luogoDisponibilita = d.getLuogoDisponibilita();
    }

    public Periodo getPeriodoDisponibilita() {
        return periodoDisponibilita;
    }

    public Comune getLuogoDisponibilita() {
        return luogoDisponibilita;
    }

    public void setPeriodoDisponibilita(Periodo periodoDisponibilita) {
        this.periodoDisponibilita = periodoDisponibilita;
    }

    public void setLuogoDisponibilita(Comune luogoDisponibilita) {
        this.luogoDisponibilita = luogoDisponibilita;
    }

    @Override
    public String toString() {
        return periodoDisponibilita.toString() + ";" + luogoDisponibilita.toString() + "\n";
    }
}

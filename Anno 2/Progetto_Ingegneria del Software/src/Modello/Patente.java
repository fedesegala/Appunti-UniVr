package Modello;

public class Patente {
    private final String patente;

    public Patente(String patente) {
        this.patente = patente;
    }

    public String getPatente() {
        return patente;
    }

    @Override
    public String toString() {
        return patente;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Patente))
            return false;
        else return this.patente.equals(obj.toString());
    }
}

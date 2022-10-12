package Modello;

public class Mansione {
    private final String mansione;

    public Mansione(String mansione) {
        this.mansione = mansione;
    }

    public String getMansione() {
        return mansione;
    }

    @Override
    public String toString() {
        return mansione;
    }

}

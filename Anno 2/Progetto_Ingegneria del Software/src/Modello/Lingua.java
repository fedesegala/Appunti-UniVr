package Modello;

public class Lingua {
    private String lingua;

    public Lingua(String lingua){
        this.lingua = lingua;
    }
    public String getLingua() {return this.lingua;}

    @Override
    public String toString() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Lingua))
            return false;

        return (this.lingua.equals(((Lingua) obj).getLingua()));
    }
}

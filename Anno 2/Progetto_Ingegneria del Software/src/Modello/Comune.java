package Modello;

/**
 * Classe rappresentante un comune
 */
public class Comune {
    private final String nome;
    private final String provincia;
    private final String regione;
    private String sigla;
    private static final String fileName = "src/DataBase/AreeGeografiche.txt";


    public Comune () {
        nome = "Estero";
        provincia = null;
        regione = null;
    }
    
    public Comune (String nome, String provincia, String regione) {
        this.nome = nome;
        this.provincia = provincia;
        this.regione = regione;

    }

    @Override
    public String toString() {
        return nome + "," + provincia + "," + regione;
    }

    public String getNome() {
        return nome;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getRegione() {
        return regione;
    }
}
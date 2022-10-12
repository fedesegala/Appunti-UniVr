package Modello;

public class ContattoEmergenza {
    private String nome;
    private String cognome;
    private String email;
    private String recapitoTelefonico;

    public ContattoEmergenza(String nome, String cognome, String email, String recapitoTelefonico) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.recapitoTelefonico = recapitoTelefonico;
    }

    public ContattoEmergenza(ContattoEmergenza c) {
        this.nome = c.getNome();
        this.cognome = c.getCognome();
        this.email = c.getEmail();
        this.recapitoTelefonico = c.getRecapitoTelefonico();
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getRecapitoTelefonico() {
        return recapitoTelefonico;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRecapitoTelefonico(String recapitoTelefonico) {
        this.recapitoTelefonico = recapitoTelefonico;
    }

    @Override
    public String toString() {
        return nome + ";" + cognome + ";" + email + ";" + recapitoTelefonico + "\n";
    }
}

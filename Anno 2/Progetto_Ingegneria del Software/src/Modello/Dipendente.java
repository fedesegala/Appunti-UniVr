package Modello;

import java.time.LocalDate;

public class Dipendente extends Persona{
    private final String username;
    private final String password;

    public Dipendente (String nome, String cognome, String email, Comune luogoNascita, LocalDate dataNascita,
                       String nazionalita, String username, String password){

        super(nome, cognome, email, luogoNascita, dataNascita, nazionalita);
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

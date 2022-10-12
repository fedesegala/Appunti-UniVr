package Modello;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Persona {
    private String nome;
    private String cognome;
    private String email;
    private Comune luogoNascita;
    private LocalDate dataNascita;
    private String nazionalita;


    public Persona (String nome, String cognome, String email, Comune luogoNascita, LocalDate dataNascita, String nazionalita) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.luogoNascita = luogoNascita;
        this.dataNascita = dataNascita;
        this.nazionalita = nazionalita;
    }


    public Persona() {}

    // metodi set
    public void setNome(String nome) {this.nome = nome;}
    public void setCognome(String cognome) {this.cognome = cognome;}
    public void setEmail(String email) {this.email = email;}
    public void setLuogoNascita(Comune luogoNascita) {this.luogoNascita = luogoNascita;}
    public void setDataNascita(LocalDate dataNascita) {this.dataNascita = dataNascita;}
    public void setNazionalita(String nazionalita) {this.nazionalita = nazionalita;}

    // metodi get
    public String getNome() {return this.nome;}
    public String getCognome() {return this.cognome;}
    public String getEmail() {return this.email;}
    public Comune getLuogoNascita() {return this.luogoNascita;}
    public LocalDate getDataNascita() {return this.dataNascita;}
    public String getNazionalita() {return this.nazionalita;}

    @Override
    public String toString() {
        return email + ";" + nome + ";" + cognome + ";" + luogoNascita.toString() + ";" + dataNascita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";" + nazionalita;
    }
}

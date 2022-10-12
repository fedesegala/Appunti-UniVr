package Modello;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Modello {
    // database
    static String dbLavoratori = "src/DataBase/Lavoratori.txt";
    static String dbEsperienze = "src/DataBase/EsperienzeLavorative.txt";
    static String dbDisponibilita = "src/DataBase/Disponibilita.txt";
    static String dbContattiEmergenza = "src/DataBase/ContattiEmergenza.txt";
    static String dbSpecializzazioni = "src/DataBase/Specializzazioni.txt";
    private static ArrayList<Lavoratore> lavoratori;
    private static Dipendente user;

    private static Modello instance = null;

    private Modello() {
        lavoratori = fetchLavoratori();
        for (Lavoratore l : lavoratori) {
            // aggiungo le specializzazioni al lavoratore
            l.setSpecializzazioni(fetchSpecializzazioni(l.getEmail()));
            // aggiungo le esperienze lavorative associate al lavoratore
            l.setEsperienzeLavorative(fetchEsperienze(l));
            // aggiungo le lingue parlate
            l.setLingueParlate(fetchLingue(l));
            // aggiungo le patenti
            l.setPatenti(fetchPatenti(l));
            // aggiungo le disponibilita
            l.setDisponibilita(fetchDisponibilita(l));
            // aggiungo i contatti di emergenza
            l.setConttattiEmergenza(fetchContattoEmergenza(l));
        }
    }

    public static Modello getInstance() {
        //return instance;
        if (instance == null)
            return new Modello();

        return instance;
    }

    private static ArrayList<Lavoratore> fetchLavoratori() {
        ArrayList<Lavoratore> lavoratori = new ArrayList<Lavoratore>();

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbLavoratori), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // per ogni linea creo un nuovo lavoratore
        for (String l : lines) {
            // recupero tutti i parametri
            String[] parameters = l.split(";");
            String nome = parameters[1];
            String cognome = parameters[2];
            String email = parameters[0];
            if (!email.equals("email")) {
                Comune comune = new Comune(
                        parameters[3].split(",")[0],
                        parameters[3].split(",")[1],
                        parameters[3].split(",")[2]
                );
                LocalDate nascita = LocalDate.of(
                        Integer.parseInt(parameters[4].split("/")[2]),
                        Integer.parseInt(parameters[4].split("/")[1]),
                        Integer.parseInt(parameters[4].split("/")[0])
                );
                String nazionalita = parameters[5];
                Comune residenza = new Comune(
                        parameters[6].split(",")[0],
                        parameters[6].split(",")[1],
                        parameters[6].split(",")[2]
                );
                String indirizzo = parameters[7];
                String recapito = parameters[8];
                String specializzazioni = "";
                boolean automunito = (parameters[9].equals("true"));
                List<EsperienzaLavorativa> esp = new ArrayList<EsperienzaLavorativa>();
                List<Patente> pat = new ArrayList<Patente>();
                List<Lingua> lingue = new ArrayList<Lingua>();
                List<Disponibilita> disp = new ArrayList<Disponibilita>();
                List<ContattoEmergenza> cont = new ArrayList<ContattoEmergenza>();


                // aggiungo un nuovo lavoratore
                lavoratori.add(new Lavoratore(
                        nome, cognome, email, comune, nascita, nazionalita,
                        residenza, indirizzo, recapito, specializzazioni,
                        automunito, esp, pat, lingue, disp, cont
                ));
            }

        }

        return lavoratori;
    }

    private static String fetchSpecializzazioni(String email) {

        ArrayList<String> specializzazioni = getSpecializzazioniDB();

        for (String s : specializzazioni) {
            if (!s.isEmpty()) {
                if (s.split("_")[0].equals(email)) {
                    return s.split("_")[1];
                }
            }
        }

        return "null";
    }

    private static ArrayList<EsperienzaLavorativa> fetchEsperienze(Lavoratore lavoratore) {
        ArrayList<EsperienzaLavorativa> esperienze = new ArrayList<EsperienzaLavorativa>();

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbEsperienze), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String l : lines) {
            String check = l.split(";")[0];
            if (check.equals(lavoratore.getEmail())) {
                String[] parameters = l.split(";");
                LocalDate inizio = LocalDate.of(
                        Integer.parseInt(parameters[1].split("/")[0].split("-")[0]),
                        Integer.parseInt(parameters[1].split("/")[0].split("-")[1]),
                        Integer.parseInt(parameters[1].split("/")[0].split("-")[2])
                );
                LocalDate fine = LocalDate.of(
                        Integer.parseInt(parameters[1].split("/")[1].split("-")[0]),
                        Integer.parseInt(parameters[1].split("/")[1].split("-")[1]),
                        Integer.parseInt(parameters[1].split("/")[1].split("-")[2])
                );
                String azienda = parameters[2];

                ArrayList<Mansione> mansioni = new ArrayList<Mansione>();
                for (String s : parameters[3].split("/")) {
                    mansioni.add(new Mansione(s));
                }

                Comune comune = new Comune(
                        parameters[4].split(",")[0],
                        parameters[4].split(",")[1],
                        parameters[4].split(",")[2]
                );

                float lordo = Float.parseFloat(parameters[5]);
                float giornaliero = Float.parseFloat(parameters[6]);

                esperienze.add(new EsperienzaLavorativa(
                        new Periodo(inizio, fine),
                        azienda,
                        mansioni,
                        comune,
                        lordo,
                        giornaliero
                ));

            }
        }

        return esperienze;

    }

    private static ArrayList<Lingua> fetchLingue(Lavoratore lavoratore) {
        ArrayList<Lingua> lingue = new ArrayList<Lingua>();

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbLavoratori), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (parameters[0].equals(lavoratore.getEmail())) {
                for (String lingua : parameters[10].split("/")) {
                    if (!lingua.equals("null"))
                        lingue.add(new Lingua(lingua));
                }
            }
        }

        return lingue;
    }

    private static ArrayList<Patente> fetchPatenti(Lavoratore lavoratore) {
        ArrayList<Patente> patenti = new ArrayList<Patente>();

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbLavoratori), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (parameters[0].equals(lavoratore.getEmail())) {
                for (String patente : parameters[11].split("/")) {
                    if (!patente.equals("null"))
                        patenti.add(new Patente(patente));
                }
            }
        }

        return patenti;
    }

    private static ArrayList<Disponibilita> fetchDisponibilita(Lavoratore lavoratore) {
        ArrayList<Disponibilita> disponibilita = new ArrayList<Disponibilita>();

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbDisponibilita), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (parameters[0].equals(lavoratore.getEmail())) {
                String[] period = parameters[1].split("/");
                LocalDate inizio = LocalDate.of(
                        Integer.parseInt(period[0].split("-")[0]),
                        Integer.parseInt(period[0].split("-")[1]),
                        Integer.parseInt(period[0].split("-")[2])
                );
                LocalDate fine = LocalDate.of(
                        Integer.parseInt(period[1].split("-")[0]),
                        Integer.parseInt(period[1].split("-")[1]),
                        Integer.parseInt(period[1].split("-")[2])
                );

                disponibilita.add(new Disponibilita(
                        new Periodo(inizio, fine),
                        new Comune(parameters[2].split(",")[0], parameters[2].split(",")[1], parameters[2].split(",")[2])
                ));
            }
        }

        return disponibilita;
    }

    private static ArrayList<ContattoEmergenza> fetchContattoEmergenza(Lavoratore lavoratore) {
        ArrayList<ContattoEmergenza> contatti = new ArrayList<ContattoEmergenza>();

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbContattiEmergenza), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (lavoratore.getEmail().equals(parameters[0])) {
                contatti.add(new ContattoEmergenza(
                        parameters[1], parameters[2], parameters[3], parameters[4]
                ));
            }

        }

        return contatti;
    }

    private static ArrayList<String> getSpecializzazioniDB() {
        String total = "";
        ArrayList<String> specializzazioni = new ArrayList<>();

        try {
            total = new String(Files.readAllBytes(Paths.get(dbSpecializzazioni)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String[] parameters = total.split("_\n");

        Collections.addAll(specializzazioni, parameters);

        return specializzazioni;
    }

    public ArrayList<Lavoratore> getElencoLavoratori() {
        return lavoratori;
    }

    public void cancelLavoratore(Lavoratore lavoratore) {
        try {
            for (Lavoratore l : lavoratori) {
                if (l.getEmail().equals(lavoratore.getEmail()))
                    lavoratori.remove(l);
            }
        } catch (ConcurrentModificationException e) {
        }

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbLavoratori), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> toWrite = new ArrayList<String>();

        // cancello da dblavoratori
        for (String l : lines) {
            String[] parameters = l.split(";");
            if (!parameters[0].equals(lavoratore.getEmail())) {
                toWrite.add(l);
            }
        }

        try (Writer writer = new BufferedWriter(new FileWriter(dbLavoratori, false))) {
            for (String l : toWrite) {
                writer.write(l + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }

        // cancello da dbContattiEmergenza
        lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbContattiEmergenza), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        toWrite = new ArrayList<String>();

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (!parameters[0].equals(lavoratore.getEmail())) {
                toWrite.add(l);
            }
        }

        //Salva su file
        try (Writer writer = new BufferedWriter(new FileWriter(dbContattiEmergenza, false))) {
            for (String l : toWrite) {
                writer.write(l + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }

        // cancello da dbDisponibilita
        try {
            lines = Files.readAllLines(Paths.get(dbDisponibilita), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        toWrite.clear();

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (!parameters[0].equals(lavoratore.getEmail())) {
                toWrite.add(l);
            }
        }


        try (Writer writer = new BufferedWriter(new FileWriter(dbDisponibilita, false))) {
            for (String l : toWrite) {
                writer.write(l + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }

        // cancello da dbEsperienze
        try {
            lines = Files.readAllLines(Paths.get(dbEsperienze), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        toWrite.clear();

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (!parameters[0].equals(lavoratore.getEmail())) {
                toWrite.add(l);
            }
        }

        try (Writer writer = new BufferedWriter(new FileWriter(dbEsperienze, false))) {
            for (String l : toWrite) {
                writer.write(l + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }

        // cancello da dbSpecializzazioni
        lines = getSpecializzazioniDB();


        try (Writer writer = new BufferedWriter(new FileWriter(dbSpecializzazioni, false))) {
            for (String l : lines) {
                if (!l.split("_")[0].equals(lavoratore.getEmail()))
                    writer.write(l + "_\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }
    }

    /**
     *
     */
    public void editLavoratore(Lavoratore lavoratore, boolean email, Lavoratore old) {
        if (old != null) {
            try {
                for (Lavoratore l : this.getElencoLavoratori()) {
                    if (l.getEmail().equals(old.getEmail())) {
                        this.getElencoLavoratori().remove(l);
                    }
                }
            } catch (ConcurrentModificationException e) {
            }

            this.getElencoLavoratori().add(lavoratore);
        }


        // se non e' stata modificata l'email del lavoratore
        if (!email) {
            List<String> lines = Collections.emptyList();
            try {
                lines = Files.readAllLines(Paths.get(dbLavoratori), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<String> toWrite = new ArrayList<String>();

            for (String l : lines) {
                String[] parameters = l.split(";");
                if (!parameters[0].equals(lavoratore.getEmail())) {
                    toWrite.add(l);
                }
            }

            //Salva su file
            try (Writer writer = new BufferedWriter(new FileWriter(dbLavoratori, false))) {
                for (String l : toWrite) {
                    writer.write(l + "\n");
                }
                writer.write(lavoratore.toString());
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }

            // sistemo le specializzazioni

            // recupero tutte le specializzazioni
            ArrayList<String> specializzazioni = getSpecializzazioniDB();

            try (Writer writer = new BufferedWriter(new FileWriter(dbSpecializzazioni, false))) {
                for (String l : specializzazioni) {
                    if (!l.split("_")[0].equals(lavoratore.getEmail()))
                        writer.write(l + "_\n");
                }
                writer.write(lavoratore.getEmail() + "_" + lavoratore.getSpecializzazioni() + "_\n");
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }
        } else {
            // sistemo le generalita del lavoratore
            List<String> lines = Collections.emptyList();
            try {
                lines = Files.readAllLines(Paths.get(dbLavoratori), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<String> toWrite = new ArrayList<String>();

            for (String l : lines) {
                String[] parameters = l.split(";");
                if (!parameters[0].equals(old.getEmail())) {
                    toWrite.add(l);
                }
            }

            //Salva su file
            try (Writer writer = new BufferedWriter(new FileWriter(dbLavoratori, false))) {
                for (String l : toWrite) {
                    writer.write(l + "\n");
                }
                writer.write(lavoratore.toString());
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }

            // sistemo i contatti del lavoratore
            lines = Collections.emptyList();
            try {
                lines = Files.readAllLines(Paths.get(dbContattiEmergenza), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            toWrite = new ArrayList<String>();

            for (String l : lines) {
                String[] parameters = l.split(";");
                if (!parameters[0].equals(old.getEmail())) {
                    toWrite.add(l);
                }
            }

            //Salva su file
            try (Writer writer = new BufferedWriter(new FileWriter(dbContattiEmergenza, false))) {
                for (String l : toWrite) {
                    writer.write(l + "\n");
                }
                for (ContattoEmergenza c : lavoratore.getConttattiEmergenza()) {
                    writer.write(lavoratore.getEmail() + ";" + c.toString());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }

            // sistemo le disponibilita
            lines = Collections.emptyList();
            try {
                lines = Files.readAllLines(Paths.get(dbDisponibilita), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            toWrite = new ArrayList<String>();

            for (String l : lines) {
                String[] parameters = l.split(";");
                if (!parameters[0].equals(old.getEmail())) {
                    toWrite.add(l);
                }
            }

            //Salva su file
            try (Writer writer = new BufferedWriter(new FileWriter(dbDisponibilita, false))) {
                for (String l : toWrite) {
                    writer.write(l + "\n");
                }
                for (Disponibilita d : lavoratore.getDisponibilita()) {
                    writer.write(lavoratore.getEmail() + ";" + d.toString());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }

            // sistemo esperienze
            lines = Collections.emptyList();
            try {
                lines = Files.readAllLines(Paths.get(dbEsperienze), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            toWrite = new ArrayList<String>();

            for (String l : lines) {
                String[] parameters = l.split(";");
                if (!parameters[0].equals(old.getEmail())) {
                    toWrite.add(l);
                }
            }

            //Salva su file
            try (Writer writer = new BufferedWriter(new FileWriter(dbEsperienze, false))) {
                for (String l : toWrite) {
                    writer.write(l + "\n");
                }
                for (EsperienzaLavorativa e : lavoratore.getEsperienzeLavorative()) {
                    writer.write(lavoratore.getEmail() + ";" + e.toString());
                }
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }

            // sistemo le specializzazioni
            toWrite.clear();
            ArrayList<String> specializzazioni = getSpecializzazioniDB();
            for (String s : specializzazioni) {
                if (!s.split("_")[0].equals(old.getEmail())) {
                    toWrite.add(s);
                }
            }

            //Salva su file
            try (Writer writer = new BufferedWriter(new FileWriter(dbSpecializzazioni, false))) {
                for (String l : toWrite) {
                    writer.write(l + "_\n");
                }
                writer.write(lavoratore.getEmail() + "_" + lavoratore.getSpecializzazioni() + "_\n");
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }

        }
    }

    public void editEsperienze(Lavoratore lavoratore) {
        try {
            for (Lavoratore l : this.getElencoLavoratori()) {
                if (l.getEmail().equals(lavoratore.getEmail())) {
                    this.getElencoLavoratori().remove(l);
                }
            }
        } catch (ConcurrentModificationException e) {
        }

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbEsperienze), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> toWrite = new ArrayList<String>();

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (!parameters[0].equals(lavoratore.getEmail())) {
                toWrite.add(l);
            }
        }

        //Salva su file
        try (Writer writer = new BufferedWriter(new FileWriter(dbEsperienze, false))) {
            for (String l : toWrite) {
                writer.write(l + "\n");
            }
            for (EsperienzaLavorativa e : lavoratore.getEsperienzeLavorative()) {
                writer.write(lavoratore.getEmail() + ";" + e.toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }
    }

    public void editDisponibilita(Lavoratore lavoratore) {
        try {
            for (Lavoratore l : this.getElencoLavoratori()) {
                if (l.getEmail().equals(lavoratore.getEmail())) {
                    this.getElencoLavoratori().remove(l);
                }
            }
        } catch (ConcurrentModificationException e) {
        }

        this.getElencoLavoratori().add(lavoratore);
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbDisponibilita), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> toWrite = new ArrayList<String>();

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (!parameters[0].equals(lavoratore.getEmail())) {
                toWrite.add(l);
            }
        }

        //Salva su file
        try (Writer writer = new BufferedWriter(new FileWriter(dbDisponibilita, false))) {
            for (String l : toWrite) {
                writer.write(l + "\n");
            }
            for (Disponibilita d : lavoratore.getDisponibilita()) {
                writer.write(lavoratore.getEmail() + ";" + d.toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }
    }

    public void editContattiEmergenza(Lavoratore lavoratore) {
        try {
            for (Lavoratore l : this.getElencoLavoratori()) {
                if (l.getEmail().equals(lavoratore.getEmail())) {
                    this.getElencoLavoratori().remove(l);
                }
            }
        } catch (ConcurrentModificationException e) {
        }

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbContattiEmergenza), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> toWrite = new ArrayList<String>();

        for (String l : lines) {
            String[] parameters = l.split(";");
            if (!parameters[0].equals(lavoratore.getEmail())) {
                toWrite.add(l);
            }
        }

        //Salva su file
        try (Writer writer = new BufferedWriter(new FileWriter(dbContattiEmergenza, false))) {
            for (String l : toWrite) {
                writer.write(l + "\n");
            }
            for (ContattoEmergenza c : lavoratore.getConttattiEmergenza()) {
                writer.write(lavoratore.getEmail() + ";" + c.toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }
    }

    public void addLavoratore(Lavoratore l) {
        // aggiungo un lavoratore all'elenco
        lavoratori.add(l);


        //Salva su file
        try (Writer writer = new BufferedWriter(new FileWriter(dbLavoratori, true))) {
            writer.write(l.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }

        for (EsperienzaLavorativa esp : l.getEsperienzeLavorative()) {
            try (Writer writer = new BufferedWriter(new FileWriter(dbEsperienze, true))) {
                writer.write(l.getEmail() + ";" + esp.toString());
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }
        }

        for (Disponibilita d : l.getDisponibilita()) {
            try (Writer writer = new BufferedWriter(new FileWriter(dbDisponibilita, true))) {
                writer.write(l.getEmail() + ";" + d.toString());
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }
        }

        for (ContattoEmergenza c : l.getConttattiEmergenza()) {
            try (Writer writer = new BufferedWriter(new FileWriter(dbContattiEmergenza, true))) {
                writer.write(l.getEmail() + ";" + c);
            } catch (FileNotFoundException e) {
                System.out.println("Errore interno al sistema");
            } catch (IOException e) {
                System.out.println("Errore interno al sistema");
            }
        }

        try (Writer writer = new BufferedWriter(new FileWriter(dbSpecializzazioni, true))) {
            writer.write(l.getEmail() + "_" + (l.getSpecializzazioni().isEmpty() ? "null_\n" : l.getSpecializzazioni() + "_\n"));
        } catch (FileNotFoundException e) {
            System.out.println("Errore interno al sistema");
        } catch (IOException e) {
            System.out.println("Errore interno al sistema");
        }
    }

    public Dipendente getDipendente() {
        return user;
    }

    public void setDipendente(Dipendente d) {
        user = d;
    }

}
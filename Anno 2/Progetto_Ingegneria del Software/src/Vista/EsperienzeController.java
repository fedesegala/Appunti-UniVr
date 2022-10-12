package Vista;

import Modello.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * controller per la visualizzazione e gestione delle esperienze lavorative
 */
public class EsperienzeController implements Initializable {

    static String dbAree = "src/DataBase/AreeGeografiche.txt";
    private List<EsperienzaLavorativa> esperienze;
    private List<EsperienzaLavorativa> esperienzeOld;
    private List<Mansione> mansioni;
    private LocalDate inizioClicked;
    private LocalDate fineClicked;
    private String aziendaClicked;
    private Mansione mansioneSelezionata;
    private Lavoratore lavoratore;
    private Stage stage;

    @FXML
    private Label aziendaError;
    @FXML
    private ImageView trash;
    @FXML
    private TextField aziendaField;
    @FXML
    private ComboBox<String> comuneBox;
    @FXML
    private Label comuneError;
    @FXML
    private Label fineError;
    @FXML
    private DatePicker finePicker;
    @FXML
    private Label inizioError;
    @FXML
    private Label duplicateError;
    @FXML
    private DatePicker inizioPicker;
    @FXML
    private TextField mansioneField;
    @FXML
    private ComboBox<String> provinciaBox;
    @FXML
    private ComboBox<String> regioneBox;
    @FXML
    private Label retribuzioneGiornalieraError;
    @FXML
    private TextField retribuzioneGiornalieraField;
    @FXML
    private Label retribuzioneLordaError;
    @FXML
    private TextField retribuzioneLordaField;
    @FXML
    private TableColumn<EsperienzaTableView, String> aziendaTable;
    @FXML
    private TableColumn<EsperienzaTableView, String> comuneTable;
    @FXML
    private TableView<EsperienzaTableView> esperienzeTable;
    @FXML
    private TableColumn<EsperienzaTableView, String> fineTable;
    @FXML
    private TableColumn<EsperienzaTableView, String> inizioTable;
    @FXML
    private TableColumn<EsperienzaTableView, String> retNettaTable;
    @FXML
    private TableColumn<EsperienzaTableView, String> retlordaTable;
    @FXML
    private TableColumn<Mansione, String> addMansioniColumn;
    @FXML
    private TableView<Mansione> addMansioniTable;
    @FXML
    private Button cancellaMansioneButton;

    /**
     * Viene chaiamata quando si preme sul bottone aggiungi esperienza
     */
    @FXML
    private void addEsperienza() {
        // Se tutti i campi sono completati correttamente
        if (checkFields()) {

            // Recupero i valori di tutti i campi
            LocalDate inizio = inizioPicker.getValue();
            LocalDate fine = finePicker.getValue();
            String azienda = aziendaField.getText();
            float retribuzioneLorda = Float.parseFloat(retribuzioneLordaField.getText());
            float retribuzioneGiornaliera = Float.parseFloat(retribuzioneGiornalieraField.getText());
            Comune comuneSvolgimento = new Comune(comuneBox.getValue(), provinciaBox.getValue(), regioneBox.getValue());

            // Verifico che non ci siano esperienze duplicate nel caso in cui non ci sia un'esperienza selezioanta
            if (checkDuplicates(inizio, fine, azienda)) {
                duplicateError.setText("");
                // Se ho selezionato un'esperienza
                if (inizioClicked != null && fineClicked != null && aziendaClicked != null) {
                    boolean flag = true;
                    for (EsperienzaLavorativa esp : esperienze) {
                        // C'erco un'esperienza della tabella corrispondente all'esperienza vera
                        if (flag) {
                            if (inizioClicked.equals(esp.getPeriodoSvolgimento().getInizio()) && fineClicked.equals(esp.getPeriodoSvolgimento().getFine()) && aziendaClicked.equals(esp.getAzienda())) {

                                // Modifico i valori dell'esperienza gia esistente
                                esp.setPeriodoSvolgimento(new Periodo(inizio, fine));
                                esp.setAzienda(azienda);
                                esp.setRetribuzioneLorda(retribuzioneLorda);
                                esp.setRetribuzioneGiornaliera(retribuzioneGiornaliera);
                                esp.setLuogoSvolgimento(comuneSvolgimento);
                                esp.setMansioni(mansioni);

                                flag = false;
                                inizioClicked = null;
                                fineClicked = null;
                                aziendaClicked = null;
                            }
                        }
                    }
                }
                // Se non ho selezionato un'esperienza ne aggiungo una nuova
                else {
                    esperienze.add(new EsperienzaLavorativa(
                            new Periodo(inizio, fine),
                            azienda,
                            mansioni,
                            comuneSvolgimento,
                            retribuzioneLorda,
                            retribuzioneGiornaliera
                    ));
                }

                // popolazione della tabella
                displayEsperienze();
                clear();
            }
        }
    }

    /**
     * @param event
     * @throws IOException
     * Metodo che manda le esperienze lavorative ad Inserisci
     */
    @FXML
    private void submit(ActionEvent event) throws IOException {
        lavoratore.setEsperienzeLavorative(esperienze);
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * funzione che pulisce tutti i campi
     */
    @FXML
    private void clear() {
        aziendaField.setText("");
        inizioPicker.setValue(null);
        finePicker.setValue(null);
        mansioneField.setText("");
        regioneBox.valueProperty().set(null);
        retribuzioneGiornalieraField.setText("");
        retribuzioneLordaField.setText("");
        mansioni = new ArrayList<Mansione>();
        aggiornaMansioni();
    }

    /**
     * funzione che viene chiamata quando si preme su aggiungi mansione
     */
    @FXML
    private void addMansione() {
        if (mansioneField.getText() != null && !mansioneField.getText().contains(",")) {
            if (!(mansioneField.getText().isBlank() || mansioneField.getText().isEmpty())) {
                mansioni.add(new Mansione(mansioneField.getText()));
                mansioneField.setText(null);
                aggiornaMansioni();
            }
        } else {
            System.err.println("Il campo mansioni non puo contenere il carattere ','");
        }
    }

    /**
     * Viene chiamata quando si preme una riga nella tabella delle esperienze
     */
    @FXML
    void rowClicked() {
        // Recupero l'oggetto selezionato nella tabella
        EsperienzaTableView esperienzaSelezionata = esperienzeTable.getSelectionModel().getSelectedItem();
        if (esperienzaSelezionata != null) {
            // Cancello i messaggi di errore
            cancellaMansioneButton.setVisible(false);
            trash.setVisible(false);
            duplicateError.setText("");
            inizioError.setText("");
            fineError.setText("");
            aziendaError.setText("");
            retribuzioneLordaError.setText("");
            retribuzioneGiornalieraField.setText("");
            comuneError.setText("");
            mansioneField.setText("");

            // Salvo i valori selezionati per azienda, inizio e fine. Serviranno per controllare che l'esperienza nella tabella corrisponda
            // Riempio i campi con i valori dell'esperienza selezionata
            aziendaClicked = esperienzaSelezionata.getID().getAzienda();
            aziendaField.setText(aziendaClicked);

            inizioClicked = esperienzaSelezionata.getID().getPeriodoSvolgimento().getInizio();
            inizioPicker.setValue(inizioClicked);

            fineClicked = esperienzaSelezionata.getID().getPeriodoSvolgimento().getFine();
            finePicker.setValue(fineClicked);

            regioneBox.valueProperty().set(esperienzaSelezionata.getID().getLuogoSvolgimento().getRegione());
            provinciaBox.valueProperty().set(esperienzaSelezionata.getID().getLuogoSvolgimento().getProvincia());
            comuneBox.valueProperty().set(esperienzaSelezionata.getID().getLuogoSvolgimento().getNome());

            retribuzioneGiornalieraField.setText(String.valueOf(esperienzaSelezionata.getID().getRetribuzioneNetta()));
            retribuzioneLordaField.setText(String.valueOf(esperienzaSelezionata.getID().getRetribuzioneLorda()));

            mansioni = new ArrayList<Mansione>();
            mansioni = esperienzaSelezionata.getID().getMansioni();

            aggiornaMansioni();
        }
    }

    /**
     * Viene chiamata quando si preme una riga nella tabella delle mansioni
     */
    @FXML
    void rowEsperienzeClicked() {
        mansioneSelezionata = addMansioniTable.getSelectionModel().getSelectedItem();
        cancellaMansioneButton.setVisible(true);
        trash.setVisible(true);
    }

    /**
     * permette di cancellare una mansione
     */
    @FXML
    void clearMansione() {
        if (mansioneSelezionata != null) {
            try {
                for (Mansione m : mansioni) {
                    if (mansioneSelezionata.equals(m)) {
                        mansioni.remove(m);
                        aggiornaMansioni();
                        cancellaMansioneButton.setVisible(false);
                        trash.setVisible(false);
                    }
                }
            } catch (ConcurrentModificationException e) {
            }

        } else duplicateError.setText("Seleziona una mansione per eliminarla");
    }

    /**
     * Funzioni che nascondono i messaggi di errore
     */
    @FXML
    private void hideInizioError() {
        if (inizioPicker.getValue() != null) {
            inizioError.setText("");
        }
    }

    @FXML
    private void hideFineError() {
        if (finePicker.getValue() != null) {
            fineError.setText("");
        }
    }

    @FXML
    private void hideAziendaError() {
        aziendaError.setText("");
    }

    @FXML
    private void hideRetribuzioneLordaError() {
        retribuzioneLordaError.setText("");
        if (!(retribuzioneGiornalieraField.getText().isBlank() || retribuzioneGiornalieraField.getText().isEmpty()) && checkRetribuzioneNetta()) {
            retribuzioneGiornalieraError.setText("");
        }
    }

    @FXML
    private void hideRetribuzioneGiornalieraError() {
        retribuzioneGiornalieraError.setText("");
    }

    @FXML
    private void hideComuneError() {
        if (regioneBox.getValue() != null && provinciaBox.getValue() != null
                && comuneBox.getValue() != null) {
            comuneError.setText("");
        }
    }

    /**
     * Eliminazione dell'esperienza selezionata
     */
    @FXML
    private void clearEsperienza() {
        // Se ho selezionato un'esperienza
        if (inizioClicked != null && fineClicked != null && aziendaClicked != null) {
            try {
                for (EsperienzaLavorativa esp : esperienze) {
                    if (inizioClicked.equals(esp.getPeriodoSvolgimento().getInizio()) && fineClicked.equals(esp.getPeriodoSvolgimento().getFine()) && aziendaClicked.equals(esp.getAzienda())) {
                        esperienze.remove(esp);
                        clear();
                        displayEsperienze();
                    }
                }
            } catch (ConcurrentModificationException e) {
            }

        } else duplicateError.setText("Seleziona un'esperienza per eliminarla");
    }

    /**
     * @param l
     * permette il passaggio del lavoratore dalla vista precedente al controller delle esperienze
     */
    protected void salvaLavoratore(Lavoratore l) {
        lavoratore = l;
        esperienzeOld = l.getEsperienzeLavorative();

        esperienze = new ArrayList<EsperienzaLavorativa>();
        for (EsperienzaLavorativa esp : esperienzeOld) {
            esperienze.add(new EsperienzaLavorativa(esp));
        }
    }

    /**
     * Riempimento della tabella delle esperienze
     */
    protected void displayEsperienze() {
        ObservableList<EsperienzaTableView> esperienzeObservableList = FXCollections.observableArrayList();
        for (EsperienzaLavorativa e : esperienze) {

            String inizio = e.getPeriodoSvolgimento().getInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String fine = e.getPeriodoSvolgimento().getFine().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String azienda = e.getAzienda();
            String mansioni = "bottone";
            String retribuzioneLorda = e.getRetribuzioneLorda() + "€";
            String retribuzioneGiornaliera = e.getRetribuzioneNetta() + "€";
            String comunesvolgimento = e.getLuogoSvolgimento().toString();

            esperienzeObservableList.add(new EsperienzaTableView(inizio, fine, azienda, mansioni, retribuzioneLorda, retribuzioneGiornaliera, comunesvolgimento, e));
        }

        inizioTable.setCellValueFactory(new PropertyValueFactory<>("inizio"));
        fineTable.setCellValueFactory(new PropertyValueFactory<>("fine"));
        aziendaTable.setCellValueFactory(new PropertyValueFactory<>("Azienda"));
        comuneTable.setCellValueFactory(new PropertyValueFactory<>("comunesvolgimento"));
        retlordaTable.setCellValueFactory(new PropertyValueFactory<>("retribuzioneLorda"));
        retNettaTable.setCellValueFactory(new PropertyValueFactory<>("retribuzioneGiornaliera"));
        esperienzeTable.setItems(esperienzeObservableList);

    }

    /**
     * Riempimento della tabella delle mansioni
     */
    private void aggiornaMansioni() {
        ObservableList<Mansione> mansioniObservableList = FXCollections.observableArrayList();
        for (Mansione m : mansioni) {
            mansioniObservableList.add(m);
        }
        addMansioniColumn.setCellValueFactory(new PropertyValueFactory<>("mansione"));
        addMansioniTable.setItems(mansioniObservableList);
    }

    /**
     * @return
     * Verifica che i campi siano corretti
     */
    private boolean checkFields() {
        boolean flag = true;

        if (inizioPicker.getValue() == null) {
            inizioError.setText("Inserire una data di inizio");
            flag = false;
        }

        if (finePicker.getValue() == null) {
            fineError.setText("Inserire una data di fine");
            flag = false;
        }

        if (aziendaField.getText().isBlank() || aziendaField.getText().isEmpty()) {
            aziendaError.setText("Inserire un'Azienda");
            flag = false;
        }

        if (retribuzioneLordaField.getText().isBlank() || retribuzioneLordaField.getText().isEmpty()) {
            retribuzioneLordaError.setText("Inserire una retribuzione lorda");
            flag = false;
        } else if (!checkRetribuzioneLorda()) {
            flag = false;
        }

        if (retribuzioneGiornalieraField.getText().isBlank() || retribuzioneGiornalieraField.getText().isEmpty()) {
            retribuzioneGiornalieraError.setText("Inserire una retribuzione netta");
            flag = false;
        } else if (!checkRetribuzioneNetta()) {
            flag = false;
        }

        //Ho inserito correttamente entrambe le retribuzioni
        if (!(retribuzioneLordaField.getText().isBlank() || retribuzioneLordaField.getText().isEmpty()) &&
                !(retribuzioneGiornalieraField.getText().isBlank() || retribuzioneGiornalieraField.getText().isEmpty()) &&
                checkRetribuzioneLorda() && checkRetribuzioneNetta()) {
            if (Float.parseFloat(retribuzioneLordaField.getText()) < Float.parseFloat(retribuzioneGiornalieraField.getText())) {
                retribuzioneGiornalieraError.setText("retribuzione netta più elevata di quella lorda");
                flag = false;
            }
        }

        if (regioneBox.getValue() == null || provinciaBox.getValue() == null || comuneBox.getValue() == null) {
            comuneError.setText("Inserire un comune");
            flag = false;
        }

        if (inizioPicker.getValue() != null && finePicker.getValue() != null) {
            if (inizioPicker.getValue().compareTo(finePicker.getValue()) > 0) {
                inizioError.setText("inizio viene dopo di fine");
                fineError.setText("fine viene prima di inizio");

                flag = false;
            }
        }

        if (finePicker.getValue() != null) {
            LocalDate oggi = LocalDate.now();
            Period beetween = Period.between(finePicker.getValue(), oggi);

            if (beetween.getYears() >= 5) {
                fineError.setText("Piu vecchia di 5 anni");
                System.err.println("Posso salvare solo esperienze meno vecchie di 5 anni");
                flag = false;
            }
        }

        return flag;
    }

    /**
     * @param inizio
     * @param fine
     * @param azienda
     * @return
     * Verifica che non ci siano esperienze duplicate
     */
    private boolean checkDuplicates(LocalDate inizio, LocalDate fine, String azienda) {
        if (inizioClicked == null && fineClicked == null && aziendaClicked == null) {
            for (EsperienzaLavorativa esp : esperienze) {
                if (inizio.equals(esp.getPeriodoSvolgimento().getInizio()) && fine.equals(esp.getPeriodoSvolgimento().getFine()) && azienda.equals(esp.getAzienda())) {
                    duplicateError.setText("Esperienza gia esistente");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return
     * Verifica che la retribuzione lorda sia corretta
     */
    private boolean checkRetribuzioneLorda() {
        if (!retribuzioneLordaField.getText().matches("[0-9.]+")) {
            retribuzioneLordaError.setText("Inserire un valore valido");
            return false;
        }
        return true;
    }

    /**
     * @return
     * Verifica che la retribuzione netta sia corretta
     */
    private boolean checkRetribuzioneNetta() {
        if (!retribuzioneGiornalieraField.getText().matches("[0-9.]+")) {
            retribuzioneGiornalieraError.setText("Inserire un valore valido");
            return false;
        }
        return true;
    }

    /**
     * @param url
     * @param resourceBundle
     * Inizializzo le box di selezione del luogo
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancellaMansioneButton.setVisible(false);
        trash.setVisible(false);
        mansioni = new ArrayList<Mansione>();
        // inizializzo elenco delle regioni
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbAree), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> regioni = new ArrayList<>();
        for (String r : lines) {
            String regione = r.split(",")[0];
            if (!regioni.contains(regione)) {
                regioni.add(regione);
            }
        }

        ObservableList regioniObList = FXCollections.observableList(regioni);
        regioneBox.getItems().clear();

        regioneBox.setItems(regioniObList);

        regioneBox.setOnAction(this::setProvince);
        provinciaBox.setOnAction(this::setComune);

    }

    private void setProvince(ActionEvent event) {
        List<String> lines = Collections.emptyList();
        String regioneSelezionata = regioneBox.getValue();

        try {
            lines = Files.readAllLines(Paths.get(dbAree), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> province = new ArrayList<>();
        for (String p : lines) {
            String provincia = p.split(",")[1];
            if (!province.contains(provincia) && p.split(",")[0].equals(regioneSelezionata)) {
                province.add(provincia);
            }
        }

        ObservableList obList = FXCollections.observableList(province);

        provinciaBox.getItems().clear();
        provinciaBox.setItems(obList);

    }

    private void setComune(ActionEvent event) {
        List<String> lines = Collections.emptyList();
        String provinciaSelezionata = provinciaBox.getValue();

        try {
            lines = Files.readAllLines(Paths.get(dbAree), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> province = new ArrayList<>();
        for (String c : lines) {
            String comune = c.split(",")[2];
            if (!province.contains(comune) && c.split(",")[1].equals(provinciaSelezionata)) {
                province.add(comune);
            }
        }

        ObservableList obList = FXCollections.observableList(province);

        comuneBox.getItems().clear();
        comuneBox.setItems(obList);

    }
}


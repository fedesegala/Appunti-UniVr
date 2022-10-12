package Vista;

import Modello.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * classe che gestisce il filtraggio della ricerca
 */
public class FiltersController implements Initializable {

    private static final String dbPatenti = "src/DataBase/Patente.txt";
    private static final String dbLingue = "src/DataBase/Lingua.txt";
    private static String nome = "";
    private static String cognome = "";
    private static ObservableList<Lingua> lingue = FXCollections.observableList(new ArrayList<Lingua>());
    private static ObservableList<Patente> patenti = FXCollections.observableArrayList(new ArrayList<Patente>());
    private static LocalDate inizio = null;
    private static LocalDate fine = null;
    private static String mansione = "";
    private static String luogo = "";
    private static String automunito = "";
    private static boolean ricercaAnd = false;
    private static boolean ricercaAutomunito = false;
    private Stage stage;

    @FXML
    private ComboBox<String> automunitoCombobox;
    @FXML
    private TextField cognomeField;
    @FXML
    private DatePicker finePicker;
    @FXML
    private DatePicker inizioPicker;
    @FXML
    private CheckComboBox<Lingua> lingueCombo;
    @FXML
    private TextField luogoField;
    @FXML
    private TextField mansionefield;
    @FXML
    private TextField nomeField;
    @FXML
    private CheckComboBox<Patente> patenteCombo;
    @FXML
    private CheckBox tipoRicercaCheckBox;
    @FXML
    private CheckBox automunitoCheckBox;


    /**
     * funzione che svuota tutti i campi della ricerca
     */
    protected static void emptyFields() {
        nome = "";
        cognome = "";
        lingue = FXCollections.observableList(new ArrayList<Lingua>());
        inizio = null;
        fine = null;
        mansione = "";
        luogo = "";
        patenti = FXCollections.observableList(new ArrayList<Patente>());
        automunito = "";
        ricercaAnd = false;
    }

    /**
     * @param event
     * applica i filtri di ricerca
     */
    @FXML
    void submit(ActionEvent event) {
        nome = nomeField.getText().toLowerCase();
        cognome = cognomeField.getText().toLowerCase();
        lingue = lingueCombo.getCheckModel().getCheckedItems();
        inizio = inizioPicker.getValue();
        fine = finePicker.getValue();
        luogo = luogoField.getText().toLowerCase();
        mansione = mansionefield.getText().toLowerCase();
        patenti = patenteCombo.getCheckModel().getCheckedItems();
        automunito = automunitoCombobox.getValue();
        ricercaAutomunito = automunitoCheckBox.isSelected();
        ricercaAnd = tipoRicercaCheckBox.isSelected();

        boolean automunitoBool = automunito != null && automunito.equals("Si");

        VisualizzaController.ricerca = new Ricerca(
                nome, cognome, lingue, inizio, fine, luogo, mansione, patenti, automunitoBool, ricercaAutomunito, ricercaAnd);

        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * @param event
     * listener per il bottone che cancella i filtri della ricerca
     */
    @FXML
    void clear(ActionEvent event) {
        emptyFields();
        nomeField.setText(nome);
        cognomeField.setText(cognome);

        lingueCombo.getCheckModel().clearChecks();
        patenteCombo.getCheckModel().clearChecks();

        inizioPicker.setValue(inizio);
        finePicker.setValue(fine);
        mansionefield.setText(mansione);
        luogoField.setText(luogo);
        automunitoCombobox.setValue(null);
        automunitoCheckBox.setSelected(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        automunitoCombobox.setVisible(false);
        nomeField.setText(nome);
        cognomeField.setText(cognome);
        inizioPicker.setValue(inizio);
        finePicker.setValue(fine);
        mansionefield.setText(mansione);
        luogoField.setText(luogo);
        tipoRicercaCheckBox.setSelected(ricercaAnd);

        if (ricercaAutomunito) {
            automunitoCheckBox.setSelected(true);
            automunitoCombobox.setVisible(true);
            automunitoCombobox.setValue(automunito);
        }

        List<Patente> elencoPatenti = fillPatenti();
        ObservableList patenteObList = FXCollections.observableList(elencoPatenti);
        patenteCombo.getItems().clear();
        patenteCombo.getItems().addAll(patenteObList);

        for (Patente p : patenti) {
            for (Patente p1 : elencoPatenti) {
                if (p.toString().equals(p1.toString())) {
                    patenteCombo.getCheckModel().check(elencoPatenti.indexOf(p1));
                }
            }
        }

        List<Lingua> elencoLingue = fillLingue();
        ObservableList linguaObList = FXCollections.observableList(elencoLingue);
        lingueCombo.getItems().clear();
        lingueCombo.getItems().addAll(linguaObList);

        // inizializzo le lingue gia selezionate
        for (Lingua l : lingue) {
            for (Lingua l1 : elencoLingue) {
                if (l.toString().equals(l1.toString()))
                    lingueCombo.getCheckModel().check(elencoLingue.indexOf(l1));

            }
        }

        List<String> automunitoList = new ArrayList<>();
        automunitoList.add("Si");
        automunitoList.add("No");
        ObservableList automunitoOBlist = FXCollections.observableList(automunitoList);
        automunitoCombobox.getItems().clear();
        automunitoCombobox.setItems(automunitoOBlist);

    }

    /**
     * @param event
     * listener per far visualizzare automunito
     */
    @FXML
    void updateAutomunito(ActionEvent event) {
        if (automunitoCheckBox.isSelected()) {
            automunitoCombobox.setVisible(true);
            ricercaAutomunito = true;
        } else {
            automunitoCombobox.setVisible(false);
            ricercaAutomunito = false;
        }
    }

    private ArrayList<Patente> fillPatenti() {
        ArrayList<Patente> result = new ArrayList<>();

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbPatenti), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String l : lines)
            result.add(new Patente(l));

        return result;
    }

    private ArrayList<Lingua> fillLingue() {
        ArrayList<Lingua> result = new ArrayList<>();

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbLingue), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String l : lines)
            result.add(new Lingua(l));

        return result;
    }
}

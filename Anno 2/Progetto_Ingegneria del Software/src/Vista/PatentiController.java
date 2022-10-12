package Vista;

import Modello.Lavoratore;
import Modello.Patente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PatentiController implements Initializable {

    static String dbPatenti = "src/DataBase/Patente.txt";
    private Lavoratore lavoratore;
    private List<Patente> patenti;
    private List<Patente> patentiOld;
    private Patente patenteSelected = null;
    private Stage stage;

    @FXML
    private ComboBox<Patente> patenteBox;
    @FXML
    private Label patenteError;
    @FXML
    private TableColumn<Patente, String> patentiColumn;
    @FXML
    private TableView<Patente> patentiTable;

    /**
     * @param event
     * aggiunge una patente alla lista
     */
    @FXML
    void addPatente(ActionEvent event) {
        if (patenteBox.getValue() != null) {
            if (checkDuplicate()) {
                patenti.add(new Patente(String.valueOf(patenteBox.getValue())));
                patenteBox.valueProperty().set(null);
                patenteSelected = null;
            }
        } else {
            patenteError.setText("inserire una lingua");
        }
        displayPatenti();
    }

    /**
     * @param event
     * nasconde l'errore quando si interagisce con la combobox
     */
    @FXML
    void hidePatenteError(ActionEvent event) {
        patenteError.setText("");
    }

    /**
     * @param event
     * @throws IOException
     * aggiunge le patenti al lavoratore e torna alla schermata del chiamante
     *
     */
    @FXML
    private void submit(ActionEvent event) throws IOException {
        lavoratore.setPatenti(patenti);
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * @param url
     * @param resourceBundle
     * inizializza la tabella e la combobox
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> patente = Collections.emptyList();
        try {
            patente = Files.readAllLines(Paths.get(dbPatenti), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList patenteObList = FXCollections.observableList(patente);
        patenteBox.getItems().clear();
        patenteBox.setItems(patenteObList);
    }

    /**
     * seleziona la patente dalla tabella
     */
    @FXML
    void rowClicked() {
        Patente patenteSelezionata = patentiTable.getSelectionModel().getSelectedItem();
        if (patenteSelezionata != null) {
            patenteError.setText("");
            patenteSelected = patenteSelezionata;
        }
    }

    /**
     * rimuove la patente dalla lista e aggiorna la tabella
     */
    @FXML
    private void clearPatente() {
        if (patenteSelected != null) {
            try {
                for (Patente p : patenti) {
                    if (patenteSelected.toString().equals(p.toString())) {
                        patenti.remove(p);
                        displayPatenti();
                    }
                }
            } catch (ConcurrentModificationException e) {
            }

        } else patenteError.setText("devi selezionare una patente");
    }

    /**
     * @param l
     * salva il lavoratore preso come parametro dal chiamante
     */
    protected void salvaLavoratore(Lavoratore l) {
        lavoratore = l;
        patenti = new ArrayList<>();
        patentiOld = lavoratore.getPatenti();

        for (Patente p : patentiOld)
            patenti.add(p);
    }

    /**
     * mostra le patenti
     */
    protected void displayPatenti() {
        ObservableList<Patente> patentiObservableList = FXCollections.observableArrayList();
        for (Patente p : patenti) {
            patentiObservableList.add(p);
        }
        patentiColumn.setCellValueFactory(new PropertyValueFactory<>("patente"));
        patentiTable.setItems(patentiObservableList);
    }

    /**
     * @return
     * verifica che non ci siano patenti duplicate, lanciando l'opportuno errore
     */
    private boolean checkDuplicate() {
        for (Patente p : patenti) {
            if (String.valueOf(patenteBox.getValue()).equals(p.toString())) {
                patenteError.setText("patente già inserita");
                return false;
            }
        }
        return true;
    }
}

package Vista;

import Modello.Lavoratore;
import Modello.Lingua;
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

public class LingueParlateController implements Initializable {

    static String dbLinugue = "src/DataBase/Lingua.txt";
    private Lavoratore lavoratore;
    private List<Lingua> lingue;
    private List<Lingua> lingueOld;
    private Stage stage;
    private Lingua linguaSelected;

    @FXML
    private ComboBox<Lingua> linguaBox;

    @FXML
    private TableColumn<Lingua, String> linguaColumn;

    @FXML
    private Label linguaError;

    @FXML
    private TableView<Lingua> linguaTable;

    /**
     * @param event
     * listener per l'aggiunta di una lingua: aggiunge la lingua alla lista e aggiorna la tabella
     */
    @FXML
    void addLingua(ActionEvent event) {
        if (linguaBox.getValue() != null) {
            if (checkDuplicate()) {
                lingue.add(new Lingua(String.valueOf(linguaBox.getValue())));
                linguaBox.valueProperty().set(null);
                linguaSelected = null;
            }
        } else {
            linguaError.setText("inserire una lingua");
        }
        displayLingue();
    }

    /**
     * @param event
     * nasconde l'errore quando si interagisce con la combobox
     */
    @FXML
    void hideLinguaError(ActionEvent event) {
        linguaError.setText("");
    }

    /**
     * @param event
     * @throws IOException
     * salva la lista di lingue
     */
    @FXML
    private void submit(ActionEvent event) throws IOException {
        if (lingue.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore");
            alert.setContentText("Non e' possibile lasciare un lavoratore senza alcuna lingua parlata");

            alert.showAndWait();
        } else {
            lavoratore.setLingueParlate(lingue);
            Node source = (Node) event.getSource();
            stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * listener per la lingua della tabella selezionata
     */
    @FXML
    void rowClicked() {
        Lingua linguaSelezionata = linguaTable.getSelectionModel().getSelectedItem();
        if (linguaSelezionata != null) {
            linguaError.setText("");
            linguaSelected = linguaSelezionata;
        }
    }

    /**
     * cancella la lingua
     */
    @FXML
    private void clearLingua() {
        if (linguaSelected != null) {
            try {
                for (Lingua l : lingue) {
                    if (linguaSelected.toString().equals(l.toString())) {
                        lingue.remove(l);
                        displayLingue();
                    }
                }
            } catch (ConcurrentModificationException e) {
            }

        } else linguaError.setText("devi selezionare una lingua");
    }

    /**
     * @param l
     * salva il lavoratore passato come parametro dal chiamante
     */
    protected void salvaLavoratore(Lavoratore l) {
        lavoratore = l;
        lingueOld = l.getLingueParlate();

        lingue = new ArrayList<>();

        for (Lingua lingua : lingueOld) {
            lingue.add(lingua);
        }
    }

    /**
     * riempie la tabella
     */
    protected void displayLingue() {
        ObservableList<Lingua> lingueObservableList = FXCollections.observableArrayList();
        for (Lingua l : lingue) {
            lingueObservableList.add(l);
        }
        linguaColumn.setCellValueFactory(new PropertyValueFactory<>("lingua"));
        linguaTable.setItems(lingueObservableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> lingua = Collections.emptyList();
        try {
            lingua = Files.readAllLines(Paths.get(dbLinugue), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList lingueObList = FXCollections.observableList(lingua);
        linguaBox.getItems().clear();
        linguaBox.setItems(lingueObList);
    }

    /**
     * @return
     * verifica che non ci siano lingue già inserite
     */
    private boolean checkDuplicate() {
        for (Lingua l : lingue) {
            if (String.valueOf(linguaBox.getValue()).equals(l.toString())) {
                linguaError.setText("lingua già esistente");
                return false;
            }
        }
        return true;
    }

}


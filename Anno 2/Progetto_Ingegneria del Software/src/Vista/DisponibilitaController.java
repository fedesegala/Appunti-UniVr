package Vista;

import Modello.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
 * controller per la gestione della schermata delle disponibilità
 */
public class DisponibilitaController implements Initializable {

    static String dbAree = "src/DataBase/AreeGeografiche.txt";
    private static LocalDate inizioClicked;
    private static LocalDate fineClicked;
    private static Comune comuneClicked;
    private Lavoratore lavoratore;
    private List<Disponibilita> disponibilita;
    private List<Disponibilita> disponibilitaOld;
    private Stage stage;

    @FXML
    private ComboBox<String> comuneBox;
    @FXML
    private Label comuneError;
    @FXML
    private TableColumn<DisponibilitaTableView, String> comuneTable;
    @FXML
    private TableView<DisponibilitaTableView> disponibilitaTable;
    @FXML
    private Label fineError;
    @FXML
    private DatePicker finePicker;
    @FXML
    private TableColumn<DisponibilitaTableView, String> fineTable;
    @FXML
    private Label inizioError;
    @FXML
    private DatePicker inizioPicker;
    @FXML
    private TableColumn<DisponibilitaTableView, String> inizioTable;
    @FXML
    private ComboBox<String> provinciaBox;
    @FXML
    private ComboBox<String> regioneBox;

    /**
     * @param event
     * permette di aggiungere una nuova disponibilita
     */
    @FXML
    void addDisponibilita(ActionEvent event) {
        if (checkFields()) {
            LocalDate inizio = inizioPicker.getValue();
            LocalDate fine = finePicker.getValue();

            Comune comuneSvolgimento = new Comune(comuneBox.getValue(), provinciaBox.getValue(), regioneBox.getValue());

            if (checkDuplicates(inizio, fine, comuneSvolgimento)) {
                comuneError.setText("");
                boolean trovato = false;
                if (inizioClicked != null && fineClicked != null && comuneClicked != null) {
                    for (Disponibilita d : disponibilita) {
                        if (inizioClicked.equals(d.getPeriodoDisponibilita().getInizio()) &&
                                fineClicked.equals(d.getPeriodoDisponibilita().getFine()) &&
                                comuneClicked.getNome().equals(d.getLuogoDisponibilita().getNome())) {
                            trovato = true;
                            d.setPeriodoDisponibilita(new Periodo(inizio, fine));
                            d.setLuogoDisponibilita(comuneSvolgimento);
                            inizioClicked = null;
                            fineClicked = null;
                            comuneClicked = null;
                            break;
                        }
                    }
                }
                if (!trovato) {
                    disponibilita.add(new Disponibilita(new Periodo(inizio, fine), comuneSvolgimento));
                }
                // popolazione della tabella
                displayDisponibilita();
                clear();
            }

        }

    }

    /**
     * permette di pulire tutti i campi
     */
    @FXML
    private void clear() {
        inizioPicker.setValue(null);
        finePicker.setValue(null);
        regioneBox.valueProperty().set(null);
    }

    @FXML
    private void hideComuneError() {
        if (regioneBox.getValue() != null && provinciaBox.getValue() != null
                && comuneBox.getValue() != null) {
            comuneError.setText("");
        }
    }

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

    /**
     * @param event
     * @throws IOException
     * premette il salvataggio delle disponibilità ed il ritorno alla schermata iniziale
     */
    @FXML
    private void submit(ActionEvent event) throws IOException {
        lavoratore.setDisponibilita(disponibilita);
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * permette il riempimento dei campi con il valore selezionato dalla tabella
     */
    @FXML
    void rowClicked() {
        DisponibilitaTableView disponiblilitaSelezionata = disponibilitaTable.getSelectionModel().getSelectedItem();
        if (disponiblilitaSelezionata != null) {
            inizioError.setText("");
            fineError.setText("");
            comuneError.setText("");
            inizioClicked = disponiblilitaSelezionata.getID().getPeriodoDisponibilita().getInizio();
            inizioPicker.setValue(inizioClicked);
            fineClicked = disponiblilitaSelezionata.getID().getPeriodoDisponibilita().getFine();
            finePicker.setValue(fineClicked);
            regioneBox.valueProperty().set(disponiblilitaSelezionata.getID().getLuogoDisponibilita().getRegione());
            provinciaBox.valueProperty().set(disponiblilitaSelezionata.getID().getLuogoDisponibilita().getProvincia());
            comuneBox.valueProperty().set(disponiblilitaSelezionata.getID().getLuogoDisponibilita().getNome());
            comuneClicked = disponiblilitaSelezionata.getID().getLuogoDisponibilita();
        }
    }

    /**
     * permette la cancellazione del periodo di disponibilità selezionato
     */
    @FXML
    private void clearDisponibilita() {
        DisponibilitaTableView disponibilitaSelezionata = disponibilitaTable.getSelectionModel().getSelectedItem();
        if (inizioClicked != null && fineClicked != null && comuneClicked != null) {
            try {
                for (Disponibilita d : disponibilita) {
                    if (inizioClicked.equals(d.getPeriodoDisponibilita().getInizio()) &&
                            fineClicked.equals(d.getPeriodoDisponibilita().getFine()) &&
                            comuneClicked.getNome().equals(d.getLuogoDisponibilita().getNome())) {
                        disponibilita.remove(d);
                        clear();
                        displayDisponibilita();
                    }
                }
            } catch (ConcurrentModificationException e) {
            }

        } else comuneError.setText("Seleziona una disponibilita per eliminarla");
    }

    /**
     * @return
     * verifica che tutti i campi non siano vuoti
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
                System.err.println("Posso salvare solo disponibilità meno vecchie di 5 anni");
                flag = false;
            }
        }


        return flag;
    }

    /**
     * riempie la tabella delle disponibilità
     */
    protected void displayDisponibilita() {
        ObservableList<DisponibilitaTableView> disponibilitaObservableList = FXCollections.observableArrayList();

        for (Disponibilita d : disponibilita) {

            String inizio = d.getPeriodoDisponibilita().getInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String fine = d.getPeriodoDisponibilita().getFine().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String comunesvolgimento = d.getLuogoDisponibilita().toString();

            disponibilitaObservableList.add(new DisponibilitaTableView(inizio, fine, comunesvolgimento, d));
        }

        inizioTable.setCellValueFactory(new PropertyValueFactory<>("inizio"));
        fineTable.setCellValueFactory(new PropertyValueFactory<>("fine"));
        comuneTable.setCellValueFactory(new PropertyValueFactory<>("comune"));
        disponibilitaTable.setItems(disponibilitaObservableList);
    }


    /**
     * @param inizio
     * @param fine
     * @param comune
     * @return
     * verifica che non ci siano periodi di disponibilità duplicati
     */
    private boolean checkDuplicates(LocalDate inizio, LocalDate fine, Comune comune) {
        if (inizioClicked == null && fineClicked == null && comuneClicked == null) {
            for (Disponibilita d : disponibilita) {
                if (((inizio.compareTo(d.getPeriodoDisponibilita().getInizio()) >= 0 && (inizio.compareTo(d.getPeriodoDisponibilita().getFine()) <= 0)) && comune.getNome().equals(d.getLuogoDisponibilita().getNome())) ||
                        ((fine.compareTo(d.getPeriodoDisponibilita().getInizio()) >= 0) && (fine.compareTo(d.getPeriodoDisponibilita().getFine()) <= 0) && comune.getNome().equals(d.getLuogoDisponibilita().getNome())) ||
                        ((d.getPeriodoDisponibilita().getInizio().compareTo(inizio) >= 0) && (d.getPeriodoDisponibilita().getInizio().compareTo(fine) <= 0) && comune.getNome().equals(d.getLuogoDisponibilita().getNome())) ||
                        ((d.getPeriodoDisponibilita().getFine().compareTo(inizio) >= 0) && (d.getPeriodoDisponibilita().getFine().compareTo(fine) <= 0) && comune.getNome().equals(d.getLuogoDisponibilita().getNome()))){
                    comuneError.setText("Disponibilità per lo stesso comune sovrapposte");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    protected void salvaLavoratore(Lavoratore l) {
        lavoratore = l;
        disponibilitaOld = l.getDisponibilita();

        disponibilita = new ArrayList<Disponibilita>();
        for (Disponibilita d : disponibilitaOld) {
            disponibilita.add(new Disponibilita(d));
        }
    }


}

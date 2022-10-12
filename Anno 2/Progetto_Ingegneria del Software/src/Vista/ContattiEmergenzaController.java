package Vista;

import Modello.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller per l'inserimento dei contatti di emergenza
 */
public class ContattiEmergenzaController{

    private Lavoratore lavoratore;
    private List<ContattoEmergenza> contatti;
    private List<ContattoEmergenza> contattiOld;

    private String emailSelected = null;
    private Stage stage;
    @FXML
    private TableColumn<ContattoEmergenza, String> cognomeColumn;

    @FXML
    private TextField cognomeField;

    @FXML
    private TableView<ContattoEmergenza> contattoTable;

    @FXML
    private TableColumn<ContattoEmergenza, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorCognomeLabel;

    @FXML
    private Label errorEmailLabel;

    @FXML
    private Label errorDelete;

    @FXML
    private Label errorNomeLabel;

    @FXML
    private Label errorTelefonoLabel;

    @FXML
    private TableColumn<ContattoEmergenza, String> nomeColumn;

    @FXML
    private TextField nomeField;

    @FXML
    private Text regioneNascitaText1;

    @FXML
    private Text regioneNascitaText11;

    @FXML
    private Text regioneNascitaText111;

    @FXML
    private Text regioneNascitaText111111;

    @FXML
    private TableColumn<ContattoEmergenza, String> telefonoColumn;

    @FXML
    private TextField telefonoField;

    /**
     * permette la crazione di un nuovo oggetto contatto
     */
    @FXML
    void addContatto(ActionEvent event) {
        if(checkFields()){
            if (emailSelected!=null) {
                for (ContattoEmergenza c : contatti){
                    if (c.getEmail().equals(emailSelected)){
                        c.setNome(nomeField.getText());
                        c.setCognome(cognomeField.getText());
                        c.setEmail(emailField.getText());
                        c.setRecapitoTelefonico(telefonoField.getText());
                        emailSelected = null;
                        clear();
                    }
                }
            }
            else if (checkDuplicate()) {
                String nome = nomeField.getText();
                String cognome = cognomeField.getText();
                String email = emailField.getText();
                String telefono = telefonoField.getText();
                contatti.add(new ContattoEmergenza(nome, cognome, email, telefono));
                clear();
            }
            displayContatti();
        }
    }

    /**
     * controlla che i campi non siano vuoti
     */
    private boolean checkFields() {
        boolean flag = true;

        if (nomeField.getText().isBlank() || nomeField.getText().isEmpty()){
            errorNomeLabel.setText("Inserire un Nome");
            flag = false;
        }
        else if (!checkNome()){
            flag = false;
        }

        if (cognomeField.getText().isBlank() || cognomeField.getText().isEmpty()){
            errorCognomeLabel.setText("Inserire un Cognome");
            flag = false;
        }
        else if (!checkCognome()){
            flag = false;
        }

        if (emailField.getText().isBlank() || emailField.getText().isEmpty()) {
            errorEmailLabel.setText("Inserire un Email");
            flag = false;
        }
        else if (!checkEmail()){
            flag = false;
        }

        if (telefonoField.getText().isEmpty() || telefonoField.getText().isEmpty()) {
            errorTelefonoLabel.setText("Inserire un telefono");
            flag = false;
        }
        else if (!checkTelefono()){
            flag = false;
        }

        return flag;
    }

    /**
     * controlla che la mail sia corretta
     */
    private boolean checkEmail() {
        String regex = "^^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailField.getText());

        if (!matcher.matches()) {
            errorEmailLabel.setText("Inserire email valida");
            return false;
        }

        return true;
    }

    /**
     * controlla che il nome sia corretto
     */
    private boolean checkNome() {
        String regex = "^[a-zA-Z ]*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nomeField.getText());

        if (!matcher.matches()) {
            errorNomeLabel.setText("Caratteri non ammessi");
            return false;
        }
        return true;
    }

    /**
     * contolla che il cognome sia corretto
     */
    private boolean checkCognome() {
        String regex = "^[a-zA-Z ]*$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(cognomeField.getText());

        if (!matcher.matches()) {
            errorCognomeLabel.setText("Caratteri non ammessi");
            return false;
        }

        return true;
    }

    /**
     * controlla che il telefono sia corretto
     */
    private boolean checkTelefono() {
        if (!telefonoField.getText().matches("[0-9]{8,10}+")) {
            errorTelefonoLabel.setText("Inserire un numero valido");
            return false;
        }
        return true;
    }

    /**
     * viene chiamata quando si vuole cancellare i campi
     */
    @FXML
    private void clear() {
        nomeField.setText("");
        cognomeField.setText("");
        emailField.setText("");
        telefonoField.setText("");
    }

    /**
     * quando si interagisce con il field dell'inserimento l'errore viene rimosso
     */
    @FXML
    private void hideEmailError() {
        errorEmailLabel.setText("");
    }

    /**
     * quando si interagisce con il field dell'inserimento l'errore viene rimosso
     */
    @FXML
    private void hideNomeError() {
        errorNomeLabel.setText("");
    }

    /**
     * quando si interagisce con il field dell'inserimento l'errore viene rimosso
     */
    @FXML
    private void hideCognomeError() {
        errorCognomeLabel.setText("");
    }

    /**
     * quando si interagisce con il field dell'inserimento l'errore viene rimosso
     */
    @FXML
    void hideTelefonoError() { errorTelefonoLabel.setText("");}

    /**
     * si ritorna alla schermata principale e si salvano i contatti
     */
    @FXML
    private void submit(ActionEvent event) throws IOException {
        if (contatti.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore");
            alert.setContentText("Non e' possibile lasciare un lavoratore senza contatti di emergenza");

            alert.showAndWait();
        } else {
            lavoratore.setConttattiEmergenza(contatti);
            Node source = (Node) event.getSource();
            stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * salva il lavoratore preso come parametro dalla schermata principale
     */
    protected void salvaLavoratore(Lavoratore l){
        lavoratore = l;
        contatti = new ArrayList<ContattoEmergenza>();
        contattiOld = l.getConttattiEmergenza();

        for (ContattoEmergenza c : contattiOld) {
            contatti.add(new ContattoEmergenza(c));
        }
    }

    /**
     * aggiorna la vista della tabella
     */
    protected void displayContatti() {
        ObservableList<ContattoEmergenza> contattiObservableList = FXCollections.observableArrayList();
        for (ContattoEmergenza c : contatti){
            contattiObservableList.add(new ContattoEmergenza(c.getNome(),c.getCognome(),c.getEmail(),c.getRecapitoTelefonico()));
        }
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cognomeColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("recapitoTelefonico"));

        contattoTable.setItems(contattiObservableList);
    }

    /**
     * verifica che non ci siano contatti con stessa mail
     */
    private boolean checkDuplicate(){
        for (ContattoEmergenza c : contatti){
            if (c.getEmail().equals(emailField.getText())){
                errorEmailLabel.setText("stessa mail per piú contatti");
                return false;
            }
        }
        return true;
    }

    /**
     * aggiorna la vista aggiornando i dati della riga selezionata
     */
    @FXML
    void rowClicked() {
        ContattoEmergenza contattoSelezionato = contattoTable.getSelectionModel().getSelectedItem();
        if(contattoSelezionato != null) {
            errorDelete.setText("");
            errorNomeLabel.setText("");
            errorCognomeLabel.setText("");
            errorEmailLabel.setText("");
            errorTelefonoLabel.setText("");
            nomeField.setText(contattoSelezionato.getNome());
            cognomeField.setText(contattoSelezionato.getCognome());
            emailSelected = contattoSelezionato.getEmail();
            emailField.setText(emailSelected);
            telefonoField.setText(contattoSelezionato.getRecapitoTelefonico());
        }
    }

    /**
     * permette la cancellazione del contatto
     */
    @FXML
    private void clearContatto(){
        ContattoEmergenza contattoSelezionato = contattoTable.getSelectionModel().getSelectedItem();
        if (emailSelected != null) {
            try {
                for (ContattoEmergenza c : contatti) {
                    if (c.getEmail().equals(emailSelected)) {
                        contatti.remove(c);
                        clear();
                        displayContatti();
                    }
                }
            } catch (ConcurrentModificationException e) {}

        }
        else errorDelete.setText("Seleziona un contatto per eliminarlo");
    }


}

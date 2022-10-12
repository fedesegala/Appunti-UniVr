package Vista;

import Modello.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InserisciController implements Initializable {
    private Parent root;
    private Scene scene;
    private Stage stage;

    // Elementi della scena
    @FXML
    private CheckBox automunitoBox;
    @FXML
    private Label errorCognomeLabel;
    @FXML
    private Label errorDatadiNascitaLabel;
    @FXML
    private Label errorEmailLabel;
    @FXML
    private Label errorComuneLabel;
    @FXML
    private Label errorComuneResLabel;
    @FXML
    private Label errorNazionalitaLabel;
    @FXML
    private Label errorIndirizzoLabel;
    @FXML
    private Label errorNomeLabel;
    @FXML
    private Label errorTelefonoLabel;
    @FXML
    private TextField cognomeField;
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private TextField emailField;
    @FXML
    private CheckBox esteroBox;
    @FXML
    private TextField indirizzoField;
    @FXML
    private ComboBox<String> nazionalitaComboBox;
    @FXML
    private TextField nomeField;
    @FXML
    private ComboBox<String> comuneBox;
    @FXML
    private ComboBox<String> comuneResBox;
    @FXML
    private ComboBox<String> provinciaBox;
    @FXML
    private ComboBox<String> provinciaResBox;
    @FXML
    private ComboBox<String> regioneBox;
    @FXML
    private ComboBox<String> regioneResBox;
    @FXML
    private TextArea specializzazioniField;
    @FXML
    private TextField telefonoField;
    @FXML
    private Text regioneNascitaText;
    @FXML
    private Text comuneNascitaText;
    @FXML
    private Text provinciaNascitaText;
    @FXML
    private Label errorSpecializzazioniLabel;

    // Directory dei vari database
    static String dbAree = "src/DataBase/AreeGeografiche.txt";
    static String dbNazionalita = "src/DataBase/Nazionalita.txt";


    // Istanza del programma
    private final Modello instance = Modello.getInstance();

    // Lavoratore attualmente inserito / modificato
    private Lavoratore lavoratore;

    /**
     * Permette al dipendente attualmente connesso la disconnessione
     * dall'applicativo
     */
    @FXML
    public void userLogOut(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    // Passaggio ad altre schermate della dashboard

    /**
     * Effettua un cambio di scena, portando alla visualizzazione generale
     * di tutti i lavoratori stagionali
     */
    @FXML
    public void visualizzaDati(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("visualizza.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    /**
     * Effettua un cambio di scena, portando alla visualizzazione dei dati
     * del dipendente attualmente connesso
     */
    @FXML
    public void datiDipendente(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dipendente.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Viene chiamata nel momento in cui viene schiacciato il tasto salva,
     * effettua un controllo su tutti i campi, compreso se è stata inserita almeno
     * una lingua parlata e un contatto di emergenza, se tutto viene inserito in modo
     * corretto, aggiunge al "database" un nuovo lavoratore
     */
    @FXML
    public void submit(ActionEvent event) {
        if(checkFields()){
            boolean natoEstero = esteroBox.isSelected();
            Comune luogoNascita;
            if (natoEstero) {
                luogoNascita = new Comune();
            } else {
                luogoNascita = new Comune(comuneBox.getValue(), provinciaBox.getValue(), regioneBox.getValue());
            }

            Comune comuneResidenza = new Comune(comuneResBox.getValue(), provinciaResBox.getValue(), regioneResBox.getValue());

            boolean automunito = automunitoBox.isSelected();

            lavoratore.setNome(nomeField.getText());
            lavoratore.setCognome(cognomeField.getText());
            lavoratore.setEmail(emailField.getText());
            lavoratore.setLuogoNascita(luogoNascita);
            lavoratore.setNazionalita(nazionalitaComboBox.getValue());
            lavoratore.setDataNascita(dataDatePicker.getValue());
            lavoratore.setComuneResidenza(comuneResidenza);
            lavoratore.setIndirizzo(indirizzoField.getText());
            lavoratore.setRecapitoTelefonico(telefonoField.getText());
            lavoratore.setSpecializzazioni(specializzazioniField.getText());
            lavoratore.setAutomunito(automunito);

            instance.addLavoratore(lavoratore);

            clear();

            // imposto il nuovo lavoratore vuoto
            lavoratore = new Lavoratore();
            lavoratore.setEsperienzeLavorative(new ArrayList<EsperienzaLavorativa>());
            lavoratore.setLingueParlate(new ArrayList<Lingua>());
            lavoratore.setPatenti(new ArrayList<Patente>());
            lavoratore.setConttattiEmergenza(new ArrayList<ContattoEmergenza>());
            lavoratore.setDisponibilita(new ArrayList<Disponibilita>());
        }
    }

    /**
     * cancella tutti i campi attualmente inseriti
     */
    private void clear() {
        nomeField.setText(null);
        cognomeField.setText(null);
        emailField.setText(null);
        dataDatePicker.setValue(null);
        comuneBox.setValue(null);
        comuneResBox.setValue(null);
        provinciaBox.setValue(null);
        provinciaResBox.setValue(null);
        regioneBox.setValue(null);
        regioneResBox.setValue(null);
        specializzazioniField.setText(null);
        nazionalitaComboBox.setValue(null);
        indirizzoField.setText(null);
        telefonoField.setText(null);
        automunitoBox.setSelected(false);
        esteroBox.setSelected(false);
    }


    // Funzioni che nascondono i messaggi di errore quando si fa un inserimento
    @FXML
    void hideItaly(ActionEvent event) {
        errorComuneLabel.setText("");
        if (esteroBox.isSelected()) {
            regioneBox.setVisible(false);
            provinciaBox.setVisible(false);
            comuneBox.setVisible(false);
            regioneNascitaText.setVisible(false);
            provinciaNascitaText.setVisible(false);
            comuneNascitaText.setVisible(false);

        } else {
            regioneBox.setVisible(true);
            provinciaBox.setVisible(true);
            comuneBox.setVisible(true);
            regioneNascitaText.setVisible(true);
            provinciaNascitaText.setVisible(true);
            comuneNascitaText.setVisible(true);
        }
    }
    @FXML
    private void hideEmailError() {
        errorEmailLabel.setText("");
    }
    @FXML
    private void hideNomeError() {
        errorNomeLabel.setText("");
    }
    @FXML
    private void hideCognomeError() {
        errorCognomeLabel.setText("");
    }
    @FXML
    private void hideComuneError() {
        if (regioneBox.getValue() != null && provinciaBox.getValue() != null
                && comuneBox.getValue() != null) {
            errorComuneLabel.setText("");
        }
    }
    @FXML
    private void hideNazionalitaError() {
        if (nazionalitaComboBox.getValue() != null) {
            errorNazionalitaLabel.setText("");
        }
    }
    @FXML
    private void hideComuneResError() {
        if (regioneResBox.getValue() != null && provinciaResBox.getValue() != null
                && comuneResBox.getValue() != null) {
            errorComuneResLabel.setText("");
        }
    }
    @FXML
    private void hideDataNascitaError() {
        if (dataDatePicker.getValue() != null){
            errorDatadiNascitaLabel.setText("");
        }
    }
    @FXML
    void hideIndirizzoError() { errorIndirizzoLabel.setText("");}
    @FXML
    void hideTelefonoError() { errorTelefonoLabel.setText("");}
    @FXML
    void hideSpecializzazioniError() {
        errorSpecializzazioniLabel.setText("");
    }

    // metodi ausiliari per il metodo checkfields

    /**
     * Verifica che sia possibile effettuare un submit dei dati inseriti
     * @return true nel caso in cui sia possibile, false in caso contrario
     */
    private boolean checkFields() {
        boolean flag = true;

        if (nomeField.getText().isBlank() || nomeField.getText().isEmpty()){
            errorNomeLabel.setText("Inserire un Nome");
            flag = false;
        }
        else if (!checkNome()) {
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

        if (!esteroBox.isSelected() && (regioneBox.getValue() == null || provinciaBox.getValue() == null ||
                comuneBox.getValue() == null)) {
            errorComuneLabel.setText("Inserire un Comune");
            flag = false;
        }

        if (nazionalitaComboBox.getValue() == null) {
            errorNazionalitaLabel.setText("Inserire una Nazionalità");
            flag = false;
        }

        if (regioneResBox.getValue() == null || provinciaResBox.getValue() == null || comuneResBox.getValue() == null) {
            errorComuneResLabel.setText("Inserire un comune");
            flag = false;
        }

        if (dataDatePicker.getValue() == null) {
            errorDatadiNascitaLabel.setText("Inserire una data di nascita valida");
            flag = false;
        }

        if (indirizzoField.getText().isEmpty() || indirizzoField.getText().isEmpty()) {
            errorIndirizzoLabel.setText("Inserire un indirizzo");
            flag = false;
        } else if (!checkIndirizzo()) {
            flag = false;
        }

        if (telefonoField.getText().isEmpty() || telefonoField.getText().isEmpty()) {
            errorTelefonoLabel.setText("Inserire un telefono");
            flag = false;
        }else if (!checkTelefono()){
            flag = false;
        }

        // controllo esistenza di altro contatto con uguale email
        if (!checkDuplicati()) {
            flag = false;
        }

        // controllo maggiore etò
        if (!checkEta()) {
            flag = false;
        }

        // controllo che le specializzazioni non contengano '_'
        if (!checkSpecializzazioni()) {
            flag = false;
        }

        // controllo che sia stato inserito almeno un contatto di emergenza
        if (!checkContattiEmergenza()) {
            flag = false;
        }

        // controllo che sia stata inserita almeno una lingua parlata
        if (!checkLingueParlate()) {
            flag = false;
        }

        return flag;
    }

    /**
     * Verifica che nel database non sia già presente un altro lavoratore con uguale email
     * @return true nel caso in cui sia tutto a posto, false, se abbiamo un duplicato
     */
    private boolean checkDuplicati() {
        String email = emailField.getText();
        for (Lavoratore l : instance.getElencoLavoratori()) {
            if (l.getEmail().equals(email)) {
                errorEmailLabel.setText("Email già presente");
                return false;
            }
        }

        return true;
    }

    /**
     * Controlla chel'utente inserito abbia almeno 16 di età
     * @return true se è tutto a posto, false in caso contrario
     */
    private boolean checkEta() {
        LocalDate date = dataDatePicker.getValue();
        if (date == null)
            return false;

        LocalDate oggi = LocalDate.now();

        Period p = Period.between(date,oggi);
        if (p.getYears() >= 16) {
            return true;
        }

        errorDatadiNascitaLabel.setText("Età < 16 anni");
        return false;
    }

    /**
     * Verifica che la mail rispetti il corretto pattern
     * @return true se è tutto a posto, false in caso contrario
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
     * Verifica che il nome rispetti il corretto pattern
     * @return true nel caso in cui il pattern sia rispettato, false in caso contrario
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
     * Verifica che il cognome rispetti il corretto pattern
     * @return true nel caso in cui il pattern sia rispettato, false in caso contrario
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
     * Verifica che il numero di telefono inserito rispetti il corretto pattern
     * @return true nel caso in cui il pattern sia rispettato, false in caso contrario
     */
    private boolean checkTelefono() {
        if (!telefonoField.getText().matches("[0-9]{8,10}")) {
            errorTelefonoLabel.setText("Inserire un numero valido");
            return false;
        }
        return true;
    }

    /**
     * Verifica che l'indirizzo inserito rispetti il corretto pattern
     * @return true nel caso in cui il pattern sia rispettato, false in caso contrario
     */
    private boolean checkIndirizzo() {
        if (!indirizzoField.getText().matches("^[a-zA-Z ]*[0-9]+")) {
            errorIndirizzoLabel.setText("Inserire indirizzo valido");
            return false;
        }
        return true;
    }

    /**
     * Verifica che nel campo specializzazioni non sia presente il carattere '_' che viene utilizzato
     * per dividere i record all'interno del database
     * @return true se il pattern viene rispettato, false altrimenti
     */
    private boolean checkSpecializzazioni() {
        if (specializzazioniField.getText().contains("_")) {
            errorSpecializzazioniLabel.setText("Il campo specializzazioni non può contenere '_'");
            return false;
        }

        return true;
    }
    
    /**
     * Verifica che sia stato inserito almeno un contatto di emergenza
     */
    private boolean checkContattiEmergenza() {
        if (lavoratore.getConttattiEmergenza().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore");
            alert.setContentText("Inserire almeno un contatto di emergenza");

            alert.showAndWait();
            return false;
        }

        return true;
    }
    /**
     * Verifica che sia stata inserita almeno una lingua parlata
     */
    private boolean checkLingueParlate() {
        if (lavoratore.getLingueParlate().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore");
            alert.setContentText("Inserire almeno una lingua parlata");

            alert.showAndWait();
            return false;
        }

        return true;
    }

    // apertura finestre di dialogo

    /**
     * apre una schermata di dialogo che permette di aggiungere esperienze lavorative al
     * lavoratore attualmente in gestione
     */
    @FXML
    public void addEsperienze(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("esperienze.fxml"));
        root = loader.load();

        EsperienzeController esperienzeController = loader.getController();
        esperienzeController.salvaLavoratore(lavoratore);
        esperienzeController.displayEsperienze();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

    }
    /**
     * apre una schermata di dialogo che permette di aggiungere lingue parlate al
     * lavoratore attualmente in gestione
     */
    @FXML
    public void addLingue(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lingueParlate.fxml"));
        root = loader.load();

        LingueParlateController lingueParlateController = loader.getController();
        lingueParlateController.salvaLavoratore(lavoratore);
        lingueParlateController.displayLingue();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
    /**
     * apre una schermata di dialogo che permette di aggiungere contatti d'emergenza al
     * lavoratore attualmente in gestione
     */
    @FXML
    public void addContatti(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("contattiEmergenza.fxml"));
        root = loader.load();

        ContattiEmergenzaController contattiEmergenzaController = loader.getController();
        contattiEmergenzaController.salvaLavoratore(lavoratore);
        contattiEmergenzaController.displayContatti();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
    /**
     * apre una schermata di dialogo che permette di aggiungere patenti al
     * lavoratore attualmente in gestione
     */
    @FXML
    public void addPatenti(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patenti.fxml"));
        root = loader.load();

        PatentiController patentiController = loader.getController();
        patentiController.salvaLavoratore(lavoratore);
        patentiController.displayPatenti();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
    /**
     * apre una schermata di dialogo che permette di aggiungere disponibilità al
     * lavoratore attualmente in gestione
     */
    @FXML
    public void addDisponibilita(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("disponibilita.fxml"));
        root = loader.load();

        DisponibilitaController disponibilitaController = loader.getController();
        disponibilitaController.salvaLavoratore(lavoratore);
        disponibilitaController.displayDisponibilita();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

    }

    /**
     * @param url
     * @param resourceBundle
     * Inizializza i campi del luogo e data di nascita e della nazionalita
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lavoratore = new Lavoratore();
        lavoratore.setEsperienzeLavorative(new ArrayList<EsperienzaLavorativa>());
        lavoratore.setLingueParlate(new ArrayList<Lingua>());
        lavoratore.setPatenti(new ArrayList<Patente>());
        lavoratore.setConttattiEmergenza(new ArrayList<ContattoEmergenza>());
        lavoratore.setDisponibilita(new ArrayList<Disponibilita>());

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
        regioneResBox.getItems().clear();

        regioneBox.setItems(regioniObList);
        regioneResBox.setItems(regioniObList);

        regioneBox.setOnAction(this::setProvince);
        regioneResBox.setOnAction(this::setProvinceRes);
        provinciaBox.setOnAction(this::setComune);
        provinciaResBox.setOnAction(this::setComuneRes);


        try {
            lines = Files.readAllLines(Paths.get(dbNazionalita), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList nazionalitaObList = FXCollections.observableList(lines);
        nazionalitaComboBox.getItems().clear();
        nazionalitaComboBox.setItems(nazionalitaObList);
    }

    /**
     * @param event
     * Inizializza le province
     */
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

    /**
     * @param event
     * Inizializza i comuni
     */
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

    /**
     * @param event
     * Inizializza le province di residenza
     */
    private void setProvinceRes(ActionEvent event) {
        List<String> lines = Collections.emptyList();
        String regioneSelezionata = regioneResBox.getValue();

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

        provinciaResBox.getItems().clear();
        provinciaResBox.setItems(obList);

    }

    /**
     * @param event
     * Inizializza i comuni di residenza
     */
    private void setComuneRes(ActionEvent event) {
        List<String> lines = Collections.emptyList();
        String provinciaSelezionata = provinciaResBox.getValue();

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

        comuneResBox.getItems().clear();
        comuneResBox.setItems(obList);

    }
}

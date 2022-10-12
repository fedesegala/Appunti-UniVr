package Vista;

import Modello.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class VisualizzaController implements Initializable{

    private Parent root;
    private Stage stage;
    private Scene scene;

    // Struttura dati dove vengono salvati i lavoratori
    private static Modello instance = Modello.getInstance();

    private Lavoratore lavoratoreSelezionato;
    protected static boolean generalitaModificate = false;
    protected static Ricerca ricerca;



    @FXML
    private TextField cercaTextField;
    @FXML
    private TableColumn<Lavoratore, String> nomeColumn;
    @FXML
    private TableColumn<Lavoratore, String> cognomeColumn;
    @FXML
    private TableColumn<Lavoratore, String> emailColumn;
    @FXML
    private TableColumn<Lavoratore, String> nazionalitaColumn;
    @FXML
    private TableColumn<Lavoratore, String> residenzaColumn;
    @FXML
    private TableView<Lavoratore> lavoratoriTableView;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label cognomeLabel;
    @FXML
    private Text selectionText;
    @FXML
    private Button lingueButton;
    @FXML
    private Button patentiButton;
    @FXML
    private Button contattiButton;
    @FXML
    private Button disponibilitaButton;
    @FXML
    private Button esperienzeButton;
    @FXML
    private Button generalitaButton;
    @FXML
    private Button cancellaButton;
    @FXML
    private Text scegliText;
    @FXML
    private ImageView imgdisp;
    @FXML
    private ImageView imgem;
    @FXML
    private ImageView imgesp;
    @FXML
    private ImageView imglang;
    @FXML
    private ImageView imgpat;
    @FXML
    private ImageView imgusr;


    /**
     * @param url
     * @param resourceBundle
     * Funzione che inizializza la tabella e poppola una lista di lavoratori
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FiltersController.emptyFields();
        showButtons(false);

        ObservableList<Lavoratore> lavoratoreObservableList = FXCollections.observableArrayList();

        aggiornaTabella(instance.getElencoLavoratori(),lavoratoreObservableList);

        // Ricerca
        attivaRicerca(lavoratoreObservableList);
    }

    /**
     * @param lavoratoreObservableList
     * atttiva il listener per la ricerca veloce
     */
    private void attivaRicerca(ObservableList<Lavoratore> lavoratoreObservableList){
        // Creo la lista filtrata che userò per le ricerche
        FilteredList<Lavoratore> filteredData = new FilteredList<>(lavoratoreObservableList, b-> true);

        cercaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Lavoratore -> {

                // Se non c'è nessun valore nel textfield mostro tutta la tabella
                if (newValue.isEmpty() || newValue.isBlank() || newValue==null){
                    return true;
                }

                String cercaTextField = newValue.toLowerCase();

                if (Lavoratore.getCognome().toLowerCase().contains(cercaTextField)){
                    return true; // Ho trovato una corrispondenza col nome

                }
                else if (Lavoratore.getNome().toLowerCase().contains(cercaTextField)){
                    return true;
                }
                else if (Lavoratore.getEmail().toLowerCase().contains(cercaTextField)){
                    return true;
                }
                else if (Lavoratore.getDataNascita().toString().contains(cercaTextField)){
                    return true;
                }
                else if (Lavoratore.getLingueParlate().toString().contains(cercaTextField)){
                    return true;
                }
                else if (Lavoratore.getDisponibilita().toString().contains(cercaTextField)){
                    return true;
                }
                else if (Lavoratore.getNazionalita().toLowerCase().contains(cercaTextField)) {
                    return true;
                }
                else if (Lavoratore.getIndirizzo().toLowerCase().contains(cercaTextField)){
                    return true;
                }
                else{
                    for (EsperienzaLavorativa e : Lavoratore.getEsperienzeLavorative()) {
                        for (Mansione m : e.getMansioni()) {
                            if (m.toString().toLowerCase().contains(cercaTextField)) {
                                return true;
                            }
                        }
                        if (e.getAzienda().toLowerCase().contains(cercaTextField)) {
                            return true;
                        }
                    }
                    for (Disponibilita d : Lavoratore.getDisponibilita()){
                        Comune c = d.getLuogoDisponibilita();
                        if (c.toString().toLowerCase().contains(cercaTextField)){
                            return true;
                        }
                    }
                    for (Lingua l : Lavoratore.getLingueParlate()){
                        if (l.toString().toLowerCase().contains(cercaTextField)){
                            return true;
                        }
                    }
                    for (Patente p : Lavoratore.getPatenti()){
                        if (p.toString().toLowerCase().contains(cercaTextField)){
                            return true;
                        }
                    }
                    return Lavoratore.getComuneResidenza().toString().toLowerCase().contains(cercaTextField);
                }
            });
        });

        SortedList<Lavoratore> sortedData = new SortedList<>(filteredData);

        // Bind tra il risultato e la tabella
        sortedData.comparatorProperty().bind(lavoratoriTableView.comparatorProperty());

        // Aggiorno la tabella con i riultati trovati
        lavoratoriTableView.setItems(sortedData);
    }

    /**
     * @param lavoratori
     * @param lavoratoreObservableList
     * riempie la tabella con i lavoratri passati come parametro
     */
    private void aggiornaTabella(Collection<Lavoratore> lavoratori, ObservableList<Lavoratore> lavoratoreObservableList ){
        for (Lavoratore l : lavoratori ) {

            String nome = l.getNome();
            String cognome = l.getCognome();
            String email = l.getEmail();
            String nazionalita = l.getNazionalita();
            Comune luogoresidenza = l.getComuneResidenza();

            lavoratoreObservableList.add(new Lavoratore(nome,cognome,email,l.getLuogoNascita(),l.getDataNascita(),nazionalita,luogoresidenza,l.getIndirizzo(),l.getRecapitoTelefonico(),l.getSpecializzazioni(),l.isAutomunito(),l.getEsperienzeLavorative(),l.getPatenti(),l.getLingueParlate(),l.getDisponibilita(),l.getConttattiEmergenza()));
        }

        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cognomeColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nazionalitaColumn.setCellValueFactory(new PropertyValueFactory<>("nazionalita"));
        residenzaColumn.setCellValueFactory(new PropertyValueFactory<>("comuneResidenza"));
        lavoratoriTableView.setItems(lavoratoreObservableList);

    }

    /**
     * @param event
     * @throws IOException
     * fa scollegare l'utente
     */
    @FXML
    public void userLogOut(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param event
     * @throws IOException
     * apre la schermata di inserimento di un nuovo lavoratore
     */
    @FXML
    public void inserisciDati(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("inserisci.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param event
     * @throws IOException
     * apre la schermata che mostra i dati del dipendente
     */
    @FXML
    public void datiDipendente(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dipendente.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * @param flag
     * funzione che mostra / nasconde i bottoni
     */
    void showButtons(boolean flag){
        selectionText.setVisible(flag);
        scegliText.setVisible(flag);
        lingueButton.setVisible(flag);
        contattiButton.setVisible(flag);
        patentiButton.setVisible(flag);
        disponibilitaButton.setVisible(flag);
        esperienzeButton.setVisible(flag);
        generalitaButton.setVisible(flag);
        imgdisp.setVisible(flag);
        imgem.setVisible(flag);
        imgesp.setVisible(flag);
        imglang.setVisible(flag);
        imgpat.setVisible(flag);
        imgusr.setVisible(flag);
        cancellaButton.setVisible(flag);
        nomeLabel.setVisible(flag);
        cognomeLabel.setVisible(flag);
    }

    /**
     * quando si seleziona un lavoratore mostra i bottoni
     */
    @FXML
    void rowClicked() {
        lavoratoreSelezionato = lavoratoriTableView.getSelectionModel().getSelectedItem();
        if(lavoratoreSelezionato != null) {
            showButtons(true);
            nomeLabel.setText(lavoratoreSelezionato.getNome());
            cognomeLabel.setText(lavoratoreSelezionato.getCognome());
        }
    }

    /**
     * @param event
     * @throws IOException
     * apre la vista delle generalità
     */
    @FXML
    public void showGeneralita(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("generalita.fxml"));
        root = loader.load();

        Lavoratore old = new Lavoratore (
                lavoratoreSelezionato.getNome(),
                lavoratoreSelezionato.getCognome(),
                lavoratoreSelezionato.getEmail(),
                lavoratoreSelezionato.getLuogoNascita(),
                lavoratoreSelezionato.getDataNascita(),
                lavoratoreSelezionato.getNazionalita(),
                lavoratoreSelezionato.getComuneResidenza(),
                lavoratoreSelezionato.getIndirizzo(),
                lavoratoreSelezionato.getRecapitoTelefonico(),
                lavoratoreSelezionato.getSpecializzazioni(),
                lavoratoreSelezionato.isAutomunito(),
                lavoratoreSelezionato.getEsperienzeLavorative(),
                lavoratoreSelezionato.getPatenti(),
                lavoratoreSelezionato.getLingueParlate(),
                lavoratoreSelezionato.getDisponibilita(),
                lavoratoreSelezionato.getConttattiEmergenza()
        );

        GeneralitaController generalitaController = loader.getController();
        generalitaController.salvaLavoratore(lavoratoreSelezionato);
        generalitaController.displayGeneralita(lavoratoreSelezionato);

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        if (generalitaModificate) {
            instance.editLavoratore(lavoratoreSelezionato, !lavoratoreSelezionato.getEmail().equals(old.getEmail()), old);
            generalitaModificate = false;
            instance = Modello.getInstance();
        }

        filtro();
    }

    /**
     * imposta il flag di generalità modificate a true, utile in seguito per il salvataggio di dati
     */
    protected static void setGeneralitaModificate () {
        generalitaModificate = true;
    }

    /**
     * @param event
     * @throws IOException
     * apre la schermata delle esperienze lavorative per il lavoratore selezionato
     */
    @FXML
    public void showEsperienze(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("esperienze.fxml"));
        root = loader.load();

        EsperienzeController esperienzeController = loader.getController();
        esperienzeController.salvaLavoratore(lavoratoreSelezionato);
        esperienzeController.displayEsperienze();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        instance.editEsperienze(lavoratoreSelezionato);

        instance = Modello.getInstance();

        filtro();
    }

    /**
     * @param event
     * @throws IOException
     * apre la schermata delle lingue lavorative per il lavoratore selezionato
     */
    @FXML
    public void showLingue(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lingueParlate.fxml"));
        root = loader.load();


        LingueParlateController lingueParlateController = loader.getController();
        lingueParlateController.salvaLavoratore(lavoratoreSelezionato);
        lingueParlateController.displayLingue();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        instance.editLavoratore(lavoratoreSelezionato, false, null);
        instance = Modello.getInstance();

        filtro();
    }

    /**
     * @param event
     * @throws IOException
     * apre la schermata dei contatti per il lavoratore selezionato
     */
    @FXML
    public void showContatti(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("contattiEmergenza.fxml"));
        root = loader.load();

        ContattiEmergenzaController contattiEmergenzaController = loader.getController();
        contattiEmergenzaController.salvaLavoratore(lavoratoreSelezionato);
        contattiEmergenzaController.displayContatti();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        instance.editContattiEmergenza(lavoratoreSelezionato);
        instance = Modello.getInstance();

        filtro();
    }

    /**
     * @param event
     * @throws IOException
     * apre la schermata delle patenti per il lavoratore selezionato
     */
    @FXML
    public void showPatenti(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patenti.fxml"));
        root = loader.load();

        PatentiController patentiController = loader.getController();
        patentiController.salvaLavoratore(lavoratoreSelezionato);
        patentiController.displayPatenti();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        instance.editLavoratore(lavoratoreSelezionato, false, null);
        instance = Modello.getInstance();

        filtro();
    }

    /**
     * @param event
     * @throws IOException
     * apre la schermata delle disponibilità per il lavoratore selezionato
     */
    @FXML
    public void showDisponibilita(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("disponibilita.fxml"));
        root = loader.load();

        DisponibilitaController disponibilitaController = loader.getController();
        disponibilitaController.salvaLavoratore(lavoratoreSelezionato);
        disponibilitaController.displayDisponibilita();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        instance.editDisponibilita(lavoratoreSelezionato);
        instance = Modello.getInstance();

        filtro();
    }

    /**
     * @param event
     * @throws IOException
     * cancella il lavoratore selezionato
     */
    @FXML
    public void clearLavoratore(ActionEvent event) throws IOException{
        instance.cancelLavoratore(lavoratoreSelezionato);
        instance = Modello.getInstance();


        filtro();


        showButtons(false);
    }

    /**
     * @param event
     * @throws IOException
     * appre la schermata di filtaggio
     */
    @FXML
    public void filtraRicerca(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("filters.fxml"));
        root = loader.load();

        scene = new Scene(root);
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        filtro();


    }

    /**
     * applica i filtri di ricerca selezionati
     */
    private void filtro() {
        if (ricerca != null) {
            Set<Lavoratore> lavoratoriFiltrati;

            if (ricerca.isRicercaAnd()) {
                lavoratoriFiltrati = filtraAnd();
            } else {
                lavoratoriFiltrati = filtraOr();
            }

            ObservableList<Lavoratore> lavoratoreObservableList = FXCollections.observableArrayList();

            aggiornaTabella(lavoratoriFiltrati,lavoratoreObservableList);
            attivaRicerca(lavoratoreObservableList);
        } else {
            ObservableList<Lavoratore> lavoratoreObservableList = FXCollections.observableArrayList();

            aggiornaTabella(instance.getElencoLavoratori(), lavoratoreObservableList);
            attivaRicerca(lavoratoreObservableList);
        }
    }

    /**
     * @return filtra i lavoratori in and
     */
    private Set<Lavoratore> filtraAnd() {
        Set<Lavoratore> result = new HashSet<Lavoratore>();
        Set<Lavoratore> esclusi = new HashSet<Lavoratore>();

        // aggingo chi non rispetta i filtri a una lista di esclusi
        for (Lavoratore l : instance.getElencoLavoratori()) {
            if (ricerca.getNome() != null && !ricerca.getNome().contains(l.getNome().toLowerCase())) {
                esclusi.add(l);
            }
            if (ricerca.getCognome() != null && !ricerca.getCognome().contains(l.getCognome().toLowerCase())) {
                esclusi.add(l);
            }
            if (ricerca.getLingue() != null) {
                for (Lingua lingua : ricerca.getLingue()) {
                    if (!l.getLingueParlate().toString().toLowerCase().contains(lingua.toString().toLowerCase())) {
                        esclusi.add(l);
                    }
                }
            }
            // da modificare
            if (ricerca.getInizio() != null && ricerca.getFine() != null) {
                if (l.getDisponibilita().isEmpty()) {
                    esclusi.add(l);
                }
                boolean found = false;
                for (Disponibilita d : l.getDisponibilita()) {
                    if (d.getPeriodoDisponibilita().getInizio().compareTo(ricerca.getInizio()) <= 0 &&
                            d.getPeriodoDisponibilita().getFine().compareTo(ricerca.getFine()) >= 0) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    esclusi.add(l);
                }
            }
            if (ricerca.getLuogo() != null) {
                if (!l.getComuneResidenza().toString().toLowerCase().contains(ricerca.getLuogo()))
                    esclusi.add(l);
            }
            if(ricerca.getMansione()!=null){
                for (String mansione : ricerca.getMansione()) {
                    if (l.getEsperienzeLavorative().isEmpty()) {
                        esclusi.add(l);
                    } else {
                        boolean found = false;
                        for (EsperienzaLavorativa esp : l.getEsperienzeLavorative()) {
                            for (Mansione m : esp.getMansioni()) {
                                if (m.toString().toLowerCase().contains(mansione))
                                    found = true;
                            }
                        }

                        if (!found) {
                            esclusi.add(l);
                        }
                    }
                }
            }
            if (ricerca.getPatente() != null) {
                for (Patente patente : ricerca.getPatente()) {
                    if (!l.getPatenti().contains(patente)) {
                        esclusi.add(l);
                    }
                }
            }
            if (ricerca.isRicercaAutomunito()) {
                if (!ricerca.isAutomunito() == l.isAutomunito()) {
                    esclusi.add(l);
                }
            }


        }

        // metto nel risultato solo chi non e' stato escluso
        for (Lavoratore l : instance.getElencoLavoratori()) {
            boolean found = false;
            for (Lavoratore escluso : esclusi) {
                if (escluso.getEmail().equals(l.getEmail())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                result.add(l);
            }
        }

        return result;
    }

    /**
     * @return
     * filtra i lavoratori in or
     */
    private Set<Lavoratore> filtraOr() {
        Set<Lavoratore> esclusi = new HashSet<Lavoratore>();
        Set<Lavoratore> result = new HashSet<Lavoratore>();

        for (Lavoratore l : instance.getElencoLavoratori()) {
            // tutti i filtri sono vuoti
            if (ricerca.getNome() == null && ricerca.getCognome() == null && ricerca.getLingue() == null &&
                ricerca.getInizio() == null && ricerca.getFine() == null && ricerca.getLuogo() == null &&
                ricerca.getMansione() == null && !ricerca.isRicercaAutomunito() && ricerca.getPatente() == null) {
                break;
            } else { // ci sono dei filtri compilati
                // se non trovo almeno un match aggiungo il lavoratore alla lista degli esclusi
                boolean found = false;
                if (ricerca.getNome() != null && ricerca.getNome().toString().contains(l.getNome().toLowerCase())) {
                    found = true;
                    continue;
                }
                if (ricerca.getCognome() != null && ricerca.getCognome().toString().contains(l.getCognome().toLowerCase())) {
                    found = true;
                    continue;
                }
                if (ricerca.getLingue() != null) {
                    boolean lingueTrovate = false;
                    for (Lingua lingua : ricerca.getLingue()) {
                        if (l.getLingueParlate().contains(lingua)) {
                            lingueTrovate = true;
                            break;
                        }
                    }
                    if (lingueTrovate) {
                        found = true;
                        continue;
                    }
                }
                if (ricerca.getInizio() != null) {
                    if (!l.getDisponibilita().isEmpty()) {
                        boolean disponibilitaTrovata = false;
                        for (Disponibilita d : l.getDisponibilita()) {
                            if (d.getPeriodoDisponibilita().getInizio().compareTo(ricerca.getInizio()) <= 0) {
                                disponibilitaTrovata = true;
                                break;
                            }
                        }
                        if (disponibilitaTrovata) {
                            found = true;
                            continue;
                        }
                    }
                }
                if (ricerca.getFine() != null) {
                    if (!l.getDisponibilita().isEmpty()) {
                        boolean disponibilitaTrovata = false;
                        for (Disponibilita d : l.getDisponibilita()) {
                            if (d.getPeriodoDisponibilita().getFine().compareTo(ricerca.getFine()) >= 0) {
                                disponibilitaTrovata = true;
                                break;
                            }
                        }
                        if (disponibilitaTrovata) {
                            found = true;
                        }
                    }
                }
                if (ricerca.getPatente() != null) {
                    boolean matchPatente = false;
                    for (Patente p : ricerca.getPatente()) {
                        if (l.getPatenti().contains(p)) {
                            matchPatente = true;
                            break;
                        }
                    }

                    if (matchPatente) {
                        found = true;
                    }
                }
                if (ricerca.getLuogo() != null) {
                    if (l.getComuneResidenza().toString().toLowerCase().contains(ricerca.getLuogo())) {
                        found = true;
                        continue;
                    }
                }
                if (ricerca.getMansione() != null) {
                    boolean mansioneTrovata = false;
                    for (EsperienzaLavorativa esp : l.getEsperienzeLavorative()) {
                        for (String m : ricerca.getMansione()) {
                            if (esp.getMansioni().toString().toLowerCase().contains(m)) {
                                mansioneTrovata = true;
                                break;
                            }
                        }
                    }
                    if (mansioneTrovata) {
                        found = true;
                        continue;
                    }
                }
                if (ricerca.isRicercaAutomunito()) {
                    if (l.isAutomunito() == ricerca.isAutomunito()) {
                        found = true;
                        continue;
                    }
                }

                if (!found) {
                   esclusi.add(l);
                }
            }
        }

        // metto nel risultato solo chi non e' stato escluso
        for (Lavoratore l : instance.getElencoLavoratori()) {
            boolean found = false;
            for (Lavoratore escluso : esclusi) {
                if (escluso.getEmail().equals(l.getEmail())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                result.add(l);
            }
        }

        return result;
    }
}

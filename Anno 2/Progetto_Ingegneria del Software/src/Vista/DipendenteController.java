package Vista;

import Modello.Modello;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import Modello.Dipendente;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller per la schermata del dipendente
 */
public class DipendenteController implements Initializable {
    private static final Modello instance = Modello.getInstance();
    private boolean password = false;

    private Parent root;
    private Scene scene;
    private Stage stage;
    @FXML
    private Label cognomeLabel;

    @FXML
    private Label dataNascitaLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label luogoNascitaLabel;

    @FXML
    private Label nomeLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button showPassword;

    @FXML
    public void userLogOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void viewPassword(ActionEvent event) {
        if (!password) {
            password = true;
            showPassword.setText("Nascondi Password");
            passwordLabel.setText(instance.getDipendente().getPassword());
        } else {
            password = false;
            passwordLabel.setText("***");
            showPassword.setText("Mostra Password");
        }
    }

    @FXML
    public void visualizzaDati(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("visualizza.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void inserisciDati(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("inserisci.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Dipendente user = instance.getDipendente();
        emailLabel.setText(user.getEmail());
        nomeLabel.setText(user.getNome());
        cognomeLabel.setText(user.getCognome());
        dataNascitaLabel.setText(user.getDataNascita().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        luogoNascitaLabel.setText(user.getLuogoNascita().getNome() + ", " + user.getLuogoNascita().getProvincia());
    }
}

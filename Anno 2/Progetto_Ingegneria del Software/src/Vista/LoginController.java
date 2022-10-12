package Vista;

import Modello.Comune;
import Modello.Dipendente;
import Modello.Modello;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class LoginController {

    private static final String dbPathname = "src/DataBase/Accesso.txt";
    Modello instance = Modello.getInstance();
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void userLogIn(ActionEvent event) throws IOException {
        Dipendente user = checkLogin();
        if (user != null) {
            //imposto il dipendente attualmente connesso
            instance.setDipendente(user);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("visualizza.fxml"));
            root = loader.load();

            // cambio la scena
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    private Dipendente checkLogin() throws IOException {
        String userName = usernameField.getText();
        String password = String.valueOf(passwordField.getText());

        Dipendente utente = autenticaUtente(userName, password);

        if (utente != null) {
            return utente;
        } else if (usernameField.getText().isEmpty() && passwordField.getText().isEmpty()) {
            errorLabel.setText("Inserisci i dati");
        } else {
            errorLabel.setText("Username o Password errati!");
        }
        return null;
    }

    /**
     * @param user
     * @param password
     * @return
     * verifica che nome utente e password corrispondano
     */
    private Dipendente autenticaUtente(String user, String password) {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(dbPathname), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : lines) {
            String[] utente = line.split(",");
            if (user.equals(utente[11]) && password.equals(utente[12])) {
                return new Dipendente(utente[0], utente[1], utente[2], new Comune(utente[3], utente[4], utente[5]),
                        LocalDate.of(Integer.parseInt(utente[7]), Integer.parseInt(utente[8]), Integer.parseInt(utente[9])),
                        utente[10], utente[11], utente[12]);
            }
        }

        return null;
    }


}


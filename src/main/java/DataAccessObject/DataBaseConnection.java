package DataAccessObject;

import javafx.scene.control.Alert;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gère la connexion à la base de données MySQL.
 * Les credentials sont lus depuis db.properties (exclu du dépôt Git).
 */
public class DataBaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    // Bloc statique : exécuté une seule fois au chargement de la classe
    // Charge les credentials depuis db.properties (sur le classpath)
    static {
        Properties props = new Properties();
        try (InputStream is = DataBaseConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (is == null) {
                throw new ExceptionInInitializerError(
                    "Fichier db.properties introuvable.\n" +
                    "Copiez db.properties.example en db.properties et remplissez vos credentials."
                );
            }
            props.load(is);
            URL      = props.getProperty("db.url");
            USER     = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

        } catch (IOException e) {
            throw new ExceptionInInitializerError("Impossible de lire db.properties : " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Connexion à la base de données échouée");
            alert.setContentText(
                "Vérifiez que MySQL est démarré et que les identifiants dans db.properties sont corrects.\n\n" +
                "Détails : " + e.getMessage()
            );
            alert.showAndWait();
            return null;
        }
    }
}

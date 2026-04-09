package DataAccessObject;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DataBaseConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3307/mysql";
    private static final String USER = "root";
    private static final String PASSWORD = "Japha@10";

   public static Connection getConnection(){

        try {
            return DriverManager.getConnection(URL,USER,PASSWORD);
        }catch(SQLException e){
            // Show error dialog
            JOptionPane.showMessageDialog(
                    null,
                    "Erreur de connexion au serveur MySQL.\n" +
                            "Vérifiez que le serveur est démarré et que les identifiants sont corrects.\n\n" +
                            "Détails : " + e.getMessage(),
                    "Erreur de connexion",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return null;
   }

}
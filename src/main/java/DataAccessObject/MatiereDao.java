package DataAccessObject;

import Objects.Matiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la gestion des matières dans la base de données.
 * Fournit les opérations CRUD : insertion, suppression, recherche et liste.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class MatiereDao {

    /**
     * Insère une matière dans la base.
     *
     * @param matiere objet Matiere à insérer
     */
    public void insert(Matiere matiere) {
        String query = "INSERT INTO Matiere(IdMatiere, nom, Description) VALUES (?, ?, ?)";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, matiere.getIdMatiere());
            pst.setString(2, matiere.getNameMatiere());
            pst.setString(3, matiere.getDescription());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Insère une matière via des paramètres simples.
     */
    public void insert(String idMatiere, String nom, String description) {
        String query = "INSERT INTO Matiere VALUES (?, ?, ?)";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, idMatiere);
            pstmt.setString(2, nom);
            pstmt.setString(3, description);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recherche une matière par son identifiant.
     *
     * @param id identifiant de la matière
     * @return la matière trouvée ou null si inexistante
     */
    public Matiere findMatiere(String id) {
        String query = "SELECT * FROM Matiere WHERE IdMatiere = ?";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Matiere(
                        rs.getString("IdMatiere"),
                        rs.getString("nom"),
                        rs.getString("Description")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Retourne la liste complète des matières.
     *
     * @return liste des matières
     */
    public List<Matiere> findAll() {
        List<Matiere> matieres = new ArrayList<>();
        String query = "SELECT * FROM Matiere";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                matieres.add(new Matiere(
                        rs.getString("IdMatiere"),
                        rs.getString("nom"),
                        rs.getString("Description")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return matieres;
    }

    /**
     * Supprime une matière selon son identifiant.
     *
     * @param id identifiant de la matière
     */
    public void delete(String id) {
        String query = "DELETE FROM Matiere WHERE IdMatiere = ?";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
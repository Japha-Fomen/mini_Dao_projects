package DataAccessObject;

import Objects.Enseignant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la gestion des enseignants.
 * Fournit les opérations CRUD : insertion, mise à jour, suppression et recherche.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class EnseignantDao {

    /**
     * Insère un enseignant dans la base.
     */
    public boolean insert(Enseignant enseignant) {
        String sql = "INSERT INTO Enseignant(id, nom, email) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enseignant.getId());
            ps.setString(2, enseignant.getNom());
            ps.setString(3, enseignant.getEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour les informations d'un enseignant.
     */
    public boolean updateEnseignant(Enseignant enseignant) {
        String sql = "UPDATE Enseignant SET nom = ?, email = ? WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, enseignant.getNom());
            ps.setString(2, enseignant.getEmail());
            ps.setInt(3, enseignant.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un enseignant selon son identifiant.
     */
    public boolean deleteEnseignant(int id) {
        String sql = "DELETE FROM Enseignant WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Recherche un enseignant par son identifiant.
     */
    public Enseignant getEnseignantById(int id) {
        String sql = "SELECT * FROM Enseignant WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Enseignant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Retourne la liste complète des enseignants.
     */
    public List<Enseignant> getAllEnseignants() {
        List<Enseignant> list = new ArrayList<>();
        String sql = "SELECT * FROM Enseignant";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Enseignant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
}
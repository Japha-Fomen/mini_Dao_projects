package DataAccessObject;

import Objects.CoursOffert;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la gestion des cours offerts.
 * Permet l'ajout, la mise à jour, la suppression et la récupération des cours.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class CoursOffertDao {

    /**
     * Insère un cours offert dans la base.
     */
    public boolean insert(CoursOffert cours) {
        String sql = "INSERT INTO CoursOffert(idMatiere, idEnseignant, session, annee) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cours.getIdMatiere());
            ps.setInt(2, cours.getIdEnseignant());
            ps.setString(3, cours.getSession());
            ps.setInt(4, cours.getAnnee());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insertion cours offert : " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour un cours offert.
     */
    public boolean updateCours(CoursOffert cours) {
        String sql = "UPDATE CoursOffert SET idMatiere = ?, idEnseignant = ?, session = ?, annee = ? WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cours.getIdMatiere());
            ps.setInt(2, cours.getIdEnseignant());
            ps.setString(3, cours.getSession());
            ps.setInt(4, cours.getAnnee());
            ps.setInt(5, cours.getIdCoursOffert());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur update cours offert : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un cours offert.
     */
    public boolean supprimerCours(int idCoursOffert) {
        String sql = "DELETE FROM CoursOffert WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCoursOffert);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression cours offert : " + e.getMessage());
            return false;
        }
    }

    /**
     * Recherche un cours offert par son identifiant.
     */
    public CoursOffert getCoursById(int id) {
        String sql = "SELECT * FROM CoursOffert WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CoursOffert(
                        rs.getInt("idCoursOffert"),
                        rs.getString("idMatiere"),
                        rs.getInt("idEnseignant"),
                        rs.getString("session"),
                        rs.getInt("annee")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération cours offert : " + e.getMessage());
        }

        return null;
    }

    /**
     * Retourne la liste des cours offerts par un enseignant.
     */
    public List<CoursOffert> getCoursByEnseignant(int idEnseignant) {
        List<CoursOffert> list = new ArrayList<>();
        String sql = "SELECT * FROM CoursOffert WHERE idEnseignant = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idEnseignant);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new CoursOffert(
                        rs.getInt("idCoursOffert"),
                        rs.getString("idMatiere"),
                        rs.getInt("idEnseignant"),
                        rs.getString("session"),
                        rs.getInt("annee")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur liste cours enseignant : " + e.getMessage());
        }

        return list;
    }

    /**
     * Retourne la liste complète des cours offerts.
     */
    public List<CoursOffert> getAllCours() {
        List<CoursOffert> list = new ArrayList<>();
        String sql = "SELECT * FROM CoursOffert";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new CoursOffert(
                        rs.getInt("idCoursOffert"),
                        rs.getString("idMatiere"),
                        rs.getInt("idEnseignant"),
                        rs.getString("session"),
                        rs.getInt("annee")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur liste cours offerts : " + e.getMessage());
        }

        return list;
    }
}
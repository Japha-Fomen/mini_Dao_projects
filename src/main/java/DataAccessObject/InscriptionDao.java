package DataAccessObject;

import Objects.Inscription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la gestion des inscriptions des étudiants aux cours offerts.
 * Permet l'ajout, la suppression, la mise à jour et la récupération des inscriptions.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class InscriptionDao {

    /**
     * Inscrit un étudiant à un cours offert.
     *
     * @param inscription objet Inscription à ajouter
     * @return true si l'inscription a réussi
     */
    public boolean inscrire(Inscription inscription) {
        String sql = "INSERT INTO Inscription(idEtudiant, idCoursOffert, statut, note) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, inscription.getIdEtudiant());
            ps.setInt(2, inscription.getIdCoursOffert());
            ps.setString(3, inscription.getStatut());
            ps.setObject(4, inscription.getNote());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur inscription : " + e.getMessage());
            return false;
        }
    }

    /**
     * Désinscrit un étudiant d'un cours offert.
     *
     * @param idEtudiant identifiant de l'étudiant
     * @param idCoursOffert identifiant du cours offert
     * @return true si la suppression a réussi
     */
    public boolean desinscrire(int idEtudiant, int idCoursOffert) {
        String sql = "DELETE FROM Inscription WHERE idEtudiant = ? AND idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idEtudiant);
            ps.setInt(2, idCoursOffert);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur désinscription : " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère une inscription spécifique.
     *
     * @return l'inscription trouvée ou null si inexistante
     */
    public Inscription getInscription(int idEtudiant, int idCoursOffert) {
        String sql = "SELECT * FROM Inscription WHERE idEtudiant = ? AND idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idEtudiant);
            ps.setInt(2, idCoursOffert);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Inscription(
                        rs.getInt("idEtudiant"),
                        rs.getInt("idCoursOffert"),
                        rs.getString("statut"),
                        rs.getObject("note", Double.class)
                );
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération inscription : " + e.getMessage());
        }

        return null;
    }

    /**
     * Retourne toutes les inscriptions d'un étudiant.
     */
    public List<Inscription> getInscriptionsByEtudiant(int idEtudiant) {
        List<Inscription> list = new ArrayList<>();
        String sql = "SELECT * FROM Inscription WHERE idEtudiant = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idEtudiant);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Inscription(
                        rs.getInt("idEtudiant"),
                        rs.getInt("idCoursOffert"),
                        rs.getString("statut"),
                        rs.getObject("note", Double.class)
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur liste inscriptions étudiant : " + e.getMessage());
        }

        return list;
    }

    /**
     * Retourne toutes les inscriptions d'un cours offert.
     */
    public List<Inscription> getInscriptionsByCours(int idCoursOffert) {
        List<Inscription> list = new ArrayList<>();
        String sql = "SELECT * FROM Inscription WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCoursOffert);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Inscription(
                        rs.getInt("idEtudiant"),
                        rs.getInt("idCoursOffert"),
                        rs.getString("statut"),
                        rs.getObject("note", Double.class)
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur liste inscriptions cours : " + e.getMessage());
        }

        return list;
    }

    /**
     * Met à jour le statut d'une inscription.
     */
    public boolean updateStatut(int idEtudiant, int idCoursOffert, String statut) {
        String sql = "UPDATE Inscription SET statut = ? WHERE idEtudiant = ? AND idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, statut);
            ps.setInt(2, idEtudiant);
            ps.setInt(3, idCoursOffert);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur update statut : " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour la note d'une inscription.
     */
    public boolean updateNote(int idEtudiant, int idCoursOffert, double note) {
        String sql = "UPDATE Inscription SET note = ? WHERE idEtudiant = ? AND idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, note);
            ps.setInt(2, idEtudiant);
            ps.setInt(3, idCoursOffert);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur update note : " + e.getMessage());
            return false;
        }
    }
}
package DataAccessObject;

import Objects.SupportCours;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la gestion des supports de cours.
 * Permet l'ajout, la suppression et la récupération des supports.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class SupportCoursDao {

    /**
     * Ajoute un support de cours.
     */
    public boolean insertSupport(SupportCours support) {
        String sql = "INSERT INTO SupportCours(idCoursOffert, chemin) VALUES (?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, support.getIdCoursOffert());
            ps.setString(2, support.getChemin());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insertion support : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un support selon son identifiant.
     */
    public boolean deleteSupport(int idSupport) {
        String sql = "DELETE FROM SupportCours WHERE idSupport = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSupport);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression support : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime tous les supports associés à un cours offert.
     */
    public boolean supprimerSupportsDuCours(int idCoursOffert) {
        String sql = "DELETE FROM SupportCours WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCoursOffert);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression supports du cours : " + e.getMessage());
            return false;
        }
    }

    /**
     * Retourne la liste des supports d'un cours offert.
     */
    public List<SupportCours> getSupportsByCours(int idCoursOffert) {
        List<SupportCours> list = new ArrayList<>();
        String sql = "SELECT * FROM SupportCours WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCoursOffert);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new SupportCours(
                        rs.getInt("idSupport"),
                        rs.getInt("idCoursOffert"),
                        rs.getString("chemin")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur liste supports : " + e.getMessage());
        }

        return list;
    }
}
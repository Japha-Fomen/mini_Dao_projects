package DataAccessObject;
import Objects.Inscription;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class InscriptionDao {
    public boolean inscrire(Inscription inscription) {
        String sql = "INSERT INTO Inscription(idEtudiant, idCoursOffert, statut) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, inscription.getIdEtudiant());
                ps.setInt(2, inscription.getIdCoursOffert());
                ps.setString(3, inscription.getStatut());

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Inscription getInscription(int idEtudiant, int idCoursOffert) {
        String sql = "SELECT * FROM Inscription WHERE idEtudiant = ? AND idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idEtudiant);
                ps.setInt(2, idCoursOffert);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Inscription(
                            rs.getInt("idEtudiant"),
                            rs.getInt("idCoursOffert"),
                            rs.getString("statut"),
                            rs.getDouble("note")
                    );
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Inscription> getInscriptionsByEtudiant(int idEtudiant) {
        List<Inscription> list = new ArrayList<>();
        String sql = "SELECT * FROM Inscription WHERE idEtudiant = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idEtudiant);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    list.add(new Inscription(
                            rs.getInt("idEtudiant"),
                            rs.getInt("idCoursOffert"),
                            rs.getString("statut"),
                            rs.getDouble("note")
                    ));
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<Inscription> getInscriptionsByCours(int idCoursOffert) {
        List<Inscription> list = new ArrayList<>();
        String sql = "SELECT * FROM Inscription WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idCoursOffert);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    list.add(new Inscription(
                            rs.getInt("idEtudiant"),
                            rs.getInt("idCoursOffert"),
                            rs.getString("statut"),
                            rs.getDouble("note")
                    ));
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public boolean updateStatut(int idEtudiant, int idCoursOffert, String newStatut) {
        String sql = "UPDATE Inscription SET statut = ? WHERE idEtudiant = ? AND idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, newStatut);
                ps.setInt(2, idEtudiant);
                ps.setInt(3, idCoursOffert);

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean updateNote(int idEtudiant, int idCoursOffert, double note) {
        String sql = "UPDATE Inscription SET note = ? WHERE idEtudiant = ? AND idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setDouble(1, note);
                ps.setInt(2, idEtudiant);
                ps.setInt(3, idCoursOffert);

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean desinscrire(int idEtudiant, int idCoursOffert) {
        String sql = "DELETE FROM Inscription WHERE idEtudiant = ? AND idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idEtudiant);
                ps.setInt(2, idCoursOffert);

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
           System.out.println(e.getMessage());
            return false;
        }
    }

}

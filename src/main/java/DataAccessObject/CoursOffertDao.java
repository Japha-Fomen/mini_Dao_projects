package DataAccessObject;
import Objects.CoursOffert;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CoursOffertDao {
    public boolean insert(CoursOffert cours) {
        String sql = "INSERT INTO CoursOffert(idMatiere, idEnseignant, session, annee) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, cours.getIdMatiere());
                ps.setInt(2, cours.getIdEnseignant());
                ps.setString(3, cours.getSession());
                ps.setInt(4, cours.getAnnee());

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            //a changer plutard.
            System.out.println(e.getMessage());
            return false;
        }
    }


    public CoursOffert getCoursById(int idCoursOffert) {
        String sql = "SELECT * FROM CoursOffert WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idCoursOffert);
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

            }
        } catch (SQLException e) {
           System.out.println(e.getMessage());
        }

        return null;
    }

    public List<CoursOffert> getCoursByMatiere(String idMatiere) {
        List<CoursOffert> list = new ArrayList<>();
        String sql = "SELECT * FROM CoursOffert WHERE idMatiere = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, idMatiere);
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

            }
        } catch (SQLException e) {
           System.out.println(e.getMessage());
        }

        return list;
    }

    public List<CoursOffert> getCoursByEnseignant(int idEnseignant) {
        List<CoursOffert> list = new ArrayList<>();
        String sql = "SELECT * FROM CoursOffert WHERE idEnseignant = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

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

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<CoursOffert> getCoursBySession(String session, int annee) {
        List<CoursOffert> list = new ArrayList<>();
        String sql = "SELECT * FROM CoursOffert WHERE session = ? AND annee = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, session);
                ps.setInt(2, annee);
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

            }
        } catch (SQLException e) {
          System.out.println(e.getMessage());
        }

        return list;
    }

    public List<CoursOffert> getAllCours() {
        List<CoursOffert> list = new ArrayList<>();
        String sql = "SELECT * FROM CoursOffert";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(new CoursOffert(
                            rs.getInt("idCoursOffert"),
                            rs.getString("idMatiere"),
                            rs.getInt("idEnseignant"),
                            rs.getString("session"),
                            rs.getInt("annee")
                    ));
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public boolean updateCours(CoursOffert cours) {
        String sql = "UPDATE CoursOffert SET idMatiere = ?, idEnseignant = ?, session = ?, annee = ? WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, cours.getIdMatiere());
                ps.setInt(2, cours.getIdEnseignant());
                ps.setString(3, cours.getSession());
                ps.setInt(4, cours.getAnnee());
                ps.setInt(5, cours.getIdCoursOffert());

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean supprimerCours(int idCoursOffert) {
        String sql = "DELETE FROM CoursOffert WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idCoursOffert);
                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


}

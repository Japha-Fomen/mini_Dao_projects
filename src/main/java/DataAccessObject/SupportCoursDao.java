package DataAccessObject;
import Objects.SupportCours;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupportCoursDao {
    public boolean insertSupport(SupportCours support) {
        String sql = "INSERT INTO SupportCours(idCoursOffert, chemin) VALUES (?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, support.getIdCoursOffert());
                ps.setString(2, support.getChemin());

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
           System.out.println(e.getMessage());
            return false;
        }
    }

    public List<SupportCours> getSupportsByCours(int idCoursOffert) {
        List<SupportCours> list = new ArrayList<>();
        String sql = "SELECT * FROM SupportCours WHERE idCoursOffert = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idCoursOffert);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    list.add(new SupportCours(
                            rs.getInt("idSupport"),
                            rs.getInt("idCoursOffert"),
                            rs.getString("chemin")
                    ));
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public boolean supprimerSupport(int idSupport) {
        String sql = "DELETE FROM SupportCours WHERE idSupport = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idSupport);
                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean supprimerSupportsDuCours(int idCoursOffert) {
        String sql = "DELETE FROM SupportCours WHERE idCoursOffert = ?";

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

import java.sql.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Repository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    // Create Token
    public Model.Token save(String name, String dept) {
        Model.Token token = new Model.Token();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "INSERT INTO token(user_name, department, issue_time, status) VALUES (?, ?, NOW(), 'WAITING')";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setString(2, dept);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                token.id = rs.getLong(1);
            }

            token.userName = name;
            token.department = dept;
            token.status = "WAITING";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

    // Get Token
    public Model.Token findById(Long id) {
        Model.Token token = null;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "SELECT * FROM token WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                token = new Model.Token();
                token.id = rs.getLong("id");
                token.userName = rs.getString("user_name");
                token.department = rs.getString("department");
                token.status = rs.getString("status");

                Timestamp ts = rs.getTimestamp("issue_time");
                if (ts != null) token.issueTime = ts.toLocalDateTime();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

    // Get Queue
    public List<Model.Token> getQueue(String dept) {
        List<Model.Token> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "SELECT * FROM token WHERE department=? AND status='WAITING' ORDER BY issue_time";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dept);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Model.Token t = new Model.Token();
                t.id = rs.getLong("id");
                t.userName = rs.getString("user_name");
                t.department = rs.getString("department");
                t.status = rs.getString("status");

                Timestamp ts = rs.getTimestamp("issue_time");
                if (ts != null) t.issueTime = ts.toLocalDateTime();

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Update status
    public void updateStatus(Long id, String status) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "UPDATE token SET status=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, status);
            ps.setLong(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
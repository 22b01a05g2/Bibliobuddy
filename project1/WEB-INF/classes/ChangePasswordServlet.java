import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/minip?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Svecw@2022";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");

        // JDBC variables
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Connect to database
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // SQL query to update the password
            String sql = "UPDATE userd SET password = ? WHERE emailid = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Password changed successfully
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><h3>Password changed successfully. You can now <a href='login.html'>login</a>.</h3></body></html>");
            } else {
                // Email not found
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><h3>Email not found. Please check and try again.</h3></body></html>");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle database connection and query errors
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body><h3>Database error. Please try again later.</h3></body></html>");

        } finally {
            // Close JDBC objects
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
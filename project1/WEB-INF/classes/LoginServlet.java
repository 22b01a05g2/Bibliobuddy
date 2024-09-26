import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/minip?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Svecw@2022";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // JDBC variables
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Connect to database
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // SQL query to check user credentials
            String sql = "SELECT * FROM userd WHERE emailid = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            // Execute the query
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Login successful, set emailid as session attribute
                HttpSession session = request.getSession();
                session.setAttribute("emi", email);

                // Navigate to welcome page
                response.sendRedirect("welcome.html"); // Replace with your welcome page URL
            } else {
                // Login failed
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><h3>Invalid email or password. Please try again.</h3></body></html>");
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
                if (rs != null) {
                    rs.close();
                }
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

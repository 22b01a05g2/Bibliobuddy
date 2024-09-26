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

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/minip?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Svecw@2022";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        // JDBC variables
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Connect to database
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // SQL query to insert data into userd table
            String sql = "INSERT INTO userd (name, emailid, password, phone_number) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, phone);

            // Execute the query
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // Registration successful
                response.sendRedirect("login.html"); // Redirect to login page
            } else {
                // Registration failed
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><h3>Registration failed. Please try again.</h3></body></html>");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle database connection and insertion errors
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

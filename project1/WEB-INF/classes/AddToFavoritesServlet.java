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
import javax.servlet.http.HttpSession;

@WebServlet("/AddToFavoritesServlet")
public class AddToFavoritesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/minip?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Svecw@2022";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve emailid and book name from request parameters
        HttpSession session = request.getSession();
        String emailid = (String) session.getAttribute("emi");
        String bookName = request.getParameter("bookName"); // Retrieve dynamically

        // JDBC variables
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Connect to database
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // SQL query to insert into favorite_books table
            String sql = "INSERT INTO favorite_books (emailid, book_name) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, emailid); // Emailid from session
            stmt.setString(2, bookName); // Book name dynamically retrieved

            // Execute the insert query
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // Insert successful
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><h3>Book added to favorites successfully.</h3></body></html>");
            } else {
                // Insert failed
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><h3>Failed to add book to favorites. Please try again.</h3></body></html>");
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

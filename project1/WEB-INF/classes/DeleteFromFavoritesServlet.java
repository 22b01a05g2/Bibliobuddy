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

@WebServlet("/DeleteFromFavoritesServlet")
public class DeleteFromFavoritesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/minip?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Svecw@2022";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve emailid and book name from session and request parameters
        HttpSession session = request.getSession();
        String emailid = (String) session.getAttribute("emi");
        String bookName = request.getParameter("bookName"); // Retrieve bookName parameter

        // JDBC variables
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Connect to database
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Delete the book from favorite_books table
            String deleteSql = "DELETE FROM favorite_books WHERE emailid = ? AND book_name = ?";
            stmt = conn.prepareStatement(deleteSql);
            stmt.setString(1, emailid);
            stmt.setString(2, bookName);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                // Deletion successful
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><h3>Book deleted from favorites successfully.</h3></body></html>");
            } else {
                // Deletion failed
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body><h3>Failed to delete book from favorites. Please try again.</h3></body></html>");
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

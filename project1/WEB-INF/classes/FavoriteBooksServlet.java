import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FavoriteBooksServlet")
public class FavoriteBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("emi") == null) {
            response.sendRedirect("login.html"); // Redirect to login if not logged in
            return;
        }

        String email = (String) session.getAttribute("emi");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/minip", "root", "Svecw@2022");

            // Query to select favorite books for the user
            String sql = "SELECT book_name FROM favorite_books WHERE emailid = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            rs = pst.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<link href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<title>Favorite Books</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container mt-5'>");
            out.println("<h1 class='mb-4'>Favorite Books</h1>");
            out.println("<ul class='list-group'>");

            // Display the list of favorite books
            while (rs.next()) {
                String bookName = rs.getString("book_name");
                out.println("<li class='list-group-item'>" + bookName + "</li>");
            }

            out.println("</ul>");
            out.println("</div>");
            out.println("<script src='https://code.jquery.com/jquery-3.5.1.slim.min.js'></script>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js'></script>");
            out.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace(out);
            }
        }
    }
}

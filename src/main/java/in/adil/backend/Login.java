package in.adil.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/loginForm")
public class Login extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		String email = req.getParameter("email1");
		String password = req.getParameter("pass1");

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/servlet_demo", "root", "root");
			PreparedStatement ps = conn.prepareStatement("select * from register where email=? and password=?");
			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				HttpSession session = req.getSession();
				session.setAttribute("session_name", resultSet.getString("name"));
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("/profile.jsp");
				requestDispatcher.include(req, resp);

			} else {
				out.print("<h3 style='color:red'>Email id and password did not matched..<h3/>");

				RequestDispatcher requestDispatcher = req.getRequestDispatcher("/login.jsp");
				requestDispatcher.include(req, resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			out.print("<h3 style='color:red'>" + e.getMessage() + "<h3/>");
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/login.jsp");
			requestDispatcher.include(req, resp);
		}

	}
}

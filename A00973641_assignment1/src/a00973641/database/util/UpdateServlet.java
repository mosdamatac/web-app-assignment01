package a00973641.database.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import a00973641.database.DBConnectionManager;
import a00973641.database.DbConstants;
import a00973641.util.ServletUtilities;

/**
 * Servlet implementation class UpdateServlet
 */
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Pattern VALID_PHONE = Pattern.compile("^\\d{3}-\\d{3}-\\d{4}$");
	private static final Pattern VALID_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String address = request.getParameter("address");
		String city = request.getParameter("city");
		String code = request.getParameter("code");
		String country = request.getParameter("country");
		String phoneNumber = request.getParameter("phoneNumber");
		String email = request.getParameter("email");

		StringBuffer errorMsg = new StringBuffer();

		if (firstName == null || firstName.trim().equals("")) {
			errorMsg.append("First name can't be null or empty;\n");
		}
		if (lastName == null || lastName.trim().equals("")) {
			errorMsg.append("Last name can't be null or empty;\n");
		}
		if (address == null || address.trim().equals("")) {
			errorMsg.append("Address can't be null or empty;\n");
		}
		if (city == null || city.trim().equals("")) {
			errorMsg.append("City can't be null or empty;\n");
		}
		if (code == null || code.trim().equals("")) {
			errorMsg.append("Code can't be null or empty;\n");
		}
		if (country == null || country.trim().equals("")) {
			errorMsg.append("Country can't be null or empty;\n");
		}
		if (phoneNumber == null || !ServletUtilities.isValid(phoneNumber.trim(), VALID_PHONE)) {
			errorMsg.append("Invalid phone number (e.g. 111-111-1234);\n");
		}
		if (email == null || !ServletUtilities.isValid(email.trim(), VALID_EMAIL)) {
			errorMsg.append("Invalid email (e.g. me@organization.com);\n");
		}

		if (errorMsg.toString().trim().length() != 0) {
			// response.sendError(HttpServletResponse.SC_BAD_REQUEST,
			// errorMsg.toString());
			System.out.println(errorMsg);
		} else {
			DBConnectionManager db = DBConnectionManager.getInstance();
			Connection dbConn = null;
			PreparedStatement ps = null;
			String updateSQL = String.format("UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?",
					DbConstants.MEMBER_TABLE_NAME, "firstName", "lastName", "Address", "City", "Code", "Country",
					"PhoneNumber", "EMail", "MemberID");
			try {
				System.out.println("Attempting to update data...");
				dbConn = db.getConnection();
				ps = dbConn.prepareStatement(updateSQL);

				ps.setString(1, request.getParameter("firstName"));
				ps.setString(2, request.getParameter("lastName"));
				ps.setString(3, request.getParameter("address"));
				ps.setString(4, request.getParameter("city"));
				ps.setString(5, request.getParameter("code"));
				ps.setString(6, request.getParameter("country"));
				ps.setString(7, request.getParameter("phoneNumber"));
				ps.setString(8, request.getParameter("email"));
				ps.setInt(9, Integer.parseInt(request.getParameter("memberID")));

				System.out.println("Executing: " + updateSQL);
				int count = ps.executeUpdate();
				System.out.println("Successfully updated row: " + count);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
				rd.forward(request, response);
			} catch (SQLException e) {
				// TODO error
				System.out.println(e.getMessage());
			} finally {
				DBUtil.closeStatement(ps);
				db.shutdown();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

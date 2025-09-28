package com.onlinevoting.servlets;


import com.onlinevoting.utils.DBConnection;


import javax.servlet.*;
import java.io.*;
import java.sql.*;



@WebServlet(name = "VoteHandler", urlPatterns = {"/VoteHandler"})
public class VoteHandler extends HttpServlet {


@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
request.setCharacterEncoding("UTF-8");
response.setContentType("application/json;charset=UTF-8");


String voterId = request.getParameter("voter_id");
String candidate = request.getParameter("candidate");


try (PrintWriter out = response.getWriter()) {
if (voterId == null || voterId.trim().isEmpty() || candidate == null || candidate.trim().isEmpty()) {
response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
out.print("{\"status\":\"error\",\"message\":\"Missing voter_id or candidate\"}");
return;
}


try (Connection conn = DBConnection.getConnection()) {
// 1) Confirm voter exists (optional but recommended)
String checkVoterSql = "SELECT has_voted FROM voters WHERE voter_id = ?";
try (PreparedStatement ps = conn.prepareStatement(checkVoterSql)) {
ps.setString(1, voterId);
try (ResultSet rs = ps.executeQuery()) {
if (!rs.next()) {
// Voter ID not registered
response.setStatus(HttpServletResponse.SC_FORBIDDEN);
out.print("{\"status\":\"error\",\"message\":\"Voter ID not registered\"}");
return;
}


boolean hasVoted = rs.getInt("has_voted") == 1;
if (hasVoted) {
// Already voted
response.setStatus(HttpServletResponse.SC_CONFLICT);
out.print("{\"status\":\"error\",\"message\":\"You have already voted\"}");
return;
}
}
}


// 2) Insert vote (transactional approach)
try {
conn.setAutoCommit(false);


String insertVoteSql = "INSERT INTO votes (voter_id, candidate) VALUES (?, ?)";
try (PreparedStatement insertPs = conn.prepareStatement(insertVoteSql)) {
insertPs.setString(1, voterId);
insertPs.setString(2, candidate);
insertPs.executeUpdate();
}


String updateVoterSql = "UPDATE voters SET has_voted = 1 WHERE voter_id = ?";
try (PreparedStatement updatePs = conn.prepareStatement(updateVoterSql)) {
}
    }
}

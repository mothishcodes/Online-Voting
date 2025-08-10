import java.io.*;
import java.net.*;
import java.sql.*;

public class VoteHandler {

    static final String DB_URL = "jdbc:mysql://localhost:3306/voting_app";
    static final String DB_USER = "root";  // change as needed
    static final String DB_PASS = "password"; // change as needed

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server running on port 5000...");

     
            Class.forName("com.mysql.cj.jdbc.Driver");

            while (true) {
                Socket socket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String line;
                StringBuilder body = new StringBuilder();
                boolean isPost = false;

                
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    if (line.startsWith("POST")) {
                        isPost = true;
                    }
                }

                if (isPost) {
                   
                    char[] buffer = new char[1024];
                    int read = in.read(buffer);
                    body.append(buffer, 0, read);

                    
                    String[] parts = body.toString().split("\"");
                    String voter = parts[3];
                    String candidate = parts[7];

                    String result;

                   
                    if (hasVoted(voter)) {
                        result = "❌ You have already voted.";
                    } else {
                        saveVote(voter, candidate);
                        result = "✅ Your vote for " + candidate + " has been recorded.";
                    }

                   
                    out.write("HTTP/1.1 200 OK\r\n");
                    out.write("Content-Type: text/plain\r\n");
                    out.write("Access-Control-Allow-Origin: *\r\n");
                    out.write("\r\n");
                    out.write(result);
                    out.flush();
                }

                in.close();
                out.close();
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private static boolean hasVoted(String voter) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT COUNT(*) FROM votes WHERE LOWER(voter) = LOWER(?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, voter);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static void saveVote(String voter, String candidate) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "INSERT INTO votes (voter, candidate) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, voter);
            ps.setString(2, candidate);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

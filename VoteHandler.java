import java.io.*;
import java.net.*;
import java.util.*;

public class VoteHandler {
    static Set<String> voters = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server running on port 5000...");

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
                    if (voters.contains(voter.toLowerCase())) {
                        result = "❌ You have already voted.";
                    } else {
                        saveVote(voter, candidate);
                        voters.add(voter.toLowerCase());
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveVote(String voter, String candidate) {
        try (FileWriter fw = new FileWriter("votes.txt", true)) {
            fw.write("Voter: " + voter + ", Voted for: " + candidate + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

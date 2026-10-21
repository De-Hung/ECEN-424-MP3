import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class NaiveClient {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java NaiveClient <server ip> <server port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(host, port);
             InputStream in = socket.getInputStream()) {

            int b;
            while ((b = in.read()) != -1) {
                System.out.print((char) b);
                System.out.flush();
                if ((char) b == '\n') {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("NaiveClient error: " + e.getMessage());
        }
    }
}
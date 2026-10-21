import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class BufferClient {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java BufferClient <server ip> <server port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        StringBuilder buffer = new StringBuilder();

        try (Socket socket = new Socket(host, port);
             InputStream in = socket.getInputStream()) {

            int b;
            while ((b = in.read()) != -1) {
                buffer.append((char) b);
                if ((char) b == '\n') {
                    System.out.print(buffer.toString());
                    System.out.flush();
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("BufferClient error: " + e.getMessage());
        }
    }
}
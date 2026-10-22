import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static String str;
    private static int n;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Server <server port> <max. clients>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        int maxClients = Integer.parseInt(args[1]);

        // Prompt user for str and N upon initialization
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter output string (str): ");
        str = scanner.nextLine();

        System.out.print("Enter positive integer for number of outputs (N): ");
        n = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Server started. Port: " + port + " Max Concurrent Clients: " + maxClients);

        // Thread pool to handle up to maxClients simultaneously
        ExecutorService threadPool = Executors.newFixedThreadPool(maxClients);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket, str, n));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final String message;
        private final int repetitions;

        public ClientHandler(Socket socket, String message, int repetitions) {
            this.socket = socket;
            this.message = message;
            this.repetitions = repetitions;
        }

        @Override
        public void run() {
            try (Socket s = this.socket;
                 OutputStream out = s.getOutputStream()) {

                for (int i = 0; i < repetitions; i++) {
                    String payload = message;
                    if (i == repetitions - 1) {
                        payload += "\n";
                    }

                    byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
                    out.write(bytes);
                    out.flush();

                    if (i < repetitions - 1) {
                        Thread.sleep(1000);
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }
    }
}
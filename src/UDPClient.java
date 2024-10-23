import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.Console;

public class UDPClient {
    private static final int DEFAULT_PORT = 8080;
    private String serverAddress;
    private int port;

    public UDPClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void sendMessage(String message) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = message.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverAddress), port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int portToUse = DEFAULT_PORT;

        if (args.length > 0) {
            serverAddress = args[0];
        }
        if (args.length > 1) {
            portToUse = Integer.parseInt(args[1]);
        }

        UDPClient client = new UDPClient(serverAddress, portToUse);
        Console console = System.console();

        if (console == null) {
            System.err.println("Aucune console disponible.");
            return;
        }

        while (true) {
            String message = console.readLine("Entrez un message à envoyer (ou 'exit' pour quitter) : ");
            if ("exit".equalsIgnoreCase(message)) {
                break;
            }
            client.sendMessage(message);
        }
    }
}

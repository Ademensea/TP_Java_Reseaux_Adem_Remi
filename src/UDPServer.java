import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    private static final int DEFAULT_PORT = 8080; // Port par défaut
    private int port;

    // Constructeur avec port
    public UDPServer(int port) {
        this.port = port;
    }

    // Constructeur par défaut
    public UDPServer() {
        this(DEFAULT_PORT);
    }

    // Lancer le serveur
    public void launch() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Serveur UDP en écoute sur le port " + port);

            byte[] buffer = new byte[1024]; // Buffer de réception

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // Recevoir un datagramme

                String message = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                System.out.println("Reçu de " + packet.getAddress() + ":" + packet.getPort() + ": " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode toString pour décrire l'état du serveur
    @Override
    public String toString() {
        return "UDPServer en écoute sur le port " + port;
    }

    // Méthode principale
    public static void main(String[] args) {
        int portToUse = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        UDPServer server = new UDPServer(portToUse);
        server.launch();
    }
}


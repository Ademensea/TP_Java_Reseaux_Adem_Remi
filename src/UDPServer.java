
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.IOException;

public class UDPServer {
    private int port;

    public UDPServer() {
        this.port = 8080; // port par défaut
    }

    public UDPServer(int port) {
        this.port = port;
    }

    public void launch() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Le serveur UDP est en cours d'exécution sur le port " + port);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String clientAddress = packet.getAddress().toString();
                int clientPort = packet.getPort();

                String received = new String(packet.getData(), 0, packet.getLength(), "UTF-8");

                System.out.println("Reçu de " + clientAddress + ":" + clientPort + " - " + received);
            }
        } catch (SocketException e) {
            System.err.println("Erreur de socket : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur IO : " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Serveur UDP à l'écoute sur le port " + port;
    }

    public static void main(String[] args) {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : 8080;
        UDPServer server = new UDPServer(port);
        System.out.println(server.toString());
        server.launch();
    }
}



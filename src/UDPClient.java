import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.Console;
import java.io.IOException;

public class UDPClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Utilisation : java UDPClient <nom d'hôte> <port>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(hostname);
            Console console = System.console();

            if (console == null) {
                System.out.println("Pas de console disponible");
                return;
            }

            while (true) {
                String message = console.readLine("Entrez un message : ");
                byte[] buffer = message.getBytes("UTF-8");

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
                socket.send(packet);

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Client arrêté...");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur IO : " + e.getMessage());
        }
    }
}


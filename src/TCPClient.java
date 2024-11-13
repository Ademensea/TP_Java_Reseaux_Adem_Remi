
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;




public class TCPClient {

    public TCPClient() {
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Utilisation : java TCPClient <nom d'hôte> <port>");
        } else {
            String host = args[0];  // L'adresse du serveur
            int port = Integer.parseInt(args[1]);  // Le port du serveur

            try {
                // Se connecter au serveur via TCP
                Socket socket = new Socket(host, port);

                // Flux de lecture et d'écriture
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  // Flux de sortie pour envoyer des messages
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Flux d'entrée pour lire les réponses

                System.out.println("Connecté au serveur " + host + " sur le port " + port);

                while (true) {
                    // Lire un message de l'utilisateur
                    String userMessage = userInput.readLine();
                    if (userMessage == null || userMessage.equalsIgnoreCase("exit")) {
                        System.out.println("Client arrêté...");
                        break;
                    }

                    // Envoyer le message au serveur
                    out.println(userMessage);

                    // Lire la réponse du serveur
                    String serverResponse = in.readLine();
                    System.out.println("Réponse du serveur : " + serverResponse);
                }

                // Fermer la connexion
                socket.close();
            } catch (IOException e) {
                System.err.println("Erreur IO : " + e.getMessage());
            }
        }
    }
}


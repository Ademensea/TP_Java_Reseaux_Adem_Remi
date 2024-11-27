package com.projet.reseau.tcp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionThread extends Thread {
    private final Socket clientSocket;

    public ConnectionThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String clientAddress = clientSocket.getInetAddress().getHostAddress();

        try (
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true)
        ) {
            System.out.println("Connected to client: " + clientAddress);

            String receivedMessage;
            while ((receivedMessage = reader.readLine()) != null) {
                System.out.println("Message from " + clientAddress + ": " + receivedMessage);

                // Respond to the client with an echo prefixed by their IP address
                String response = clientAddress + ": " + receivedMessage;
                writer.println(response);

                // Close connection if the client sends "exit"
                if ("exit".equalsIgnoreCase(receivedMessage)) {
                    System.out.println("Client " + clientAddress + " disconnected.");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Error with client " + clientAddress + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }
}


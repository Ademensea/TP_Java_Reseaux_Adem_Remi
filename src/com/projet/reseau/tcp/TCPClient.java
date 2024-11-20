package com.projet.reseau.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient {
    private static final String EXIT_COMMAND = "exit";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TCPClient <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(host, port);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("Connected to server. Type 'exit' to disconnect.");

            String message;
            while ((message = console.readLine()) != null) {
                out.println(message);
                System.out.println("Server response: " + in.readLine());

                if (message.equalsIgnoreCase(EXIT_COMMAND)) {
                    System.out.println("Client terminated.");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error in TCPClient: " + e.getMessage());
        }
    }
}


package com.projet.reseau.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPMultiServer {
    private static final int DEFAULT_PORT = 8080; // Port par défaut
    private static final int THREAD_POOL_SIZE = 10; // Taille du pool de threads
    private final int port;

    public TCPMultiServer(int port) {
        this.port = port;
    }

    public TCPMultiServer() {
        this(DEFAULT_PORT);
    }

    public void launch() {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        System.out.println("TCP Multi-Client Server running on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                threadPool.execute(new ConnectionHandler(clientSocket));
            }
        } catch (Exception e) {
            System.err.println("Error in TCPMultiServer: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        TCPMultiServer server = new TCPMultiServer(port);
        server.launch();
    }
}

class ConnectionHandler implements Runnable {
    private static final String EXIT_COMMAND = "exit";
    private final Socket clientSocket;

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String clientAddress = clientSocket.getInetAddress().toString();
            String message;

            while ((message = in.readLine()) != null) {
                System.out.println("Message from " + clientAddress + ": " + message);
                out.println(clientAddress + " : " + message);

                if (message.equalsIgnoreCase(EXIT_COMMAND)) {
                    System.out.println("Client " + clientAddress + " disconnected.");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error in ConnectionHandler: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}


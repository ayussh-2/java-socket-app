package com.ayush.socket;

import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;

public class SocketManager {
    private Socket socket;

    public SocketManager(String serverUrl) {
        try {
            socket = IO.socket(serverUrl);

            Log.d("chaatu",serverUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            socket = null; // Set socket to null if there's an error
        }
    }

    public void connect() {
        if (socket != null) {
            socket.connect();
        } else {
            System.err.println("Socket is not initialized. Unable to connect.");
        }
    }

    public void disconnect() {
        if (socket != null) {
            socket.disconnect();
        } else {
            System.err.println("Socket is not initialized. Unable to disconnect.");
        }
    }

    public boolean isConnected() {
        return socket != null && socket.connected();
    }

    public Socket getSocket() {
        return socket;
    }
}

package com.ayush.socket;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private SocketManager socketManager;
    private Socket socket;

    private EditText inputMessage;
    private Button sendButton;
    private TextView chatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);
        chatWindow = findViewById(R.id.chatWindow);

        // Initialize SocketManager with your server URL
        socketManager = new SocketManager("http://192.168.0.120:5000/");
        // Get the socket instance
        socket = socketManager.getSocket();



        if (socket != null) {
            // Set up event listeners after ensuring socket is initialized
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on("chat message", onNewMessage);

            // Connect the socket
            socket.connect();


        } else {
            // Handle the case where the socket is not initialized
            chatWindow.append("Socket connection failed.\n");
        }

        // Send a message when the send button is clicked
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
                if (socket != null && socket.connected()) {
                    if(!message.isEmpty()){
                        socket.emit("chat message", message);
                        inputMessage.setText("");
                    }else{
                        Log.d("chaatu","Message cannot be empty");
                    }

                } else {
                    chatWindow.append("Not connected to server kaise karu.\n");
                }
            }
        });
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatWindow.append("Connected to server.\n");
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    chatWindow.append(message + "\n");
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketManager.disconnect();
    }
}

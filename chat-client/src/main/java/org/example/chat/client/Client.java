package org.example.chat.client;

import java.io.*;
import java.net.Socket;

public class Client {

    private final Socket socket;
    private final String name;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket, String userName) {
        this.socket = socket;
        name = userName;
        try{
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        try {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

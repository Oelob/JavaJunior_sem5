package org.example.chat.server;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientManager  implements Runnable{
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;


    public static ArrayList<ClientManager> clients = new ArrayList<>();

    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            name = bufferedReader.readLine();
            clients.add(this);
            System.out.println(name + " подключился к чату");
            broadcastMessage("Server: " + name + " подключился к чату") ;

        }catch (IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }


    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                if(messageFromClient == null){
                    closeEverything(socket, bufferedWriter, bufferedReader);
                    break;
                }
                sendMsg(messageFromClient);
            }catch (IOException e){
                closeEverything(socket, bufferedWriter, bufferedReader);
                break;
            }
        }
    }
    private void sendMsg(String msg){
        StringBuilder nameClient = new StringBuilder();
        String[] arrayMsg = msg.split(" ");
        String nameUser = arrayMsg[1];
        String[] arrayNameUser = nameUser.split("");

        if(arrayNameUser[0].equals("@")){
            for (int i = 1; i < arrayNameUser.length; i++) {
                nameClient.append(arrayNameUser[i]);
            }
            StringBuilder newMsg = new StringBuilder(arrayMsg[0] + " ");
            for (int i = 2; i < arrayMsg.length; i++) {
                newMsg.append(arrayMsg[i]);
                newMsg.append(" ");
            }
                for (ClientManager client:clients) {
                    try {
                        if (nameClient.toString().equals(client.name)){
                            client.bufferedWriter.write(newMsg.toString());
                            client.bufferedWriter.newLine();
                            client.bufferedWriter.flush();
                        }
                    }catch (IOException e){
                        closeEverything(socket, bufferedWriter, bufferedReader);
                    }
                }
        }else{
            broadcastMessage(msg);
        }
    }
    private void broadcastMessage(String message){
        for (ClientManager client : clients) {
            try {
                if(!client.name.equals(name)){
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket, bufferedWriter, bufferedReader);
            }

        }
    }

    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        removeClient();
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

    private void removeClient(){
        clients.remove(this);
        System.out.println(name + "покинул чат");
        broadcastMessage("Server: " + name + " покинул чат");
    }

}

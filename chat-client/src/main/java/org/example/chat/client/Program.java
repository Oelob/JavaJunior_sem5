package org.example.chat.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Введите ваше имя: ");
            String userName = scan.nextLine();
            Socket socket = new Socket("localhost", 1400);
            Client client = new Client(socket, userName);
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println("InetAdress: "+inetAddress);
            String remoteIP = inetAddress.getHostAddress();
            System.out.println("Remote IP: "+remoteIP);
            System.out.println("Local Port: "+socket.getLocalPort());

            client.listenForMessage();
            client.sendMessage();

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

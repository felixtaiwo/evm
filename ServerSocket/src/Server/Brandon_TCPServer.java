package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Brandon_TCPServer {
   public static void main(String[] args) throws IOException {
       System.out.println("server is up and running.....");
       final int port = 12337;
       ServerSocket serverSocket = new ServerSocket(port);
       Socket socket = serverSocket.accept();
       InputStreamReader in = new InputStreamReader(socket.getInputStream());
       BufferedReader bf = new BufferedReader(in);
       String W = bf.readLine();
       serverSocket.close();
       in.close();
       bf.close();
       Socket socket1 = new Socket(W, 80);
       PrintWriter wtr = new PrintWriter(socket1.getOutputStream());
       wtr.println("GET / HTTP/1.1");
       wtr.println("Host:" +W);
       wtr.println("\n");
       wtr.flush();
       InputStreamReader in1 = new InputStreamReader(socket1.getInputStream());
       BufferedReader buf = new BufferedReader(in1);
       String line = buf.readLine();
       while (line != null) {
           System.out.println(line);
           line = buf.readLine();
       }
       socket1.close();
       in1.close();
       buf.close();
   }
}

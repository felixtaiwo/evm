package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Brandon_TCPClient {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Client Application up and running.....");
        System.out.println("Enter your web server name");
        String W = scanner.next();
        System.out.println("Enter 1 or 2");
        int a = scanner.nextInt();
        switch (a) {
            case 1 :
                Socket socket = new Socket(W, 80);
                PrintWriter wtr = new PrintWriter(socket.getOutputStream());
                wtr.println("GET / HTTP/1.1");
                wtr.println("Host:" +W);
                wtr.println("\n");
                wtr.flush();
                InputStreamReader in = new InputStreamReader(socket.getInputStream());
                BufferedReader buf = new BufferedReader(in);
                String line = buf.readLine();
                while(line!=null){
                    System.out.println(line);
                    line = buf.readLine();
                }
                socket.close();
                wtr.close();
                in.close();
                buf.close();
                break;
            case 2:
                socket = new Socket("127.0.0.1", 12337);
                PrintWriter wtr1 = new PrintWriter(socket.getOutputStream());
                wtr1.println(W);
                wtr1.flush();
                socket.close();
                wtr1.close();
                break;
            default:
                System.out.println("invalid entry");
        }
    }
}

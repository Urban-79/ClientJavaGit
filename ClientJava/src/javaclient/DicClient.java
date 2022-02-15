package javaclient;

import java.util.Scanner;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

public class DicClient {

    public final static String SERVER = "127.0.0.1";
    public final static int PORT = 5000;
    public final static int TIMEOUT = 30000;

    public static void main(String[] args) throws UnknownHostException {

        SocketAddress addr = new InetSocketAddress(InetAddress.getByName(SERVER), PORT);
        Scanner sc =new Scanner(System.in);

        for(;;) {
            System.out.print("Key/Text (Q to quit) : ");
            String content = sc.next();

            if(content.equalsIgnoreCase("q")) {
                break;
            }
            String[] items = content.split("/");

            if(items.length>1) {
                try {
                    request(addr, "POST", items[0], items[1]);
                    request(addr, "GET", "", "");
                }
                catch(IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        sc.close();
    }

    public static void request(SocketAddress addr, String verb, String url, String content) throws IOException
    {
        Socket socket = new Socket();

        socket.connect(addr, TIMEOUT);

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

        pw.printf("%s /%s HTTP/1.1\r\n", verb, url);
        pw.printf("Content-Type: text/plain\r\n");
        pw.printf("Content-Length: %d\r\n\r\n", content.length());
        pw.printf("%s\r\n", content);
        pw.flush();

        String tmp;

        while((tmp=br.readLine())!=null){
            System.out.println(tmp);
        }
        pw.close();
        br.close();
        socket.close();
    }
}

package xcf.practice.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer启动类
 */
public class StartServer {

    public static void main(String[] args) {
        StartServer startServer = new StartServer();
        startServer.start();
    }

    private ServerSocket serverSocket;

    private ExecutorService executorService;

    public StartServer() {
        try {
            System.out.println("正在启动服务端、、、");
            serverSocket = new ServerSocket(8080);
            executorService = Executors.newFixedThreadPool(100);
            System.out.println("启动服务端完毕、、、");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            while (true){
                System.out.println("等待客户端连接、、、");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端连接了、、、");
                ClientHandler handler = new ClientHandler(socket);
                executorService.execute(handler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

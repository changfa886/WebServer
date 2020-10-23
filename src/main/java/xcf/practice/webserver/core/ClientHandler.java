package xcf.practice.webserver.core;

import xcf.practice.webserver.exception.EmptyRequestException;
import xcf.practice.webserver.http.HttpRequest;
import xcf.practice.webserver.http.HttpResponse;
import xcf.practice.webserver.servlet.HttpServlet;
import xcf.practice.webserver.servlet.LoginServlet;
import xcf.practice.webserver.servlet.RegServlet;
import xcf.practice.webserver.servlet.ShowAllUserServlet;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * 客户端处理器，负责处理与客户端的交互工作
 */
public class ClientHandler implements Runnable {

    private Socket socket;

    //实例化ClientHandler时给socket赋值
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1、解析请求
            HttpRequest httpRequest = new HttpRequest(socket);
            HttpResponse httpResponse = new HttpResponse(socket);
            //2、处理请求
            //2.1、根据Request获取请求的抽象路径
            String path = httpRequest.getRequestURL();
            //2.2根据请求路径判断是否为一个业务操作
            HttpServlet servlet = ServerContent.getHttpServlet(path);
            if (servlet!=null){
                servlet.service(httpRequest,httpResponse);
            }else{
                //2.3、根据请求的抽象路径去webapp下寻找对应的资源
                File file = new File("src/main/webapp"+path);
                if (file.exists()){
                    System.out.println("资源已找到、、、");
                    httpResponse.setEntity(file);
                }else {
                    System.out.println("资源未找到、、、");
                    file = new File("src/main/webapp/404.html");
                    httpResponse.setEntity(file);
                    httpResponse.setStateCode(404);
                    httpResponse.setStateReason("NOT FOUND");
                }
            }
            //响应请求
            httpResponse.flush();
        } catch (EmptyRequestException e){
            System.out.println("空请求、、、");
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            //处理完请求和响应客户端后断开其连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package xcf.practice.webserver.servlet;

import xcf.practice.webserver.http.HttpRequest;
import xcf.practice.webserver.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * 处理登陆业务
 */
public class LoginServlet extends HttpServlet {
    public void service (HttpRequest request, HttpResponse response){
        System.out.println("开始处理登陆业务、、、");
        try (RandomAccessFile file = new RandomAccessFile("user.dat","r")){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            for (int i = 0; i < file.length() / 100; i++) {
                file.seek(i*100);
                byte[] data = new byte[32];
                file.read(data);
                String name = new String(data,"UTF-8").trim();
                if (username.equals(name)){
                    file.read(data);
                    String word = new String(data,"UTF-8").trim();
                    if (password.equals(word)){
                        forward("login_success.html",response);
                        System.out.println("登陆成功！");
                        return;
                    }
                    /**
                     * 当用户名正确后无论密码正确还是错误，都应停止遍历读取数据
                     * 减少没必要的读取，提高性能
                     */
                    break;
                }
            }
            //用户名或密码错误时响应登陆失败页面
            forward("login_error.html",response);
            System.out.println("登陆失败！");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("处理登陆业务完毕、、、");
    }
}

package xcf.practice.webserver.servlet;

import xcf.practice.webserver.http.HttpRequest;
import xcf.practice.webserver.http.HttpResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * 处理用户注册业务
 */
public class RegServlet extends HttpServlet {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("开始处理注册业务、、、");
        try (RandomAccessFile file = new RandomAccessFile("user.dat","rw")){
            //1.通过request获取用户注册信息
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            int age = Integer.parseInt(request.getParameter("age"));
            String nickname = request.getParameter("nickname");
            //2.将注册信息写入文件
            /**
             * 先判断用户名是否已存在，若已存在则响应用户已存在页面
             * 不存在才将用户信息写入文件
             */
            for (int i = 0; i < file.length() / 100; i++) {
                file.seek(i*100);
                byte[] data = new byte[32];
                file.read(data);
                String name = new String(data,"UTF-8").trim();
                if (username.equals(name)){
                    forward("reg_error.html",response);
                    System.out.println("注册失败！");
                    return;
                }
            }
            //先将指针移到最后
            file.seek(file.length());
            //写用户名、密码、年龄、昵称
            byte[] data = username.getBytes("UTF-8");
            data = Arrays.copyOf(data,32);
            file.write(data);

            data = password.getBytes("UTF-8");
            data = Arrays.copyOf(data,32);
            file.write(data);

            data = nickname.getBytes("UTF-8");
            data = Arrays.copyOf(data,32);
            file.write(data);

            file.writeInt(age);
            //3.注册完毕后响应客户端注册成功页面
            forward("reg_success.html",response);
            System.out.println("注册成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("处理注册业务完毕、、、");
    }
}

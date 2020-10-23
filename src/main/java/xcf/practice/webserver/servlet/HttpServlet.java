package xcf.practice.webserver.servlet;

import xcf.practice.webserver.http.HttpRequest;
import xcf.practice.webserver.http.HttpResponse;
import java.io.File;

/**
 * 所有servlet的超类
 */
public abstract class HttpServlet {
    /**
     *用于处理请求的方法。ClientHandler在调用
     *某请求对应的处理类(某Servlet)时，会调用
     *其service方法。
     * @param request
     * @param response
     */
    public abstract void service(HttpRequest request, HttpResponse response);

    /**
     * 跳转指定页面
     * @param path 路径
     * @param response
     */
    public void forward(String path, HttpResponse response){
        File file = new File("src/main/webapp/"+path);
        response.setEntity(file);
    }
}

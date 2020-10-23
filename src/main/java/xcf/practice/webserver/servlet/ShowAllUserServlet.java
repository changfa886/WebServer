package xcf.practice.webserver.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import xcf.practice.webserver.http.HttpRequest;
import xcf.practice.webserver.http.HttpResponse;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 显示用户列表
 */
public class ShowAllUserServlet extends HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        /**
         * 1:读取user.dat文件，将所有用户读取出来
         *   其中每个用户用一个Map保存，key保存
         *   属性名，value保存该属性的值。
         *   再将这些Map实例存入一个List集合备用
         *
         * 2:利用thymeleaf将List集合的数据绑定到
         *   showAllUser.html页面上。
         *   页面上要对应的添加thymeleaf需要的属性
         *   否则thymeleaf不知道如何绑定。
         */
        try (RandomAccessFile file = new RandomAccessFile("user.dat","r")){
            List<Map<String,String>> userList = new ArrayList<>();
            for (int i = 0; i < file.length() / 100; i++) {
                file.seek(i*100);
                byte[] bytes = new byte[32];
                file.read(bytes);
                String userName = new String(bytes,"UTF-8").trim();
                file.read(bytes);
                String passWord = new String(bytes,"UTF-8").trim();
                file.read(bytes);
                String nickName = new String(bytes,"UTF-8").trim();
                int age = file.readInt();
                Map<String,String> user = new HashMap<>();
                user.put("userName",userName);
                user.put("passWord",passWord);
                user.put("nickName",nickName);
                user.put("age",age+"");
                userList.add(user);
            }
            /*
             * thtmeleaf模板引擎，用来将数据绑定到
             * 静态页面的核心组件
             */
            TemplateEngine templateEngine = new TemplateEngine();
            FileTemplateResolver resolver = new FileTemplateResolver();
            //设置字符集，告知引擎静态页面的字符集
            resolver.setCharacterEncoding("UTF-8");
            //将解析器设置到引擎上,是的引擎知道如何处理静态页面
            templateEngine.setTemplateResolver(resolver);
            //Context用来储存所有要在页面显示的数据
            Context context = new Context();
            context.setVariable("list",userList);
            /*
             * 调用引擎的process处理方法，该方法就是将指定
             * 的数据与指定的页面进行绑定。
             * 参数1:静态页面的路径
             * 参数2:需要在静态页面上显示的动态数据
             * 该方法返回值为一个字符串，内容就是绑定了动态
             * 数据的静态页面所对应的HTML代码
             */
            String html = templateEngine.process("src/main/webapp/ShowAllUser.html",context);
            response.setData(html.getBytes("UTF-8"));
            response.putHeader("Content-Type","text/html");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

package xcf.practice.webserver.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import xcf.practice.webserver.servlet.HttpServlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务端配置相关信息
 */
public class ServerContent {

    private static Map<String, HttpServlet> servletMapping = new HashMap<>();

    static {
        initServletMapping();
    }
    private static void initServletMapping(){
        /*
         * 解析servlets.xml文件，将根标签
         * 下所有名为<servlet>的子标签得到，然后
         * 将每个servlet子标签中的属性path作为key
         * 将属性className的值得到后利用反射加载
         * 这个类的类对象并进行实例化，将实例化的
         * 对象作为value保存到servletMapping这个
         * Map中完成初始化。
         */

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read("src/main/webapp/WEB-INF/servlet.xml");
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();
            for (Element element :elementList){
                String path = element.attributeValue("path");
                String className = element.attributeValue("className");
                Class cla = Class.forName(className);
                HttpServlet servlet = (HttpServlet) cla.newInstance();
                servletMapping.put(path,servlet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServlet getHttpServlet(String path){
        return servletMapping.get(path);
    }
}

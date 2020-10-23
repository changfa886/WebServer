package xcf.practice.webserver.http;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP协议相关定义
 */
public class HttpContext {
    /**
     * Content—Type的值与资源后缀名的对应关系
     * key：资源的后缀名
     * value：对应Content-Type的值
     */
    private static Map<String,String> mime = new HashMap<>();

    //初始化资源类型
    static{
        initMime();
    }
    private static void initMime(){
        try{
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File("src/main/webapp/web-type.xml"));
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.elements("mime-mapping");
            for (Element element : list){
                String extension = element.elementTextTrim("extension");
                String type = element.elementTextTrim("mime-type");
                mime.put(extension,type);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getMimeType(String suffix){
        return mime.get(suffix);
    }

}

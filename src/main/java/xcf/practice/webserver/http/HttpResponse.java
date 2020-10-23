package xcf.practice.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示服务端发送给客户端的具体响应内容
 *
 * 每个响应包含三部分：状态行、响应头、响应正文
 */
public class HttpResponse {
    /**
     * 状态行相关信息定义
     */
    private int stateCode = 200;//状态码
    private String stateReason = "OK";//状态描述
    /**
     * 响应头相关信息定义
     */
    private Map<String,String> headers = new HashMap<>();

    /**
     * 响应正文相关信息定义
     */

    //响应正文的实体文件
    private File entity;

    private byte[] data;

    /**
     * 连接相关信息定义
     */
    private Socket socket;
    private OutputStream out;

    public HttpResponse(Socket socket) {
        try {
            this.socket = socket;
            this.out = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送一个标准的HTTP响应给客户端回应该资源
     */
    public void flush(){
        sendStateLine();
        sendHeaders();
        sendContent();
    }

    /**
     * 发送状态行
     */
    private void sendStateLine(){
        System.out.println("开始发送状态行、、、");
        try {
            String line = "HTTP/1.1"+" "+stateCode+" "+stateReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//CR
            out.write(10);//LF
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("发送状态行完毕、、、");
    }

    /**
     * 发送响应头
     */
    private void sendHeaders(){
        System.out.println("开始发送响应头、、、");
        try{
            Set<Map.Entry<String,String>> set = headers.entrySet();
            for (Map.Entry<String,String> header : set){
                String value = header.getValue();
                String key = header.getKey();
                String line = value+": "+key;
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
            }
            //单独发送一个CR、LF
            out.write(13);
            out.write(10);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("发送响应头完毕、、、");
    }

    /**
     * 发送消息正文
     */
    private void sendContent(){
        System.out.println("开始发送消息正文、、、");
        if (entity!= null){
            try(FileInputStream fis = new FileInputStream(entity);){
                int len = -1;
                byte[] bytes = new byte[1024*10];
                while((len=fis.read(bytes))!=-1){
                    out.write(bytes,0,len);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (data!= null){
            try {
                out.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("发送消息正文完毕、、、");
    }

    /**
     * 向当前响应对象设置响应正文的实体文件
     * 设置的同时根据实体文件的类型设置两个对应的响应头：
     * Content-Type、Content-Length
     * @param entity
     */
    public void setEntity(File entity) {
        this.entity = entity;
        //获取响应正文实体文件的名字
        String name = entity.getName();
        //获取响应正文的数据类型和字节数
        String suffix = name.substring(name.lastIndexOf(".")+1);
        String contentType = HttpContext.getMimeType(suffix);
        headers.put("Content-Type",contentType);
        headers.put("Content-Length",entity.length()+"");
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public void setStateReason(String stateReason) {
        this.stateReason = stateReason;
    }

    //向当前响应对象中添加一个响应头
    public void putHeader(String kry,String value){
        headers.put(kry, value);
    }

    //设置响应正文数据同时向headers中添加一个响应正文字节数的响应头
    public void setData(byte[] data) {
        this.data = data;
        headers.put("Content-Length",data.length+"");
    }
}

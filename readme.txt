第一步、
  WebServer的工作流程大体分为三步:
  1:解析请求
  2:处理请求
  3:发送响应
  通过以上三步将浏览器的请求处理完毕并予以响应，使其能看到处理结果。

  实现功能：
  在ClientHandler中读取客户端发送过来的一行字符串一行字符串的结束标志为连续读取的两个字符分别是CR,LF
  即:回车符和换行符
  因为请求行与消息头共同点都是以一行一行的字符串进行发送的。

第二步、
  由于一个请求包含三部分:请求行，消息头，消息正文而我们后期处理请求这三部分信息都要获取，因此我们开始整理请求。
  设计一个类:HttpRequest,响应对象。使用这个类的每一个实例表示客户端发送过来的一个具体请求内容。
  在HttpRequest中设计若干属性，分别对应请求中各部分内容以便后期通过请求对象获取。在请求对象实例化时进行解析。

  实现:
  1:创建一个包:com.webserver.http
  2:在http包中定义类:HttpRequest 请求对象并设计对应的属性
  3:定义构造方法，在构造方法中完成请求的解析工作以初始化请求对象。

第三步、
  在项目目录下新建一个目录webapps,在里面创建一个网络应用(webapp)
  每一个网络应用会包含:网页，静态资源(图片等素材)，业务逻辑(java程序，处理当前网络应用提供给用户的功能)等。
  WebServer作为一个网络容器，可以同时管理多个网络应用，
  并通过HTTP协议与客户端交互，允许客户端通过url指定想访问的网络应用。

实现:
  1:在项目目录下新建目录webapps
  2:在webapps下新建第一个网络应用目录:myweb
  3:在myweb目录下新建一个页面index.html

  实现WebServer处理请求的工作，这里ClientHandler要根据请求中用户指定的抽象路径去webapps下寻找对应资源，并打桩输出结果。

例如:
  用户在浏览器输入了地址:http://localhost:8088/myweb/index.html
  那么我们之前解析请求后，抽象路径部分就是:/myweb/index.html
  我们就对应的从webapps目录下寻找该资源:./webapps/myweb/index.html

第四步、
  发送响应给客户端。上一个版本中我们根据请求找到了资源，因此，本版本我们要将客户端请求的资源响应给客户端。
  HTTP协议对响应的格式有规定。
  响应:Response,是指服务端发送给客户端的内容。

第五步、
  重构代码，设计一个响应对象HttpResponse,将响应工作移动到这个里面。将来响应内容中会变化的地方我们设计为属性。
  然后ClientHandler在处理请求时，将需要响应的内容设置到响应对象中即可。具体的响应工作由HttpResponse完成。

第六步、
  完成响应404页面的工作。ClientHandler在处理请求时，若发现客户端请求的资源不存在时，应当响应404页面通知
  客户端请求资源不存在的问题。这里发送的状态代码应当也是404.因此，我们需要对HttpResponse进行改动，添加两个新的
  属性分别是状态代码与状态描述。然后在发送状态行操作中根据这两个属性值发送状态代码内容，从而ClientHandler可以根据不同情况设置并进行响应。

实现:
1:在HttpResponse中定义两个属性:
  int statusCode           状态代码,默认值200
  String statusReason      状态描述,默认值"OK"
  因为实际应用中，响应200是最多的操作，因此默认值为200
  可以在正常响应时不用设置这两项，只有特别的情况才需要额外设置

2:为两个属性添加get，set方法

3:修改发送状态行的方法，发送这两个属性的值。

4:在webapps目录下新建子目录root,并将404.html页面
  定义在这里，因为不管哪个网络应用，客户端请求其中资源
  时都可能出现资源不存在的情况，因此响应的404页面应当
  是独立于这些网络应用的。对此我们单独定义一个root目录存放公用的资源。

5:在ClientHandler处理请求的分支中，修改资源不存在时操作的分支
  设置response状态代码为404和描述"NOT FOUND"
  设置响应正文文件为404页面然后响应客户端即可。

第七步、
  完成响应404页面的工作。ClientHandler在处理请求时，若发现客户端请求的资源不存在时，应当响应404页面通知
  客户端请求资源不存在的问题。这里发送的状态代码应当也是404.因此，我们需要对HttpResponse进行改动，添加两个新的
  属性分别是状态代码与状态描述。然后在发送状态行操作中根据这两个属性值发送状态代码内容，从而ClientHandler可以根据不同情况设置并进行响应。


实现:
  1:在HttpResponse中定义两个属性:
  int statusCode           状态代码,默认值200
  String statusReason      状态描述,默认值"OK"
  因为实际应用中，响应200是最多的操作，因此默认值为200
  可以在正常响应时不用设置这两项，只有特别的情况才需要额外设置
 2:为两个属性添加get，set方法
 3:修改发送状态行的方法，发送这两个属性的值。
 4:在webapps目录下新建子目录root,并将404.html页面
  定义在这里，因为不管哪个网络应用，客户端请求其中资源
  时都可能出现资源不存在的情况，因此响应的404页面应当
  是独立于这些网络应用的。对此我们单独定义一个root目录存放公用的资源。

5:在ClientHandler处理请求的分支中，修改资源不存在时操作的分支
  设置response状态代码为404和描述"NOT FOUND"
  设置响应正文文件为404页面然后响应客户端即可。

第八步、
  接着对V8的改进需求操作。
  在上个版本中我们已经将设置响应头Content-Type的工作
  挪到HttpResponse的setEntity方法中了。但是设置的内容
  仍然是写死的"text/html",本版本中我们要根据实际设置的
  正文文件类型来对应的设置Content-Type的值。使得我们可以
  正确响应给客户端其请求的资源并说明正确的类型。

  由于不同的文件类型有对应的Content-Type的值，大约1000多种。并且都有相关规定，可参阅w3c官网。
  因此我们设计一个类:HttpContext,将所有HTTP规定的内容全部放在这里定义。


  实现:
  1:在com.webserver.http包中定义一个类:HttpContext

  2:在HttpContext中定义一个静态属性:Map mime_mapping
  这个Map用来保存所有资源类型与Content-Type对应的值,其中key保存资源的后缀名，如(html,css,js)
  value保存对应的Content-Type值(text/html,text/css)并完成初始化操作。

  3:在HttpContext中定义一个公用的静态方法:getMimeType
  可以根据资源的后缀名获取对应的Content-Type的值

  4:在HttpResponse的setEntity方法中修改逻辑，根据设置
  的正文文件获取其名称，然后得到后缀名，在通过
  HttpContext的getMimeType获取到Content-Type的值并设置到响应头中。

  经过上述操作后，再次请求学子商城的页面时就能看到正确的显示效果了。

第九步、
 解决空请求带来的下标越界问题。

  HTTP协议允许客户端发送空请求，正常情况下客户端发送的
  是一个请求内容。因此我们在处理客户端操作时第一步就是
  解析请求，而我们解析请求第一件事就是解析请求行。但是若
  客户端发送的是空请求,那么我们在读取请求行时实际得到的
  是一个空字符串，没有办法按照空格拆分为三部分，因此这时获取请求行内容就会出现下标越界。
  对于空请求，我们的处理办法应当是直接忽略本次请求处理。


  解决办法:
  当HttpRequest实例化时(解析请求的过程)若遇到了空请求,那么就直接对外抛出一个空请求异常(自定义一个异常),这样
  将异常抛给ClientHandler，当ClientHandler实例化HttpRequest抛出该异常则会忽略后续所有步骤。

第十步、
  使当前WebServer支持所有的资源类型所对应的Content-Type

  Tomcat是一个开源的免费的web容器，也是将来使用最广泛的，
  它已经将所有的类型都整理了出来并保存在其安装路径下的conf目录中，名为web.xml文件。

  我们要做的工作就是解析这个web.xml文件，将所有的类型得到并用来初始化HttpContext中的Map:mime_mapping.

第十一步、
  从本版本开始，我们来处理业务操作.比如用户注册，登录等等。

  完成用户注册功能
  注册流程为:
  1:用户访问用户注册页面
  2:在注册页面上输入用户信息(用户名，密码等)
  3:点击注册按钮，将数据提交给服务端
  4:服务端通过request得到用户提交的数据并保存
  5:响应客户端注册结果页面(成功或失败)


  在本版中我们要了解的知识点:
  1:表单的应用 form。
  2:GET方式的提交规则。
  3:解析请求时,针对GET请求提交数据的解析。



实现
  1:在webapps/myweb下新建注册用户页面:reg.html
  在其中定义表单，并且指定提交路径为"./reg"。

  2:本环节的工作重点
  在解析请求时，如果是表单用GET形式提交数据，那么数据会被拼接到url中，这是请求行中抽象路径部分就能看到
  格式如下:/myweb/reg?username=xxxx&password=xxx&...
  因此我们针对这种传参的请求，要将参数也解析出来并保存到请求对象中，方便处理业务时获取这些参数内容。
  这个操作只需要开发一次，将来在处理其他业务时也可以直接从请求对象中获取参数了。


3:在ClientHandler处理请求的环节中添加一个新的分支判断抽象路径的值是否为"/myweb/reg",若是则打桩备用
  若不是则走原来的处理业务操作(判断是否为请求资源文件)这里存在两个问题，原来处理请求时是根据url作为抽象
  路径使用，但是由于现在该抽象路径可能含有参数了，因此不适用了，要改为根据requestURI的值判定了

  第二个问题，当走到处理业务的分支中，由于现在仅打桩,没有做实质操作，会导致response对象中的响应正文实体
  文件entity没有设置，但此时ClientHandler在处理第三个环节响应客户端时，HttpResponse发送正文会读取entity
  指定的文件，因此会出现空指针。所以在这里要添加一个判断，只有entity不为null才作为正文内容发送。

第十二步、
  完成用户注册业务处理

第十三步、
  完成用户登录功能

  登录流程:
  1:用户请求登录页面 login.html
  2:在页面中输入用户名及密码，然后点击登录按钮
  3:form表单将登录信息提交路径"./login"
  4:ClientHandler在处理请求的环节添加一个新的分支，
  若请求路径的值为"/myweb/login",则实例化登录业务
  处理类:LoginServlet,并调用其service方法
  5:LoginServlet的service方法处理登录工作
  5.1:通过request获取用户输入的登录信息(用户名，密码)
  5.2:读取user.dat文件，并比对是否有一条记录的用户名与密码与其一致
  若一致，则设置response对象响应登录成功的页面:login_success.html
  若用户名一致，但密码错误。或没有此用户，都设置
  response对象响应登录失败页面login_fail.html
  登录成功页面上显示:登录成功，欢迎回来!
  登录失败页面上显示:登录失败，用户名或密码不正确!

第十四步、
  重构项目，在servlet包中定义类:HttpServlet,这个类
  作为所有Servlet的超类使用。将共有代码放在这里，约束所有Servlet处理业务方法service


  支持POST请求
  用户在页面表单输入的内容如果包含隐私信息，或者存在
  上传附件的操作时，表单提交形式应当为POST请求。
  而POST请求会将用户提交的数据包含在请求的消息正文中
  因此我们应当在HttpRequest中完成对POST请求的解析工作。

第十五步、
 使用Thymeleaf，完成动态页面。

  本版本我们将实现将现有的所有注册用户展现在一个页面中给用户看。
  这需要将user.dat文件中所有用户数据读取出来并利用
  一个已经写好的html页面展现，利用Thymeleaf将动态数据绑定到静态页面后将页面发送给用户。

第十六步、
  之前的WebServer存在一个弊端，就是每当添加一个新的
  业务处理类(某Servlet)，就要修改ClientHandler
  在处理请求的环节添加一个新的分支，判断新的请求然后实例化这个Servlet调用其service方法。
  这样会导致处理业务与ClientHandler的流程控制有一个必然的耦合关系。

  利用反射，我们要达到ClientHandler不再关心具体哪个
  请求对应哪个Servlet，而是在配置文件(servlets.xml)
  中配置对应关系，ClientHandler根据配置信息处理。
  这样将来无论添加什么新的业务，ClientHandler都不用再进行修改了。

第十七步、
  修改WebServer主类，使用线程池来管理线程。

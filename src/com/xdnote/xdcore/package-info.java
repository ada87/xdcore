/**
 * 框架框架主体，包含一个过滤器 {@link com.xdnote.xdcore.BaseFilter}  和一个业务处理类 {@link com.xdnote.xdcore.BaseAction} 
 * 实际使用时，需要扩展这两个类去实现MVC,请进入本包的说明查看详细教程<br>
 * 包括三个包，分别为util,result,plugin
 * 
 * @since 0.1
 * @author xdnote.com
 * 
 * <h3>一：创建一个web项目</h3>
 * <p>创建项目后，如果你下载的是jar包，则将xdcore.jar放在你的WEB-INF/lib下，并build到path里面，如果你是用的源码，创建一个Source Folder（一般不创建的话就是src，推荐创建一个，以免和本身的项目代码搞混），
 * 并复制com文件夹到你的folder下</p>
 * <h3>二：在你项目的web.xml里面加入以下配置</h3>
 * 	<pre>
 &lt;filter&gt;
	&lt;filter-name&gt;xdcore&lt;/filter-name&gt;
	&lt;filter-class&gt;com.xdnote.xdcore.BaseFilter&lt;/filter-class&gt;
&lt;/filter&gt;
&lt;filter-mapping&gt;
	&lt;filter-name&gt;xdcore&lt;/filter-name&gt;
	&lt;url-pattern&gt;/*&lt;/url-pattern&gt;
&lt;/filter-mapping&gt;
</pre>
 * <h3>三：编写一个业务类，实现BaseAction</h3>
 * <pre>
-----------------HomeAction-----------------
public class HomeAction extends BaseAction {
	&#64;Override
	public Object index(HttpServletRequest request ,HttpServletResponse response, String[] args){
		request.setAttribute("welcome", "Hello World");
		return "/index.jsp";
	}
	
	public Object home(HttpServletRequest request ,HttpServletResponse response, String[] args){
		request.setAttribute("welcome", "Welcome to home");
		return "/index.jsp";
	}
}
-----------------index.jsp--------------------
 &lt;% out.println(request.getAttrbute("welcome"))%&gt;
 * </pre>
 * <h3>四：配置映射xMap.properties</h3>
 * <p>要想执行刚才定义的Action，只需要配置你想要的链接映射到类上就可以了<br>
 * 你需要在你的src目录或你的Source Foder上建一个文件xMap.properties,然后添加以下一行<br>
 * /home=com.xxx.actions.HomeAction<br>
 * </p>
 * <h3>五：OK</h3>
 * <p>准备工作已经完成，启动你的Server，然后在浏览器里面查看 http://ip:port/project/ 和 http://ip:port/project/home 就会发现分别执行了index与home方法，并返回了页面</p>
 * <h3>六：更多</h3>
 * <p>xdCore除了基本的MVC外，还提供了缓存管理，SQL连接池，JSON返回，Redirct，Cookie管理，HTTP发送等等基本的小工具，详细请参考API文档</p>
 */
package  com.xdnote.xdcore;

/**
 * 框架的插件相关，如果插件需要注册到控制流里面，<br>
 * 配置插件参数一般可以创建xPlugin.properties文件，进行配置。
 * 
 * @author xdnote
 * @since 0.1
 * 
 * 目前本插件实现了一个MySql的执行器与邮件发送<br>
 * <p>MySql执行器使用说明：本Util不含驱动，如果需要使用，则额外下载驱动包，并配置在项目环境里面(下载地址：<a href="http://dev.mysql.com/downloads/connector/j/ target="_blank">http://dev.mysql.com/downloads/connector/j/</a>")<br>
 * 详细使用方法请参见{@link com.xdnote.xdcore.plugin.MysqlExecuter}执行器 与 {@link com.xdnote.xdcore.plugin.MySqlConnectPool} 连接池，一般都</p>
 * <p>MailUtil使用说明：使用需要额外下载邮件发送包，请去官方网站下载，详细使用方法见 {@link com.xdnote.xdcore.plugin.MailUtil} </p>
 * 1.搜索引擎高亮<br>
 * 2.BI统计<br>
 * 3.计数器<br>
 * 4.文件上传<br>
 * 5.结果处理<br>
 */
package com.xdnote.xdcore.plugin;
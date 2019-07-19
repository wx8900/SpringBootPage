package com.demo.test.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取发出request请求的客户端ip, 如果是自己发出的请求,那么获取的是自己的ip
 * 注意:
 * 如果使用此工具,获取到的不是客户端的ip地址;而是虚拟机的ip地址(d当客户端安装有VMware时,可能出现此情况);
 * 那么需要在客户端的[控制面板\网络和 Internet\网络连接]中禁用虚拟机网络适配器
 *
 * @author Jack
 * @date 2019/05/30 15:32 PM
 */
public class IPUtils {

    static Logger logger = LogManager.getLogger(IPUtils.class);

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                        LoggerUtils.logInfo(logger,"IP Address is :" + inet);
                    } catch (UnknownHostException e) {
                        LoggerUtils.logError(logger,"UnknownHostException happen in getIpAddr()", e.getStackTrace());
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
            LoggerUtils.logError(logger,"Exception happen in getIpAddr()", e.getStackTrace());
        }

        return ipAddress;
    }
}
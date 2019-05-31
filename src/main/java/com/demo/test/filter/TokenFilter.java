package com.demo.test.filter;

import com.demo.test.domain.Student;
import com.demo.test.utils.MDUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Token Utils 类
 *
 * @author Jack
 * @date 2019/05/30 14:36 PM
 */
// /* 表示全部拦截
@WebFilter(filterName = "tokenFilter", urlPatterns = "/*")
public class TokenFilter implements Filter {

    private static final String salt = "1234567890...abcdefghigklmpopqrstuvwxyz";

    //在这里面填不需要被拦截的地址
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("/v1/api/students/login", "/v1/api/students/isLogin", "/findCategory"))
    );

    /**
     * 校验签名
     *
     * @param timestamp
     * @param signature
     * @param params
     * @return
     */
    public static boolean checkSignature(long timestamp, String signature, String[] params) {
        if (timestamp == 0 || signature == null || params == null) {
            return false;
        }
        long nowTime = System.currentTimeMillis();
        // 接口请求的时间在600秒钟之前的直接抛弃
        if ((nowTime - timestamp) > 600000) {
            return false;
        }
        // 将方法的参数按字典排序,然后按字典排序将它们加起来的字符串+盐按SHA512加密，和签名作对比
        Arrays.sort(params);
        int length = params.length;
        String signStr = "";
        if (params != null && length > 0) {
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                if (params[i] != null && params[i].length() > 0) {
                    sb.append(params[i]);
                }
            }
            signStr = MDUtils.encrypt(sb.toString());
            /*if (signature.equals(EncryptionUtil.SHA512((signStr+salt)))) {
                return true;
            }*/
        }
        return false;
    }

    //初始化调用的方法
    //当服务器 被启动的时候，调用
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    //拦截的方法
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        //解决跨域的问题
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With,X-App-Id, X-Token");
        response.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");

        String path = request.getRequestURI().substring(request.getContextPath().length())
                .replaceAll("[/]+$", "");
        System.out.println("=========> Going into the tokenFilter ----- path : " + path);
        boolean allowePath = ALLOWED_PATHS.contains(path);

        //需要拦截的方法
        if (!allowePath) {
            Student student = (Student) request.getSession().getAttribute("currentUser");
            if (student != null) {
                /*// timeStamp是客户端从Header传过来的值
                Long timeStamp = RequestHeaderContext.getInstance().getTimeStamp();
                boolean checkTime = checkTime(timeStamp, 30 * 1000);
                if (!checkTime) {
                    return responseErrorAPISecurity(response);
                }*/

                // 请求的API参数，如果是再body，则MD5；如果是param，则原字符串
                /*StringBuffer urlSign = new StringBuffer();
                if ("POST".equals(request.getMethod()) || "PUT".equals(request.getMethod())) {
                    String bodyStr = RequestReaderUtil.ReadAsChars(request);
                    String bodySign = "";
                    if (!StringUtils.isEmpty(bodyStr)){
                        bodySign = DigestUtils.md5DigestAsHex((bodyStr).getBytes());
                    }
                    urlSign = new StringBuffer(bodySign);
                } else if ("GET".equals(request.getMethod()) || "DELETE".equals(request.getMethod())) {
                    String params = request.getQueryString();
                    if (params == null){
                        params = "";
                    }
                    urlSign = new StringBuffer(params);
                }
                // “请求的API参数”+“时间戳”+“盐”进行MD5算法加密
                String sign = DigestUtils.md5DigestAsHex(urlSign.append(timeStamp).append(salt).toString().getBytes());

                // signature是客户端从Header传过来的值
                if (signature.equals(sign)) {
                    return true;
                } else {
                    return false;
                }*/
                filterChain.doFilter(request, response);
            } else {
                response.getWriter().write("No person has login, please login in !!!");
            }
        } else {  //不需要被拦截的方法
            //直接放行
            filterChain.doFilter(request, response);
        }

    }

    //销毁时候调用的方法
    @Override
    public void destroy() {
    }
}

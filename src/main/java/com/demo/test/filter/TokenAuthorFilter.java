package com.demo.test.filter;

import com.alibaba.fastjson.JSON;
import com.demo.test.constant.Constant;
import com.demo.test.domain.Student;
import com.demo.test.exception.ApiErrorResponse;
import com.demo.test.exception.GlobalExceptionHandler;
import com.demo.test.utils.TokenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 已废弃
 * token验证拦截类————在2019/10/07之后该类已废弃
 *
 * @author Jack
 * @version 2.0, use now
 * @date 2019/06/02
 * @date 2019/10/07 no longer used
 */
@WebFilter(filterName = "tokenAuthorFilter", urlPatterns = "/*")
public class TokenAuthorFilter implements Filter {

    static Logger logger = LogManager.getLogger(TokenAuthorFilter.class);

    //在这里面填不需要被拦截的地址
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList("/v1/api/students/login", "/v1/api/students/isLogin"
                    , "/api", "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs"
                    , "/webjars/*", "/addStudent", "/loginForMap", "/loginForParams"
                    , "/test", "/myException", "/byzero", "/druid/login.html", "/druid/*"
                    , "/swagger-resources/configuration/ui", "/swagger-resources"
                    , "/swagger-resources/configuration/security"))
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        // 设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        rep.setHeader("Access-Control-Allow-Origin", "*");
        //允许的访问方法
        rep.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        //Access-Control-Max-Age 用于 CORS 相关配置的缓存
        rep.setHeader("Access-Control-Max-Age", "3600");
        rep.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                "X-App-Id, Token, Content-Length, Authorization");
        rep.setHeader("Access-Control-Allow-Credentials", "true");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        // header方式传递token, 实际是signature
        String token = req.getHeader("token");
        String path = req.getRequestURI().substring(req.getContextPath().length())
                .replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(path);
        boolean isFilter = false;
        ApiErrorResponse apiErrorResponse;

        String method = ((HttpServletRequest) request).getMethod();
        if (Constant.OPTIONS.equals(method)) {
            rep.setStatus(HttpServletResponse.SC_OK);
        } else {
            if (!allowedPath) {
                String msg, code;
                if (null != token && token.length() > 0) {
                    Student student = (Student) req.getSession().getAttribute("currentUser");
                    if (student != null) {
                        if (TokenUtils.volidateToken(token, student.getId())) {
                            msg = "用户授权认证通过!";
                            code = Constant.SUCCESS;
                            isFilter = true;
                        } else {
                            code = Constant.TOKEN_INVALID;
                            msg = "客户端请求参数Token验证失败！请重新申请 token!";
                            logger.error(msg + " {token} : " + token);
                        }
                    } else {
                        code = Constant.NO_LOGIN_USER;
                        msg = "当前没有用户登录，请重新登录!";
                        logger.error(msg + " {token} : " + token);
                    }
                } else {
                    code = Constant.NO_TOKEN;
                    //msg = "客户端请求无参数token信息, 没有访问权限！";
                    msg = "用户没有登录, 没有访问权限！";
                    logger.error(msg + " {token} : " + token);
                }
                apiErrorResponse = ApiErrorResponse.builder().code(code).message(msg).build();

                // 验证失败
                String resultCode = apiErrorResponse.getCode();
                if (Constant.NO_TOKEN.equals(resultCode)
                        || Constant.TOKEN_INVALID.equals(resultCode)
                        || Constant.NO_LOGIN_USER.equals(resultCode)) {
                    try (OutputStreamWriter osw =
                                 new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
                         PrintWriter writer = new PrintWriter(osw, true)) {
                        String jsonStr = JSON.toJSONString(apiErrorResponse);
                        writer.write(jsonStr);
                        writer.flush();
                    } catch (IOException e) {
                        logger.error("过滤器返回信息失败，错误:" + GlobalExceptionHandler.buildErrorMessage(e));
                    }
                    return;
                }
                if (isFilter) {
                    logger.info("token filter OK!");
                    chain.doFilter(request, response);
                }
            } else {
                //不需要被拦截的方法,直接放行
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void init(FilterConfig arg0) {
    }

    @Override
    public void destroy() {
    }

}
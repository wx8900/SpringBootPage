package com.demo.test.filter;

import com.alibaba.fastjson.JSON;
import com.demo.test.domain.Constant;
import com.demo.test.domain.ResultInfo;
import com.demo.test.domain.Student;
import com.demo.test.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * token验证拦截
 *
 * @author   Jack
 * @time     2019/06/02
 * @version  latest version, use now
 */
@WebFilter(filterName = "tokenAuthorFilter", urlPatterns = "/*")
public class TokenAuthorFilter implements Filter {

    //在这里面填不需要被拦截的地址
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("/v1/api/students/login", "/v1/api/students/isLogin"))
    );
    private static Logger logger = LoggerFactory.getLogger(TokenAuthorFilter.class);

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
        boolean allowePath = ALLOWED_PATHS.contains(path);
        boolean isFilter = false;
        ResultInfo resultInfo;

        String method = ((HttpServletRequest) request).getMethod();
        if (Constant.OPTIONS.equals(method)) {
            rep.setStatus(HttpServletResponse.SC_OK);
        } else {
            if (!allowePath) {
                if (null != token || token.length() > 0) {
                    Student student = (Student) req.getSession().getAttribute("currentUser");
                    if (student != null) {
                        if (TokenUtil.volidateToken(token, student.getId())) {
                            resultInfo = new ResultInfo(Constant.SUCCESS, "用户授权认证通过!");
                            isFilter = true;
                        } else {
                            String msg = "客户端请求参数Token无效，token验证失败！请重新申请 token!";
                            resultInfo = new ResultInfo(Constant.TOKEN_INVALID, msg);
                            logger.error(msg);
                        }
                    } else {
                        resultInfo = new ResultInfo(Constant.NO_LOGIN_USER, "当前没有登录用户，请重新登录!");
                    }
                } else {
                    resultInfo = new ResultInfo(Constant.NO_TOKEN, "客户端请求参数无token信息, 没有访问权限！");
                }
                // 验证失败
                String code = resultInfo.getCode();
                if (Constant.NO_TOKEN.equals(code)
                        || Constant.TOKEN_INVALID.equals(code)
                        || Constant.NO_LOGIN_USER.equals(code)) {
                    PrintWriter writer = null;
                    OutputStreamWriter osw = null;
                    try {
                        osw = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
                        writer = new PrintWriter(osw, true);
                        String jsonStr = JSON.toJSONString(resultInfo);
                        writer.write(jsonStr);
                        writer.flush();
                        writer.close();
                        osw.close();
                    } catch (UnsupportedEncodingException e) {
                        logger.error("过滤器返回信息失败，编码错误:" + e.getMessage(), e);
                    } catch (IOException e) {
                        logger.error("过滤器返回信息失败，IO错误:" + e.getMessage(), e);
                    } finally {
                        if (null != writer) {
                            writer.close();
                        }
                        if (null != osw) {
                            osw.close();
                        }
                    }
                    return;
                }
                if (isFilter) {
                    logger.info("token filter过滤ok!");
                    chain.doFilter(request, response);
                }
            } else {  //不需要被拦截的方法,直接放行
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
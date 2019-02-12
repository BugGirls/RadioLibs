package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ManagerBusiness;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录api
 *
 * @author Hystar
 * @date 2017/9/29
 */
@WebServlet("/api/manager/login")
public class LoginServlet extends BaseServlet {

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final int TEN_SECONDS = 60 * 1000;

    /**
     * 验证token是否有效
     *
     * @param request
     * @return
     */
    private Map<String, String> checkToken(HttpServletRequest request) {
        // HashMap的默认值大小为16
        Map<String, String> result = new HashMap<>(16);
        String queryUri = request.getRequestURI() + "?" + request.getQueryString();
        long timestamp = NumberUtils.toLong(request.getParameter("time"), 0L);

        // 10秒有效期
        if (System.currentTimeMillis() - timestamp < TEN_SECONDS) {
            // 获取参数中的appID
            String appId = request.getParameter("appID");
            // 验证是否是自己的appId, 并通过appID判断是进入音频库还是进入网络广播
            // 进入网络广播
            if (appId.equals(Utils.APPID_VLIVE)) {
                String token = request.getParameter("token");
                String signature = queryUri.replaceAll("&token=.*", String.format("&secret=%s", Utils.APPSECRET_VLIVE));
                signature = DigestUtils.sha1Hex(signature);
                // 验证通过
                if (signature.equals(token)) {
                    result.put(SUCCESS, appId);
                } else {
                    result.put(ERROR, "非法请求");
                }
            }
            // 进入音频库
            else if (appId.equals(Utils.APPID_RESP)) {
                String token = request.getParameter("token");
                String signature = queryUri.replaceAll("&token=.*", String.format("&secret=%s", Utils.APPSECRET_RESP));
                signature = DigestUtils.sha1Hex(signature);
                // 验证通过
                if (signature.equals(token)) {
                    result.put(SUCCESS, appId);
                } else {
                    result.put(ERROR, "非法请求");
                }
            }
            // 参数错误
            else {
                result.put(ERROR, "AppID不正确，非法请求");
            }
        } else {
            result.put(ERROR, "token失效");
        }

        return result;
    }

    /**
     * 用户认证成功后 登录 构造session
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private synchronized String centerLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取验证token结果
        Map<String, String> map = checkToken(request);
        if (map.get(SUCCESS) != null) {
            String name = request.getParameter("name");
            if(Utils.isNotEmpty(name)) {
                name = Utils.base64Decode2(name);
            }

            ManagerBean managerBean = ManagerBusiness.getInstance().load(name);
            if (managerBean != null) {
                ManagerBusiness.getInstance().saveByName(managerBean);
                ManagerBusiness.getInstance().attachRoleBeanList(managerBean);
                ManagerBusiness.getInstance().attachUrlList(managerBean);
                request.getSession().setAttribute(EnumValue.LITERAL_MANAGER, managerBean);
            } else {// 如果用户为空，则添加该用户
                managerBean = new ManagerBean();
                managerBean.setStatus(EnumValue.Status.EFFECTIVE.getCode());
                managerBean.setName(name);
                ManagerBusiness.getInstance().saveByName(managerBean);
                ManagerBusiness.getInstance().attachRoleBeanList(managerBean);
                ManagerBusiness.getInstance().attachUrlList(managerBean);
                request.getSession().setAttribute(EnumValue.LITERAL_MANAGER, managerBean);
            }

            if (managerBean != null && !managerBean.getUrlList().isEmpty()) {
                Logger.info(GSON.toJson(managerBean.getUrlList().toString()));
                if (managerBean.getUrlList().toString().contains("vlive") && !managerBean.getUrlList().toString().contains("resp")) {
                    response.sendRedirect("/vlive/index.jsp");
                } else if (managerBean.getUrlList().toString().contains("resp") && !managerBean.getUrlList().toString().contains("vlive")) {
                    response.sendRedirect("/resp/index.jsp");
                } else {
                    response.sendRedirect("/index.jsp");
                }
            } else {
                response.sendRedirect("/vlive/error.xhtml");
            }
        }
        return map.get(ERROR);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> result = checkToken(request);
        System.out.println(result);
        centerLogin(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }
}

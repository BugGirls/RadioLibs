package hndt.radiolibs.biz;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.bean.PermissionBean;
import hndt.radiolibs.bean.RoleBean;
import hndt.radiolibs.util.*;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ManagerBusiness {

    private static String host = "http://uc.hndt.com";
    private static String appID = "37ec2961f4fd4f4c8f4dc810b088c54f";
    private static String appSecret = "f01363c380547c504a339d82f190bdaae9f99ca7";

    String auth(String url) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        url = url + (url.indexOf('?') > 0 ? "&" : "?");
        String url2 = String.format("%stime=%s&appID=%s&appSecret=%s", url, timestamp, appID, appSecret);
        String token = DigestUtils.sha1Hex(url2);
        url2 = String.format("%stime=%s&appID=%s&token=%s", url, timestamp, appID, token);
        return url2;
    }

    public String login(String name, String password) {
        String tip = null;
        if (Utils.isEmpty(name)) {
            tip = "请输入登录姓名";
        } else if (Utils.isEmpty(password)) {
            tip = "请输入口令";
        }

        name = StringUtils.trim(name);
        password = StringUtils.trim(password);

        if (Utils.isEmpty(tip)) {
            OkHttpClient client = new OkHttpClient();
            String json = Utils.asJsonMap("name", name, "password", password);
            FormBody params = new FormBody.Builder().add("json", json).build();
            String url = host + auth("/api/manager/login");
            System.out.println(url);
            Request request = new Request.Builder().url(url).post(params).build();
            try {
                Response response = client.newCall(request).execute();
                tip = (response.body().string());
                Logger.error(tip);
            } catch (Exception e) {
                Logger.error(e);
            }
        } else {
            tip = GSON.toJson(Utils.asHashMap("error", tip));
        }

        return tip;
    }

    public int toggleStatus(ManagerBean bean) {
        int r = DBTool.update("UPDATE manager SET status=? WHERE id=?", bean.getStatus() == 1 ? 0 : 1, bean.getId());
        return r;
    }

    public PageBean pagination(String keyword, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;

        SQL sql = SQL.of("SELECT * FROM manager WHERE id>1").and("name LIKE", keyword).append("ORDER BY id DESC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<ManagerBean> list = DBTool.list(ManagerBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(list);

        return pageBean;
    }

    /**
     * 通过id来判断是添加还是更新
     *
     * @param bean
     * @return
     */
    public int save(ManagerBean bean) {
        int r = -1;
        if (bean.getId() == null || bean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO manager(name, role_ids, permission) VALUES (?,?,?)", bean.getName(), bean.getRole_ids(), bean.getPermission());
            if (id > 0) {
                bean.setId(id);
            }
        } else {
            r = DBTool.update("UPDATE manager SET role_ids=?, permission=?, status=? WHERE id=?", bean.getRole_ids(), bean.getPermission(), bean.getStatus(), bean.getId());
        }
        return r;
    }

    /**
     * 通过登录名来判断是添加还是更新
     *
     * @param bean
     * @return
     */
    public int saveByName(ManagerBean bean) {
        int r = -1;

        if (bean.getId() == null) {
            long id = DBTool.insert("INSERT INTO manager(name, status) VALUES (?,?)", bean.getName(), bean.getStatus());
            if (id > 0) {
                bean.setId(id);
                r = 1;
            }
        } else {
            r = DBTool.update("UPDATE manager SET name=? WHERE id=?", bean.getName(), bean.getId());
        }

        return r;
    }

    public List<ManagerBean> listByIds(List<Long> idList) {
        return DBTool.list(ManagerBean.class, "SELECT * FROM manager WHERE id IN(" + SQL.toInString(idList) + ") AND status=1");
    }

    public int delete(ManagerBean bean) {
        int r = DBTool.update("DELETE FROM manager WHERE id=?", bean.getId());
        return r;
    }

    public ManagerBean load(long id) {
        ManagerBean bean = DBTool.find(ManagerBean.class, "SELECT * FROM manager WHERE id=?", id);
        return bean;
    }

    public ManagerBean load(String name) {
        ManagerBean bean = DBTool.find(ManagerBean.class, "select * from manager where name=?", name);
        return bean;
    }

    public ManagerBean loadRole(long id) {
        ManagerBean bean = DBTool.find(ManagerBean.class, "SELECT * FROM role WHERE id=?", id);
        return bean;
    }

    public List<ManagerBean> list() {
        List<ManagerBean> managerBeanList = DBTool.list(ManagerBean.class, "select * from manager where status=1");
        for (ManagerBean managerBean : managerBeanList) {
            if (managerBean.getRole_ids() != null) {
                List<Long> roleIds = GSON.toList(managerBean.getRole_ids(), Utils.typeLong);
                managerBean.setRoleList(RoleBusiness.getInstance().listByIds(roleIds));
            }

        }
        return managerBeanList;
    }



    /**
     * 获取管理员ID列表
     *
     * @return
     */
    public List<Long> superManagerIds() {
        List<Long> list = new ArrayList<>(5);
        SQL sql = SQL.of("SELECT * FROM manager WHERE role_ids IN (1,2,3)");
        List<ManagerBean> managerBeanList = DBTool.list(ManagerBean.class, sql.sql(), sql.params());
        for (ManagerBean managerBean : managerBeanList) {
            list.add(managerBean.getId());
        }
        return list;
    }

    public void attachRoleBeanList(ManagerBean bean) {
        Logger.info(GSON.toJson(bean));
        if (Utils.isNotEmpty(bean.getRole_ids())) {
            List<Long> list = GSON.toList(bean.getRole_ids(), Utils.typeLong);
            bean.setRoleList(RoleBusiness.getInstance().listByIds(list));
        }
    }

    public void attachRoleBeanListAll(ManagerBean bean) {
        List<RoleBean> roleBeanList = RoleBusiness.getInstance().list();
        bean.setRoleList(roleBeanList);
    }

    public void attachUrlList(ManagerBean bean) {
        if (Utils.isEmpty(bean.getUrlList())) {
            bean.setUrlList(new ArrayList<>());
        }
        if (bean.getRoleList() != null) {
            for (RoleBean role : bean.getRoleList()) {
                RoleBusiness.getInstance().attachPermissionBeanList(role);
                for (PermissionBean permission : role.getPermissionBeanList()) {
                    PermissionBusiness.getInstance().attachUrlList(permission);
                    bean.getUrlList().addAll(permission.getUrlList());
                }
            }
        }
    }

    public boolean containRole(ManagerBean bean, int role_id) {
        Optional<RoleBean> any = bean.getRoleList().stream().filter(x -> role_id == x.getId().intValue()).findAny();
        return any.isPresent();
    }

    /**
     * 判断ManagerBean是否有访问uri的权限
     *
     * @param bean
     * @param uri
     * @return
     */
    public boolean allowVisit(ManagerBean bean, String uri) {
        boolean allow = false;
        List<String> urlList = bean.getUrlList();
        for (String item : urlList) {
            int match_index = item.indexOf('*');
            //不带*，严格匹配
            if (match_index == -1) {
                if (Objects.equals(item, uri)) {
                    allow = true;
                    break;
                }
            }
            //*开头，如*.xhtml
            else if (match_index == 0) {
                String section = item.substring(1);
                if (uri.endsWith(section)) {
                    allow = true;
                    break;
                }
            }
            //*结尾 如/vlive/*
            else if (match_index == item.length() - 1) {
                String section = item.substring(0, match_index);
                if (uri.startsWith(section)) {
                    allow = true;
                    break;
                }
            }
            //*在中间 如/vlive/resp_*.xhtml
            else if (match_index > 0 && match_index < item.length() - 1) {
                String prefix = item.substring(0, match_index);
                String suffix = item.substring(match_index + 1, item.length());
                if (uri.startsWith(prefix) && uri.endsWith(suffix)) {
                    allow = true;
                    break;
                }
            }
        }
        return allow;
    }

    private static ManagerBusiness biz = null;

    private ManagerBusiness() {
    }

    public synchronized static ManagerBusiness getInstance() {
        if (biz == null) {
            biz = new ManagerBusiness();
        }
        return biz;
    }
}

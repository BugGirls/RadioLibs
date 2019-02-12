package hndt.radiolibs.biz;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.bean.RuntimeBean;
import hndt.radiolibs.ctrl.BaseController;
import hndt.radiolibs.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

/**
 * 网络广播频率Business
 * <p>
 *
 * @author Hystar
 * @date 2017/7/17
 */
public class ChannelBusiness {

    public PageBean pagination(Long manager_id, String keyword, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sql = SQL.of("SELECT id, uuid, manager_id, name, description, logo, status, createtime FROM channel")
                .and("manager_id=", manager_id)
                .and("name LIKE", keyword)
                .append("ORDER BY id DESC")
                .limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<ChannelBean> channelBeanList = DBTool.list(ChannelBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(channelBeanList);
        return pageBean;
    }

    public int save(ChannelBean bean) {
        int r = 0;
        if (bean.getId() == null || bean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO channel(uuid, manager_id, name, description, logo, status, createtime) VALUES(?,?,?,?,?,?,?)", bean.getUuid(), bean.getManager_id(), bean.getName(), bean.getDescription(), bean.getLogo(), bean.getStatus(), bean.getCreatetime());
            if (id > 0) {
                bean.setId(id);
                r = 1;
            }
        } else {
            ManagerBean managerBean = ManagerBusiness.getInstance().load(bean.getManager_id());
            if (managerBean == null) {
                bean.setManager_id(new BaseController().getManagerIdParam());
            }
            r = DBTool.update("UPDATE channel SET name=?, description=?, logo=?, status=?, createtime=?, manager_id=? WHERE id=?", bean.getName(), bean.getDescription(), bean.getLogo(), bean.getStatus(), bean.getCreatetime(), bean.getManager_id(), bean.getId());
        }
        return r;
    }

    public int toggleStatus(ChannelBean bean) {
        int r = DBTool.update("UPDATE channel SET status=? WHERE id=?", bean.getStatus() == 1 ? 0 : 1, bean.getId());
        return r;
    }

    public int delete(ChannelBean bean) {
        int r = DBTool.update("DELETE FROM channel WHERE id=?", bean.getId());
        return r;
    }

    public ChannelBean load(long id) {
        ChannelBean channelBean = DBTool.find(ChannelBean.class, "SELECT id, uuid, manager_id, name, description, logo, status, createtime FROM channel WHERE id=?", id);
        return channelBean;
    }

    public List<ChannelBean> list() {
        return DBTool.list(ChannelBean.class, "SELECT id, uuid, manager_id, name, description, logo, status, createtime FROM channel WHERE status=1 ORDER BY id DESC");
    }

    public List<ChannelBean> list(Long manager_id) {
        return DBTool.list(ChannelBean.class, "SELECT id, uuid, manager_id, name, description, logo, status, createtime FROM channel WHERE manager_id=? AND status=1", manager_id);
    }

    public List<ChannelBean> list(Timestamp nowDate) {
        List<ChannelBean> result;
        List<RuntimeBean> runtimeBeanList = DBTool.list(RuntimeBean.class, "SELECT DISTINCT channel_id FROM runtime WHERE playdate=?", nowDate);
        List<Long> ids = new ArrayList<>();
        runtimeBeanList.forEach(runtimeBean -> ids.add(runtimeBean.getChannel_id()));
        result = listByIds(ids, null);
        return result;
    }

    public List<ChannelBean> listByIds(List<Long> ids, Long manager_id) {
        List<ChannelBean> channelBeanList = Collections.EMPTY_LIST;
        if (Utils.isNotEmpty(ids)) {
            SQL sql = SQL.of("SELECT * FROM channel").and("id IN", SQL.toInString(ids)).and("status=", 1).and("manager_id=", manager_id);
            channelBeanList = DBTool.list(ChannelBean.class, sql.sql(), sql.params());
        }
        return channelBeanList;
    }

    /**
     * 文件上传
     *
     * @param bean
     * @return
     */
    public String logoUpload(ChannelBean bean) {
        Part part = bean.getPart();
        String header = part.getHeader("Content-Disposition");
        String fileName = header.substring(header.indexOf("filename=\"") + 10, header.lastIndexOf("\""));// 获取上传文件名
        String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + IDGen.id();
        String suffix = fileName.substring(fileName.lastIndexOf("."));// 获取文件后缀名
        fileName = newFileName + suffix;
        Logger.info("上传的文件名称：" + fileName);
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        Path path = Paths.get(servletContext.getRealPath("/upload/"), fileName);
        try {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            byte[] buffer = new byte[(int) part.getSize()];
            IOUtils.readFully(part.getInputStream(), buffer);
            FileUtils.writeByteArrayToFile(path.toFile(), buffer);

        } catch (Exception e) {
            Logger.error(e);
        }
        String url = "/upload/" + fileName;
        bean.setLogo(url);
        return bean.getLogo();
    }

    public void attachShim(ChannelBean bean) {
        List<ResBean> list = ResBusiness.getInstance().listByTags(bean.getManager_id(), -1, Collections.singletonList(1001200));
        ResBean rbean = null;
        Optional<ResBean> first = list.stream().filter(x -> x.getSystem() == 0).findFirst();
        if (first.isPresent()) {
            rbean = first.get();
        } else {
            first = list.stream().filter(x -> x.getSystem() == 1).findFirst();
        }
        if (first.isPresent()) {
            rbean = first.get();
            rbean.setDuration(rbean.getFormat_duration());
        }
        if (rbean == null) {
            rbean = ResBusiness.getInstance().load(2);
        }
        bean.setShim(rbean);
    }

    private static ChannelBusiness channelBusiness = null;

    private ChannelBusiness() {
    }

    public synchronized static ChannelBusiness getInstance() {
        if (channelBusiness == null) {
            channelBusiness = new ChannelBusiness();
        }

        return channelBusiness;
    }

}

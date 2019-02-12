package hndt.radiolibs.biz;

import hndt.radiolibs.bean.*;
import hndt.radiolibs.util.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * 钟型Business
 * <p>
 *
 * @author Hystar
 * @date 2017/7/20
 */
public class ClockBusiness {
    private static ClockBusiness clockBusiness = null;

    private ClockBusiness() {
    }

    public synchronized static ClockBusiness getInstance() {
        if (clockBusiness == null) {
            clockBusiness = new ClockBusiness();
        }
        return clockBusiness;
    }

    public PageBean pagination(Long managerId, String keyword, Long channelId, Long duration, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sql = SQL.of("SELECT id,manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock")
                .and("manager_id=", managerId)
                .and("name LIKE", keyword)
                .and("channel_id=", channelId)
                .and("duration=", duration)
                .append("ORDER BY id DESC")
                .limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<ClockBean> clockBeanList = DBTool.list(ClockBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(clockBeanList);
        return pageBean;
    }

    public List<ClockBean> listByIds(List<Long> ids, Long managerId) {
        List<ClockBean> clockBeanList = Collections.EMPTY_LIST;
        if (Utils.isNotEmpty(ids)) {
            SQL sql = SQL.of("SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock").and("id IN", SQL.toInString(ids)).and("status=", 1).and("manager_id=", managerId);
            clockBeanList = DBTool.list(ClockBean.class, sql.sql(), sql.params());
        }
        return clockBeanList;
    }

    public Map<Long, ClockBean> mapByIds(List<Long> ids) {
        Map<Long, ClockBean> map = new HashMap<>(16);
        if (ids != null && !ids.isEmpty()) {
            List<ClockBean> list = DBTool.list(ClockBean.class, "SELECT * FROM clock WHERE id IN(" + SQL.toInString(ids) + ")");
            for (ClockBean rb : list) {
                map.put(rb.getId(), rb);
            }
        }
        return map;
    }

    public Map<Long, ClockBean> mapByIds(List<Long> ids, Long managerId) {
        Map<Long, ClockBean> map = new HashMap<>(16);
        if (ids != null && !ids.isEmpty()) {
            SQL sql = SQL.of("SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock").and("id IN", SQL.toInString(ids)).and("status=", 1).and("manager_id=", managerId);
            List<ClockBean> list = DBTool.list(ClockBean.class, sql.sql(), sql.params());
            for (ClockBean rb : list) {
                map.put(rb.getId(), rb);
            }
        }
        return map;
    }

    public List<ClockBean> list() {
        return DBTool.list(ClockBean.class, "SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock WHERE status=1");
    }

    public List<ClockBean> list(long managerId) {
        return DBTool.list(ClockBean.class, "SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock WHERE manager_id=? AND status=1", managerId);
    }

    public List<ClockBean> list(String channelId) {
        return DBTool.list(ClockBean.class, "SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock WHERE channel_id=? AND status=1", channelId);
    }

    public List<ClockBean> listByOrder(List<Long> clockIds, long channelId, Timestamp nowDate) {
        List<ClockBean> result = new ArrayList<>();
        List<RuntimeBean> runtimeBeanList = DBTool.list(RuntimeBean.class, "SELECT DISTINCT clock_id FROM runtime WHERE channel_id=? AND date_format(createtime,'%Y-%m-%d')=?", channelId, Utils.format(nowDate.toLocalDateTime(), Utils.DATEFORMAT3));
        List<Long> ids = new ArrayList<>();
        runtimeBeanList.forEach(runtimeBean -> ids.add(runtimeBean.getClock_id()));
        Map<Long, ClockBean> clockBeanMap = ClockBusiness.getInstance().mapByIds(ids, null);
        clockIds.forEach(x -> result.add(clockBeanMap.get(x)));
        return result;
    }

    public List<ClockBean> list(Long managerId, String keyword, Long channelId, Long duration) {
        SQL sql = SQL.of("SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock")
                .and("manager_id=", managerId)
                .and("name LIKE", keyword)
                .and("channel_id=", channelId)
                .and("duration=", duration)
                .append("AND status=1");
        return DBTool.list(ClockBean.class, sql.sql(), sql.params());
    }

    public List<ClockBean> listExclusiveIds(List<Long> ids, Long managerId) {
        if (ids.size() == 0) {
            return DBTool.list(ClockBean.class, "SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock WHERE manager_id=? AND status=1", managerId);
        } else {
            return DBTool.list(ClockBean.class, "SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock WHERE manager_id=? AND status=1 AND id NOT IN(" + SQL.toInString(ids) + ")", managerId);
        }
    }

    public int delete(ClockBean clockBean) {
        int r = DBTool.update("DELETE FROM clock WHERE id=?", clockBean.getId());
        return r;
    }

    public void attachChannelBeanList(ClockBean bean) {
        bean.setChannelList(ChannelBusiness.getInstance().list(bean.getManager_id()));
    }

    public void attachTypedBeanList(ClockBean bean) {
        List<Long> typedIds = GSON.toList(bean.getTyped_ids(), Utils.typeLong);
        List<TypedBean> list = new ArrayList<>();
        typedIds.forEach(x -> {
            list.add(TypedBusiness.getInstance().load(x));
        });
        bean.setTypedList(list);
    }

    public int copy(ClockBean clockBean) {
        int r = 0;
        if (clockBean == null) {
            return r;
        }
        long id = DBTool.insert("INSERT INTO clock(manager_id, channel_id, name, duration, status, createtime, typed_ids) VALUES(?,?,?,?,?,?,?)", clockBean.getManager_id(), clockBean.getChannel_id(), clockBean.getName(), clockBean.getDuration(), clockBean.getStatus(), clockBean.getCreatetime(), clockBean.getTyped_ids());
        if (id > 0) {
            clockBean.setId(id);
            r = 1;
        }
        return r;
    }

    public int clean(ClockBean clockBean) {
        int r = DBTool.update("UPDATE clock SET name=?, duration=?, status=?, createtime=?, typed_ids=? WHERE id=?", null, 0, 1, null, null, clockBean.getId());
        return r;
    }

    public int save(ClockBean clockBean) {
        int r = 0;
        if (clockBean.getId() == null || clockBean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO clock(channel_id, name, duration, status, createtime, typed_ids, manager_id) VALUES(?,?,?,?,?,?,?)", clockBean.getChannel_id(), clockBean.getName(), clockBean.getDuration(), clockBean.getStatus(), clockBean.getCreatetime(), clockBean.getTyped_ids(), clockBean.getManager_id());
            if (id > 0) {
                r = 1;
                clockBean.setId(id);
            }
        } else {// 更新操作
            r = DBTool.update("UPDATE clock SET channel_id=?, name=?, duration=?, status=?, createtime=?, typed_ids=? WHERE id=?", clockBean.getChannel_id(), clockBean.getName(), clockBean.getDuration(), clockBean.getStatus(), clockBean.getCreatetime(), clockBean.getTyped_ids(), clockBean.getId());
        }
        return r;
    }

    public ClockBean load(long id) {
        ClockBean clockBean = DBTool.find(ClockBean.class, "SELECT id, manager_id, channel_id, name, duration, status, createtime, typed_ids FROM clock WHERE id=?", id);
        return clockBean;
    }

    public int toggleStatus(ClockBean bean) {
        int r = DBTool.update("UPDATE clock SET status=? WHERE id=?", bean.getStatus() == 1 ? 0 : 1, bean.getId());
        return r;
    }

}

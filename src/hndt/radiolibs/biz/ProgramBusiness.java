package hndt.radiolibs.biz;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.bean.ClockBean;
import hndt.radiolibs.bean.ProgramBean;
import hndt.radiolibs.util.*;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 节目单Business
 *
 * @author
 * @date 2017/7/19
 */
public class ProgramBusiness {

    private static ProgramBusiness programBusiness = null;

    private ProgramBusiness() {
    }

    public PageBean pagination(Long managerId, Long channelId, String keyword, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sql = SQL.of("SELECT * FROM program").and("manager_id=", managerId).and("channel_id=", channelId).and("name LIKE", keyword).append("ORDER BY channel_id,id DESC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<ProgramBean> programBeanList = DBTool.list(ProgramBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(programBeanList);
        return pageBean;
    }

    public void attachOrderClockIdsList(ProgramBean bean) {
        if (bean != null && Utils.isNotEmpty(bean.getClock_ids())) {
            List<Long> ids = GSON.toList(bean.getClock_ids(), Utils.typeLong);
            bean.setClockIds(ids);
        }
        if (bean != null && Utils.isNotEmpty(bean.getClockIds())) {
            Map<Long, ClockBean> clockMap = ClockBusiness.getInstance().mapByIds(new ArrayList<>(bean.getClockIds()), bean.getManager_id());
            // 根据clockIds排序后的List
            List<ClockBean> orderList = new ArrayList<>();
            bean.getClockIds().forEach(x -> orderList.add(clockMap.get(x)));
            orderList.removeIf(x -> x == null);
            // 设置每个钟型的预计播出时间
            if (bean.getStarttime() != null) {
                Timestamp time = null;
                // 存放上一个钟型的播放时长
                int preDuration = 0;
                for (ClockBean clockItem : orderList) {
                    if (orderList.indexOf(clockItem) == 0) {
                        time = bean.getStarttime();
                        preDuration = clockItem.getDuration();
                    } else {// 如果该钟型不是排列在第一位，则需要获取上一个钟型的播放时长
                        time = new Timestamp(time.getTime() + preDuration * 1000 * 60);
                        preDuration = clockItem.getDuration();
                    }
                    //格式化日期，只获取时间
                    time.toLocalDateTime().format(Utils.DATEFORMAT7);
                    clockItem.setStarttime(time.toLocalDateTime().format(Utils.DATEFORMAT7));
                }
            }
            bean.setClockBeanList(orderList);
        }
    }

    public void attachChannelBeanList(ProgramBean bean) {
        List<ChannelBean> channelBeanList = ChannelBusiness.getInstance().list(bean.getManager_id());
        bean.setChannelBeanList(channelBeanList);
    }

    public int save(ProgramBean bean) {
        int r = 0;
        if (bean.getId() == null || bean.getId() == 0) {// 添加操作
            long id = DBTool.insert("INSERT INTO program(channel_id, manager_id, name, description, days, startdate,enddate, starttime, endtime, clock_ids,status) VALUE(?,?,?,?,?,?,?,?,?,?,?)", bean.getChannel_id(), bean.getManager_id(), bean.getName(), bean.getDescription(), bean.getDays(), bean.getStartdate(), bean.getEnddate(), bean.getStarttime(), bean.getEndtime(), bean.getClock_ids(), bean.getStatus());
            if (id > 0) {
                bean.setId(id);
                r = 1;
            }
        } else {// 更新操作
            r = DBTool.update("UPDATE program SET channel_id=?, manager_id=?, name=?, description=?, days=?, startdate=?,enddate=?,starttime=?, endtime=?, clock_ids=?,status=? WHERE id=?", bean.getChannel_id(), bean.getManager_id(), bean.getName(), bean.getDescription(), bean.getDays(), bean.getStartdate(), bean.getEnddate(), bean.getStarttime(), bean.getEndtime(), bean.getClock_ids(), bean.getStatus(), bean.getId());
        }
        return r;
    }

    public ProgramBean load(long id) {
        ProgramBean programBean = DBTool.find(ProgramBean.class, "SELECT * FROM program WHERE id=?", id);
        if (programBean != null) {
            programBean.setLocalStartTime(programBean.getStarttime().toLocalDateTime().toLocalTime());
            programBean.setLocalEndTime(programBean.getEndtime().toLocalDateTime().toLocalTime());
        }
        return programBean;
    }

    public ProgramBean load(Long manager_id, Long channel_id, int week) {
        return load(manager_id, channel_id, week, null);
    }

    public ProgramBean load(Long manager_id, Long channel_id, int week, LocalDate date) {
        SQL sql = SQL.of("SELECT * FROM program").and("status=", 1).and("manager_id=", manager_id).and("channel_id=", channel_id).and("days LIKE", String.valueOf(week)).append("ORDER BY id DESC");
        if (date == null) {
            date = LocalDate.now();
        }
        List<ProgramBean> result = new ArrayList<>();
        List<ProgramBean> list = DBTool.list(ProgramBean.class, sql.sql(), sql.params());
        for (ProgramBean bean : list) {
            if (bean.getStartdate() == null && bean.getEnddate() == null) {
                result.add(bean);
            } else if (bean.getStarttime() != null && bean.getEnddate() == null) {
                if (date.isEqual(LocalDate.from(bean.getStartdate().toLocalDateTime()))) {
                    result.add(bean);
                }
            } else if (bean.getEnddate() != null && bean.getStartdate() == null) {
                if (date.isBefore(LocalDate.from(bean.getEnddate().toLocalDateTime()))) {
                    result.add(bean);
                }
            } else if (bean.getEnddate() != null && bean.getStartdate() != null) {
                if (date.isBefore(LocalDate.from(bean.getEnddate().toLocalDateTime())) && date.isAfter(LocalDate.from(bean.getStartdate().toLocalDateTime()))) {
                    result.add(bean);
                }
            }
        }
        for (ProgramBean bean : list) {
            if (bean.getStartdate() != null && bean.getEnddate() == null && date.isAfter(LocalDate.from(bean.getStartdate().toLocalDateTime()))) {
                result.add(bean);
            }
        }
        for (ProgramBean bean : list) {
            if (bean.getStartdate() == null && bean.getEnddate() != null && date.isBefore(LocalDate.from(bean.getEnddate().toLocalDateTime()))) {
                result.add(bean);
            }
        }
        for (ProgramBean bean : list) {
            if (bean.getStartdate() == null && bean.getEnddate() == null) {
                result.add(bean);
            }
        }
        result.forEach(bean -> {
            bean.setLocalStartTime(bean.getStarttime().toLocalDateTime().toLocalTime());
            bean.setLocalEndTime(bean.getEndtime().toLocalDateTime().toLocalTime());
        });
        return result.size() > 0 ? result.get(0) : null;
    }

    public int toggleStatus(ProgramBean bean) {
        int r = DBTool.update("UPDATE program SET status=? WHERE id=?", bean.getStatus() == 1 ? 0 : 1, bean.getId());
        return r;
    }

    public int delete(ProgramBean bean) {
        int r = DBTool.update("DELETE FROM program WHERE id=?", bean.getId());
        return r;
    }

    /**
     * 将传入的String类型的时间戳转换成Timestamp类型的日期
     *
     * @param dateValue
     * @return
     */
    public Timestamp StringToTimestamp(String dateValue) {
        Timestamp dateTime = null;
        if (StringUtils.isNotEmpty(dateValue)) {
            Date date = new Date(Long.parseLong(dateValue));
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDate.now().atTime(0, 0, 0); //将时间重置为0，只要日期
            dateTime = Timestamp.valueOf(localDateTime);
        }
        return dateTime;
    }

    public synchronized static ProgramBusiness getInstance() {
        if (programBusiness == null) {
            programBusiness = new ProgramBusiness();
        }
        return programBusiness;
    }

}

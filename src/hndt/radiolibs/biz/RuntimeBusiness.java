package hndt.radiolibs.biz;

import hndt.radiolibs.bean.*;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.SQL;
import hndt.radiolibs.util.Utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hystar
 * @date 2017/8/11
 */
public class RuntimeBusiness {

    public List<RuntimeBean> list(Long managerId, Long channelId, Long programId, Long clockId, Timestamp createTime) {
        createTime = (Timestamp.valueOf(createTime.toLocalDateTime().toLocalDate().atTime(0, 0, 0)));
        SQL sql = SQL.of("SELECT * FROM runtime").and("manager_id=", managerId).and("channel_id=", channelId).and("program_id=", programId).and("clock_id=", clockId).and("createtime=", createTime).append("ORDER BY sequence");
        List<RuntimeBean> runtimeBeanList = DBTool.list(RuntimeBean.class, sql.sql(), sql.params());
        return runtimeBeanList;
    }

    /**
     * createTime格式：2017-10-24 00:00:00
     */
    public List<RuntimeBean> list(Long channelId, LocalDate createDate) {
        SQL sql = SQL.of("SELECT * FROM runtime").and("channel_id=", channelId).and("createtime=", createDate).append("ORDER BY sequence");
        List<RuntimeBean> runtimeBeanList = DBTool.list(RuntimeBean.class, sql.sql(), sql.params());
        return runtimeBeanList;
    }

    public boolean none(Long channelId, LocalDate createDate) {
        SQL sql = SQL.of("SELECT * FROM runtime").and("channel_id=", channelId).and("createtime=", createDate).append("ORDER BY sequence").append("LIMIT 0,1");
        RuntimeBean rbean = DBTool.find(RuntimeBean.class, sql.sql(), sql.params());
        return rbean == null;
    }

    public RuntimeBean findByPlaceholder(Long channel_id, Long clock_id, Long typed_id) {
        RuntimeBean bean = null;
        SQL sql = SQL.of("SELECT * FROM runtime WHERE playdate is NULL").and("channel_id=", channel_id).and("clock_id=", clock_id).and("typed_id=", typed_id).and("placeholder=", 1);
        List<RuntimeBean> runtimeBeanList = DBTool.list(RuntimeBean.class, sql.sql(), sql.params());
        if (runtimeBeanList.size() > 0) {
            bean = runtimeBeanList.get(0);
        }
        return bean;
    }

    public int updatePlaceholder(Long runtime_id, int duration, Long res_id) {
        int r = DBTool.update("UPDATE runtime SET placeholder=0,duration=?,res_id=? WHERE id=?", duration, res_id, runtime_id);
        return r;
    }

    /**
     * 获取上一条数据
     *
     * @return
     */
    public RuntimeBean getPreRuntimeById(Long managerId, Long channelId, Long programId, Long clockId, Timestamp createTime, Long sequence) {
        return DBTool.find(RuntimeBean.class, "SELECT id, sequence FROM runtime WHERE manager_id=? AND channel_id=? AND program_id=? AND clock_id=? AND createtime=? AND sequence<? ORDER BY sequence DESC LIMIT 0,1",
                managerId, channelId, programId, clockId, createTime, sequence);
    }

    /**
     * 获取下一条数据
     *
     * @return
     */
    public RuntimeBean getNextRuntimeById(Long managerId, Long channelId, Long programId, Long clockId, Timestamp createTime, Long sequence) {
        return DBTool.find(RuntimeBean.class, "SELECT id, sequence FROM runtime WHERE manager_id=? AND channel_id=? AND program_id=? AND clock_id=? AND createtime=? AND sequence>? ORDER BY sequence ASC LIMIT 0,1",
                managerId, channelId, programId, clockId, createTime, sequence);
    }

    /**
     * 通过id修改该信息的sequence属性
     *
     * @param runtimeId
     * @param sequence
     * @return
     */
    public int updateSequenceById(Long runtimeId, Long sequence) {
        int r = DBTool.update("UPDATE runtime SET sequence=? WHERE id=?", sequence, runtimeId);
        return r;
    }

    /**
     * 通过ID移除一条runtime表中的数据
     *
     * @param id
     * @return
     */
    public int removeRuntimeById(long id) {
        int r = DBTool.update("DELETE FROM runtime WHERE id=?", id);
        return r;
    }

    public int save(RuntimeBean bean) {
        bean.setCreatetime(Timestamp.valueOf(bean.getCreatetime().toLocalDateTime().toLocalDate().atTime(0, 0, 0)));
        int r = 0;
        if (bean.getId() == null || bean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO runtime(sequence, manager_id, channel_id, program_id, clock_id, res_id, typed_id, placeholder,duration,unitary,createtime) VALUES(?,?,?,?,?,?,?,?,?,?,?)", 0, bean.getManager_id(), bean.getChannel_id(), bean.getProgram_id(), bean.getClock_id(), bean.getRes_id(), bean.getTyped_id(), bean.getPlaceholder(), bean.getDuration(), bean.getUnitary(), bean.getCreatetime());
            if (id > 0) {
                bean.setId(id);
                bean.setSequence(id);
                r = DBTool.update("UPDATE runtime SET sequence=? WHERE id=?", bean.getSequence(), bean.getId());
            }
        } else {
            r = DBTool.update("UPDATE runtime SET sequence=?,manager_id=?,channel_id=?,program_id=?,clock_id=?,res_id=?,typed_id=?,placeholder=?,duration=?,unitary=?,playdate=?,createtime=? WHERE id=?", bean.getSequence(), bean.getManager_id(), bean.getChannel_id(), bean.getProgram_id(), bean.getClock_id(), bean.getRes_id(), bean.getTyped_id(), bean.getPlaceholder(), bean.getDuration(), bean.getUnitary(), bean.getPlaydate(), bean.getCreatetime(), bean.getId());
        }
        return r;
    }

    /**
     * 获取指定时间到今天 的runtime数据
     *
     * @param channel_id
     * @param typed_id
     * @param fromDate
     * @return
     */
    public List<Long> listResIds(long channel_id, long typed_id, Timestamp fromDate) {
        List<RuntimeBean> runtimeBeanList = DBTool.list(RuntimeBean.class, "SELECT * FROM runtime WHERE channel_id=? AND typed_id=? AND playdate>?", channel_id, typed_id, fromDate);
        List<Long> result = new ArrayList<>();
        for (RuntimeBean runtimeBean : runtimeBeanList) {
            result.add(runtimeBean.getId());
        }
        return result;
    }

    public List<RuntimeBean> listByIds(List<Long> ids) {
        return DBTool.list(RuntimeBean.class, "SELECT * FROM runtime WHERE res_id IN(" + SQL.toInString(ids) + ")");
    }

    public RuntimeBean load(Long res_id, Long clock_id) {
        return DBTool.find(RuntimeBean.class, "SELECT * FROM runtime WHERE res_id=? AND clock_id=?", res_id, clock_id);
    }

    public RuntimeBean load(Long channel_id, Long clock_id, Long res_id) {
        return DBTool.find(RuntimeBean.class, "SELECT * FROM runtime WHERE channel_id=? AND clock_id=? AND res_id=?", channel_id, clock_id, res_id);
    }

    public RuntimeBean load(Long id) {
        return DBTool.find(RuntimeBean.class, "SELECT * FROM runtime WHERE id=?", id);
    }

    /**
     * 获取当前频率下，当前规则下，的最后一个资源文件信息
     * 如果最后一个资源文件是垫片，则跳过获取上一个资源文件，直到获取的不是垫片
     *
     * @param channel_id
     * @param typed_Id
     * @param i          获取资源文件的开始位置
     * @return
     */
    public ResBean loadLastRes(Long channel_id, Long typed_Id, int i) {
        List<RuntimeBean> list = DBTool.list(RuntimeBean.class, "SELECT * FROM runtime WHERE channel_id=? AND typed_id=? ORDER BY id DESC limit " + i + ",1", channel_id, typed_Id);
        if (list.size() == 0) {
            return null;
        }
        ResBean resBean = ResBusiness.getInstance().load(list.get(0).getRes_id());
        return resBean;
    }

    /**
     * 生成runtime表中的数据
     */
    public void generate(long channel_id, Timestamp date) {
        //设置数据的创建时间，如果为空则生成当天的数据
        LocalDateTime now = date == null ? LocalDate.now().atTime(0, 0, 0) : date.toLocalDateTime().toLocalDate().atTime(0, 0, 0);

        //获取星期几
        int dayOfWeek = now.getDayOfWeek().getValue();
        ChannelBean channelBean = ChannelBusiness.getInstance().load(channel_id);
        Long managerId = channelBean.getManager_id();
        ProgramBean programBean = ProgramBusiness.getInstance().load(managerId, channel_id, dayOfWeek, now.toLocalDate());
        ProgramBusiness.getInstance().attachOrderClockIdsList(programBean);
        List<ClockBean> clocks = programBean == null ? new ArrayList<>() : programBean.getClockBeanList();

        for (ClockBean clock : clocks) {
            //填充规则
            ClockBusiness.getInstance().attachTypedBeanList(clock);
            List<TypedBean> typedList = clock.getTypedList();
            typedList.removeIf(x -> x == null);
            for (TypedBean typed : typedList) {
                TypedBusiness.getInstance().attachTagList(typed);
                List<ResBean> result = resByTyped(managerId, channel_id, typed, now);
                clock.getResList().addAll(result);
                for (ResBean rb : result) {
                    // 如果当前频率开启了“审核”功能，并且资源文件“审核未通过”，则不生成记录
                    if (EnumValue.OpenAudit.OPEN.equals(channelBean.getAudit())) {
                        if (EnumValue.AuditStatus.NOT_PASS.equals(rb.getAudit_status())) {
                            break;
                        }
                    }
                    Long res_id = rb.getId();
                    int duration = rb.getFormat_duration().intValue();
                    if (typed.getPlaceholder() == 1) {
                        res_id = 0L;
                        duration = typed.getDuration();
                    }
                    save(now, res_id, programBean.getId(), managerId, channel_id, clock.getId(), typed.getId(), typed.getPlaceholder(), duration, typed.getUnitary());
                }
            }
        }
    }

    public void clear(long channel_id, LocalDateTime now) {
        String nowday = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        DBTool.update("DELETE FROM runtime WHERE channel_id=? AND createtime=?", channel_id, nowday);
    }

    /**
     * 清除runtime表中未播的记录
     *
     * @param now
     */
    public void clear(LocalDateTime now) {
        DBTool.update("DELETE FROM runtime WHERE playdate IS NULL AND createtime <=?", Timestamp.valueOf(now));
    }

    /**
     * 预留规则选取数量只能是1
     *
     * @param manager_id
     * @param channel_id
     * @param typed
     * @param now
     * @return
     */
    public List<ResBean> resByTyped(Long manager_id, Long channel_id, TypedBean typed, LocalDateTime now) {
        List<ResBean> result = new ArrayList<>();
        TypedBusiness.getInstance().attachTagList(typed);
        if (typed.getOrdinal() == EnumValue.Ordinal.ORDER.getCode()) { //顺序
            List<ResBean> resList = ResBusiness.getInstance().listByTags(manager_id, typed.getRes_category(), typed.getTagList(), "ORDER BY sequence ASC");
            ResBean lastResBean = RuntimeBusiness.getInstance().loadLastRes(channel_id, typed.getId(), 0);
            int start = 0;
            //runtime表中存在该规则的记录
            if (lastResBean != null) {
                start = resList.indexOf(lastResBean) + 1;
            }
            //表示start的位置在resList列表中是最后一个，则从第一个开始循环
            if (start == resList.size()) {
                start = 0;
            }
            result = subListFrom(resList, start, typed.getAmount());
        } else if (typed.getOrdinal() == EnumValue.Ordinal.DESC.getCode()) {//倒序
            List<ResBean> resList = ResBusiness.getInstance().listByTags(manager_id, typed.getRes_category(), typed.getTagList(), "ORDER BY sequence DESC");
            ResBean lastResBean = RuntimeBusiness.getInstance().loadLastRes(channel_id, typed.getId(), 0);
            int start = 0;
            if (lastResBean != null) {
                start = resList.indexOf(lastResBean) + 1;
            }
            if (start == resList.size() - 1) {
                start = 0;
            }
            result = subListFrom(resList, start, typed.getAmount());
        } else if (typed.getOrdinal() == EnumValue.Ordinal.RANDOM.getCode()) {//随机
            List<ResBean> resList = ResBusiness.getInstance().listByTags(manager_id, typed.getRes_category(), typed.getTagList());
            Timestamp fromDate = Timestamp.valueOf(now.minusDays(typed.getIntervals()));// intervals天前的日期
            List<Long> exclusiveResIdList = RuntimeBusiness.getInstance().listResIds(channel_id, typed.getId(), fromDate);
            result = subListFrom(resList, exclusiveResIdList, typed.getAmount());
        } else if (typed.getOrdinal() == EnumValue.Ordinal.REPEATE.getCode()) {//重播
            ResBean lastResBean = RuntimeBusiness.getInstance().loadLastRes(channel_id, typed.getId(), 0);
            result.add(lastResBean);
        }

        return result;
    }

    void save(LocalDateTime createtime, Long res_id, Long program_id, Long manager_id, Long channel_id, Long clock_id, Long type_id, int placeholder, int duration, int unitary) {
        RuntimeBean bean = new RuntimeBean();
        bean.setRes_id(res_id);
        bean.setProgram_id(program_id);
        bean.setManager_id(manager_id);
        bean.setChannel_id(channel_id);
        bean.setClock_id(clock_id);
        bean.setTyped_id(type_id);
        bean.setPlaceholder(placeholder);
        bean.setDuration(duration);
        bean.setUnitary(unitary);
        bean.setCreatetime(Timestamp.valueOf(createtime));
        save(bean);
    }

    /**
     * 从list的第start个位置取amount条元素。如果遇到末尾，从头开始
     *
     * @param sources
     * @param start
     * @param amount
     * @return
     */
    List<ResBean> subListFrom(List<ResBean> sources, int start, int amount) {
        List<ResBean> result = new ArrayList<>();

        if (sources.size() == 0) {
            return result;
        }

        if (sources.size() < amount) {
            result.addAll(sources);
        } else {
            if (start + amount > sources.size() - 1) {
                List<ResBean> listBefore = sources.subList(start, sources.size());
                for (ResBean res1 : listBefore) {
                    result.add(res1);
                }
                List<ResBean> listEnd = sources.subList(0, amount - listBefore.size());
                for (ResBean res2 : listEnd) {
                    result.add(res2);
                }
            } else {
                List<ResBean> list = sources.subList(start, start + amount);
                for (ResBean resBean : list) {
                    result.add(resBean);
                }
            }
        }

        result.removeIf(x -> x == null);

        return result;
    }

    /**
     * 从list排除掉exclusiveResIdList包含的ID，然后随机取amount条元素
     *
     * @param sources
     * @param exclusiveResIdList
     * @param amount
     * @return
     */
    List<ResBean> subListFrom(List<ResBean> sources, List<Long> exclusiveResIdList, int amount) {
        sources.removeIf(x -> exclusiveResIdList.contains(x.getId()));
        List<ResBean> result = new ArrayList<>();
        if (!sources.isEmpty()) {
            for (int i = 0; i < amount; i++) {
                if (sources.size() < amount) {
                    amount = sources.size();
                    i = 0;
                }
                int random = Utils.getRandomInt(sources.size());
                result.add(sources.get(random));
                sources.remove(random);
            }
        }
        return result;
    }

    /**
     * 获取列表时长与钟型时长的差值
     *
     * @param clock
     * @param resBeanList
     * @return
     */
    public int timeDifference(ClockBean clock, List<ResBean> resBeanList) {
        int count = 0;
        for (ResBean resBean : resBeanList) {
            count += resBean.getFormat_duration();
        }
        return clock.getDuration() * 60 - count;
    }

    /**
     * 填充runtime表中的时间属性
     * playdate（播放日期）
     */
    public void updatePlayDate(String runtime_id) {
        // 通过参数获取runtime信息
        String nowday = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String nowtime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        int r = DBTool.update("UPDATE runtime SET playdate=?,playtime=? WHERE id=? AND playdate IS NULL AND playtime IS NULL", nowday, nowtime, runtime_id);
    }


    public Long currentPlayItem(Long channel_id) {
        List<RuntimeBean> list = DBTool.list(RuntimeBean.class, "select id,playdate from runtime where channel_id=? and createtime=? order by id desc", channel_id, Utils.nowDateString() + " 00:00:00");
        RuntimeBean current = null;
        for (RuntimeBean bean : list) {
            if (bean.getPlaydate() != null) {
                current = list.get(list.indexOf(bean) - 1);
                break;
            }
        }
        return current == null ? 0L : current.getId();
    }

    private static RuntimeBusiness runtimeBusiness = null;

    public RuntimeBusiness() {
    }

    public static synchronized RuntimeBusiness getInstance() {
        if (runtimeBusiness == null) {
            runtimeBusiness = new RuntimeBusiness();
        }
        return runtimeBusiness;
    }
}

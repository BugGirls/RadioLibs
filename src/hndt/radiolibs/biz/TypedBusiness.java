package hndt.radiolibs.biz;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.bean.TagBean;
import hndt.radiolibs.bean.TypedBean;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.PageBean;
import hndt.radiolibs.util.SQL;
import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.Typed;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Hystar
 * @date 2017/7/21
 */
public class TypedBusiness {

    private static TypedBusiness typedBusiness = null;

    private TypedBusiness() {
    }

    public PageBean pagination(Long managerId, String keyword, Long channelId, Long resCategory, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sql = SQL.of("SELECT * FROM typed").and("manager_id=", managerId).or("res_category LIKE", keyword, "ordinal LIKE", keyword).and("channel_id=", channelId).and("res_category=", resCategory)
                .append("ORDER BY id DESC")
                .limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<TypedBean> typedBeanList = DBTool.list(TypedBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(typedBeanList);
        return pageBean;
    }

    public int save(TypedBean typedBean) {
        int r = 0;
        // 添加
        if (typedBean.getId() == null || typedBean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO typed(manager_id, channel_id, res_category, status, ordinal, intervals, createtime, amount, special,placeholder,duration,unitary,color) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", typedBean.getManager_id(), typedBean.getChannel_id(), typedBean.getRes_category(), typedBean.getStatus(), typedBean.getOrdinal(), typedBean.getIntervals(), typedBean.getCreatetime(), typedBean.getAmount(), typedBean.getSpecial(), typedBean.getPlaceholder(), typedBean.getDuration(), typedBean.getUnitary(),typedBean.getColor());
            if (id > 0) {
                typedBean.setId(id);
                r = 1;
            }
        } else {// 更新
            r = DBTool.update("UPDATE typed SET manager_id=?, channel_id=?, res_category=?, status=?, ordinal=?, intervals=?, listing=?, createtime=?, amount=?, special=?,placeholder=?,duration=?,unitary=?,color=? WHERE id=?", typedBean.getManager_id(), typedBean.getChannel_id(), typedBean.getRes_category(), typedBean.getStatus(), typedBean.getOrdinal(), typedBean.getIntervals(), typedBean.getListing(), typedBean.getCreatetime(), typedBean.getAmount(), typedBean.getSpecial(), typedBean.getPlaceholder(), typedBean.getDuration(), typedBean.getUnitary(),typedBean.getColor(), typedBean.getId());
        }

        return r;
    }

    public int saveTypeTags(TypedBean typedBean) {
        int r = 0;
        if (typedBean.getId() == null || typedBean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO typed(res_tags) VALUES(?)", typedBean.getRes_tags());
            if (id > 0) {
                typedBean.setId(id);
                r = 1;
            }
        } else {
            r = DBTool.update("UPDATE typed SET res_tags=? WHERE id=?", typedBean.getRes_tags(), typedBean.getId());
        }
        return r;
    }

    public int delete(TypedBean typedBean) {
        int r = DBTool.update("DELETE FROM typed WHERE id=?", typedBean.getId());
        return r;
    }

    public int copy(TypedBean typedBean) {
        int r = 0;
        typedBean.setCreatetime(Timestamp.valueOf(LocalDateTime.now()));
        long id = DBTool.insert("INSERT INTO typed(manager_id, channel_id, res_category, status, ordinal, intervals, listing, createtime, amount, res_tags, special,color) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", typedBean.getManager_id(), typedBean.getChannel_id(), typedBean.getRes_category(), typedBean.getStatus(), typedBean.getOrdinal(), typedBean.getIntervals(), typedBean.getListing(), typedBean.getCreatetime(), typedBean.getAmount(), typedBean.getRes_tags(), typedBean.getSpecial(),typedBean.getColor());
        if (id > 0) {
            typedBean.setId(id);
            r = 1;
        }
        return r;
    }

    public TypedBean load(long id) {
        TypedBean typedBean = DBTool.find(TypedBean.class, "SELECT * FROM typed WHERE id=?", id);
        return typedBean;
    }

    public Map<Long, TypedBean> mapByIds(List<Long> ids) {
        List<TypedBean> list = TypedBusiness.getInstance().listByIds(ids);
        Map<Long, TypedBean> map = new HashMap<>();
        list.stream().forEach(x -> map.put(x.getId(), x));
        return map;
    }

    public List<TypedBean> listByIds(List<Long> ids) {
        List<TypedBean> typedBeanList = Collections.EMPTY_LIST;
        if (Utils.isNotEmpty(ids)) {
            typedBeanList = DBTool.list(TypedBean.class, "SELECT * FROM typed WHERE id IN(" + SQL.toInString(ids) + ") AND status=1");
        }
        return typedBeanList;
    }

    /**
     * 获取数据库标签并存入List列表中
     *
     * @param typedBean
     */
    public void attachTagList(TypedBean typedBean) {
        if (typedBean != null && StringUtils.isNotEmpty(typedBean.getRes_tags())) {
            List<Integer> tags = new ArrayList<>();
            if (StringUtils.isNotEmpty(typedBean.getRes_tags())) {
                String[] sp = typedBean.getRes_tags().split(",");
                for (int i = 0; i < sp.length; i++) {
                    tags.add(Integer.parseInt(sp[i]));
                }
            }
            typedBean.setTagList(tags);
        }
    }

    public void attachTagBeanList(TypedBean typedBean) {
        List<TagBean> tagBeanList = Collections.EMPTY_LIST;
        if (typedBean != null && Utils.isNotEmpty(typedBean.getRes_tags())) {
            tagBeanList = TagBusiness.getInstance().listByCodes(Utils.asList(typedBean.getRes_tags()));
        }
        typedBean.setTagBeanList(tagBeanList);
    }

    public List<TypedBean> list(Long managerId) {
        return DBTool.list(TypedBean.class, "SELECT * FROM typed WHERE manager_id=? AND status=1", managerId);
    }

    public List<TypedBean> list(Long managerId, Long channelId, Long resCategory) {
        SQL sql = SQL.of("SELECT * FROM typed").and("manager_id=", managerId).and("channel_id=", channelId).and("res_category=", resCategory).append("AND status=1");
        return DBTool.list(TypedBean.class, sql.sql(), sql.params());
    }

    public List<TypedBean> listExclusiveIds(List<Long> ids, Long managerId) {
        if (ids.size() == 0) {
            return DBTool.list(TypedBean.class, "SELECT * FROM typed WHERE manager_id=? AND status=1 AND special=0", managerId);
        } else {
            return DBTool.list(TypedBean.class, "SELECT * FROM typed WHERE manager_id=? AND status=1 AND special=0 AND id NOT IN(" + SQL.toInString(ids) + ")", managerId);
        }
    }

    /**
     * 当保存一条频率时，创建一条默认的规则：标签是垫片，顺序策略是0，选取数量是9。
     */
    public void generateDefaultTyped(Long managerId, Long channelId) {
        TypedBean typedBean = new TypedBean();
        typedBean.setManager_id(managerId);
        typedBean.setChannel_id(channelId);
        typedBean.setRes_category(EnumValue.Category.SONG.getCode());
        typedBean.setStatus(EnumValue.Status.EFFECTIVE.getCode());
        typedBean.setOrdinal(EnumValue.Ordinal.ORDER.getCode());
        typedBean.setIntervals(EnumValue.Intervals.DISABLED.getCode());
        typedBean.setAmount(9);
        typedBean.setCreatetime(new Timestamp(System.currentTimeMillis()));
        TagBean tagBean = TagBusiness.getInstance().loadByName("垫片");
        typedBean.setRes_tags(tagBean == null ? null : tagBean.getCode().toString());
        typedBean.setSpecial(EnumValue.Special.SPECIAL.getCode());

        this.save(typedBean);
        this.saveTypeTags(typedBean);
    }


    /**
     * 获取当前频率下系统默认创建的规则信息
     *
     * @param managerId
     * @param channelId
     * @return
     */
    public TypedBean loadSpecialTyped(Long managerId, Long channelId) {
        return DBTool.find(TypedBean.class, "SELECT * FROM typed WHERE manager_id=? AND channel_id=? AND special=1 AND status=1", managerId, channelId);
    }

    public synchronized static TypedBusiness getInstance() {
        if (typedBusiness == null) {
            typedBusiness = new TypedBusiness();
        }

        return typedBusiness;
    }

}

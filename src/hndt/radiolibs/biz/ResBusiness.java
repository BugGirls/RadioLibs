package hndt.radiolibs.biz;

import com.google.gson.reflect.TypeToken;
import hndt.radiolibs.bean.*;
import hndt.radiolibs.util.*;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ResBusiness {

    public ResBean durationAndBitrate(String resFile, ResBean bean) {
        String text = ProcessExecutor.getInstance().executeFileInfo(resFile);
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = (lines[i]);
            if (!line.contains("Duration")) {
                continue;
            }
            String[] components = line.split(",");
            for (int j = 0; j < components.length; j++) {
                String comp = components[j];
                int idx = comp.indexOf(':');
                if (idx <= 0) {
                    continue;
                }
                String name = comp.substring(0, idx).trim();
                String value = comp.substring(idx + 1).trim();
                if ("Duration".equals(name)) {
                    int duration = LocalTime.parse(value).toSecondOfDay();
                    bean.setFormat_duration(Long.valueOf(duration));
                }
                if ("bitrate".equals(name)) {
                    bean.setFormat_coding(value);
                }
            }
            break;
        }
        ProcessExecutor.getInstance().exit();
        return bean;
    }

    /**
     * 判断上传的文件采样率是否为44100Hz或44.1KHz的文件
     *
     * @param resFile
     * @return
     */
    public boolean is44100HZ(String resFile) {
        // 获取上传的文件信息
        String text = ProcessExecutor.getInstance().executeFileInfo(resFile);
        if (text.contains(EnumValue.SAMPLING_FREQUENCY)) {
            return true;
        }
        return false;
    }

    public void attachWaveData(ResBean bean) {
        String data = null;
        if (Utils.isNotEmpty(bean.getId())) {
            data = DBTool.field(String.class, "SELECT wave FROM respository_file_meta WHERE id=?", bean.getId());
            bean.setWaveData(data);
        }
    }

    public int saveFile(ResBean bean) {
        int r = 0;
        if (bean.getId() == null || bean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO respository(uuid,manager_id,category,sequence,system,path,size,format_duration,format_coding,format_quality,visibility,lifetime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)", bean.getUuid(), bean.getManager_id(), bean.getCategory(), bean.getSequence(), bean.getSystem(), bean.getPath(), bean.getSize(), bean.getFormat_duration(), bean.getFormat_coding(), bean.getFormat_quality(), bean.getVisibility(), bean.getLifetime());
            if (id > 0) {
                bean.setId(id);
                DBTool.update("UPDATE respository SET sequence=? WHERE id=?", id, id);
                saveWaveData(bean);
                r = 1;
            }
        } else {
            r = DBTool.update("UPDATE respository SET category=?,system=?,format_duration=?,format_coding=?,format_quality=?,visibility=?,lifetime=?,path=?,size=? WHERE id=?", bean.getCategory(), bean.getSystem(), bean.getFormat_duration(), bean.getFormat_coding(), bean.getFormat_quality(), bean.getVisibility(), bean.getLifetime(), bean.getPath(), bean.getSize(), bean.getId());
            saveWaveData(bean);
        }
        return r;
    }

    public long save(ResBean resBean) {
        long r = 0;
        if (resBean.getId() == null || resBean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO respository(uuid,manager_id,category,sequence,system,title_proper,title_parallel,publisher,format_duration,title_series,visibility,lifetime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                    resBean.getUuid(), resBean.getManager_id(), resBean.getCategory(), resBean.getSequence(), resBean.getSystem(), resBean.getTitle_proper(), resBean.getTitle_parallel(), resBean.getPublisher(), resBean.getFormat_duration(), resBean.getTitle_series(), resBean.getVisibility(), resBean.getLifetime());
            if (id > 0) {
                resBean.setId(id);
                DBTool.update("UPDATE respository SET sequence=? WHERE id=?", id, id);
                r = id;
            }
        } else {
            r = DBTool.update("UPDATE respository SET category=?,sequence=?,system=?,title_proper=?,title_parallel=?,publisher=?,format_duration=?,title_series=?,visibility=?,lifetime=? WHERE id=?",
                    resBean.getCategory(), resBean.getSequence(), resBean.getSystem(), resBean.getTitle_proper(), resBean.getTitle_parallel(), resBean.getPublisher(), resBean.getFormat_duration(), resBean.getTitle_series(), resBean.getVisibility(), resBean.getLifetime());
            saveWaveData(resBean);
        }
        return r;
    }

    public int saveWaveData(ResBean bean) {
        int r = 0;
        // ResFileMetaBean表中的id与res表中的id相对应，通过id判断ResFileMetaBean对应的表是否存在
        String wave = DBTool.field(String.class, "SELECT wave FROM respository_file_meta WHERE id=?", bean.getId());
        if (wave == null) {
            long id = DBTool.insert("INSERT INTO respository_file_meta(id, wave) VALUES (?,?)", bean.getId(), bean.getWaveData());
            if (id > 0) {
                r = 1;
            }
        } else {
            r = DBTool.update("UPDATE respository_file_meta SET wave=? WHERE id=?", bean.getWaveData(), bean.getId());
        }

        return r;
    }

    public int saveTitle(ResBean bean) {
        int r = DBTool.update("UPDATE respository SET title_proper=?,title_parallel=?,title_other=?,title_alternative=?,title_series=?,sequence=? WHERE id=?", bean.getTitle_proper(), bean.getTitle_parallel(), bean.getTitle_other(), bean.getTitle_alternative(), bean.getTitle_series(), bean.getSequence(), bean.getId());
        return r;
    }

    public int saveCreator(ResBean bean) {
        int r = DBTool.update("UPDATE respository SET creator_content=? WHERE id=?", bean.getCreator_content(), bean.getId());
        return r;
    }

    public int saveTypeTags(ResBean bean) {
        int r = DBTool.update("UPDATE respository SET type_tags=? WHERE id=?", bean.getType_tags(), bean.getId());
        return r;
    }

    /**
     * 将资源的类型标签保存到频率管理员所对应的表中
     *
     * @param manager_id
     * @param bean
     * @return
     */
    public int saveTypeTagsForChannelManager(long manager_id, ResBean bean) {
        int r = 0;

        List<Integer> newCodeList = new ArrayList<>();
        for (Iterator<Integer> iterator = bean.getTagList().iterator(); iterator.hasNext(); ) {
            int code = iterator.next();
            TagBean tagBean = TagBusiness.getInstance().validateLoad(code);
            if (tagBean == null) continue;
            TagGroupBean tagGroupBean = TagGroupBusiness.getInstance().load(tagBean.getGroup_id());
            if (tagGroupBean != null && (tagGroupBean.getNaturally() == 0 || bean.getManager_id() == manager_id)) {
                newCodeList.add(code);
            }
        }

        CustomTagBean customTagBean = loadCustomTag(manager_id, bean.getId());
        if (customTagBean != null) {
            customTagBean.setRes_id(bean.getId());
            customTagBean.setType_tags(Utils.asString(newCodeList));
            //DBTool.update("UPDATE res_tag_custom SET type_tags=?, updatetime=? WHERE id=?")
            r = DBTool.update("UPDATE res_tag_custom SET res_id=?, type_tags=?, updatetime=? WHERE id=?", customTagBean.getRes_id(), customTagBean.getType_tags(), Utils.nowTimeString(), customTagBean.getId());
        } else {
            customTagBean = new CustomTagBean();
            customTagBean.setManager_id(manager_id);
            customTagBean.setRes_id(bean.getId());
            customTagBean.setType_tags(Utils.asString(newCodeList));
            long id = DBTool.insert("INSERT INTO res_tag_custom(manager_id, res_id, type_tags) VALUES(?,?,?)", customTagBean.getManager_id(), customTagBean.getRes_id(), customTagBean.getType_tags());
            if (id > 0) {
                customTagBean.setId(id);
                r = 1;
            }
        }
        return r;
    }

    /**
     * 获取频率管理员对应的表中的资源标签
     *
     * @param manager_id
     * @param res_id
     * @return
     */
    public CustomTagBean loadCustomTag(long manager_id, long res_id) {
        String managers = ManagerGroupBusiness.getInstance().group(manager_id);
        CustomTagBean customTagBean = DBTool.find(CustomTagBean.class, "SELECT * FROM res_tag_custom WHERE manager_id in (" + managers + ") AND res_id=?", res_id);
        return customTagBean;
    }


    public Map<Long, CustomTagBean> mapByResId(long manager_id, List<Long> res_ids) {
        if (res_ids == null || res_ids.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        String managers = ManagerGroupBusiness.getInstance().group(manager_id);
        List<CustomTagBean> list = DBTool.list(CustomTagBean.class, "SELECT * FROM res_tag_custom WHERE manager_id in (" + managers + ") AND res_id in (" + SQL.toInString(res_ids) + ")");
        Map<Long, CustomTagBean> map = list.stream().collect(Collectors.toMap(CustomTagBean::getRes_id, x -> x));
        return map;
    }

    /**
     * desc说明，publish出版，date日期，id出版编号
     *
     * @param bean
     * @return
     */
    public int savePublisher(ResBean bean) {
        int r = DBTool.update("UPDATE respository SET subject_lyrics=?, subject_term=?,subject_keyword=?,desc_note=?,desc_abstract=?,desc_awards=?,publisher=?,date_published=?,date_recorded=?,date_debut=?,id_isrc=?,id_isbn=?,id_original=? WHERE id=?",
                bean.getSubject_lyrics(), bean.getSubject_term(), bean.getSubject_keyword(), bean.getDesc_note(), bean.getDesc_abstract(), bean.getDesc_awards(), bean.getPublisher(), bean.getDate_published(), bean.getDate_recorded(), bean.getDate_debut(), bean.getId_isrc(), bean.getId_isbn(), bean.getId_original(), bean.getId());
        return r;
    }

    /**
     * 事件
     *
     * @param bean
     * @return
     */
    public int saveSubject(ResBean bean) {
        int r = DBTool.update("UPDATE respository SET subject_contributor=?,subject_event_place=?,subject_debut_column=?,subject_term=?,subject_keyword=? WHERE id=?",
                bean.getSubject_contributor(), bean.getSubject_event_place(), bean.getSubject_debut_column(), bean.getSubject_term(), bean.getSubject_keyword(), bean.getId());
        return r;
    }

    /**
     * 格式
     *
     * @param bean
     * @return
     */
    public int saveFormat(ResBean bean) {
        int r = DBTool.update("UPDATE respository SET format_starting_point=?,format_duration=?,format_media=?,format_background_voice=?,format_channels=?,format_channels_content=?,format_languange=?,format_quality=?,format_mark=? WHERE id=?",
                bean.getFormat_starting_point(), bean.getFormat_duration(), bean.getFormat_media(), bean.getFormat_background_voice(), bean.getFormat_channels(), bean.getFormat_channels_content(), bean.getFormat_languange(), bean.getFormat_quality(), bean.getFormat_mark(), bean.getId());
        return r;
    }

    public int saveFormatMark(ResBean resBean) {
        int r = DBTool.update("UPDATE respository SET format_mark = ? WHERE id = ?", resBean.getFormat_mark(), resBean.getId());
        return r;
    }

    /**
     * 授权
     *
     * @param bean
     * @return
     */
    public int saveRight(ResBean bean) {
        int r = DBTool.update("UPDATE respository SET source_acquired=?, source_provider=?, right_content=? WHERE id=?", bean.getSource_acquired(), bean.getSource_provider(), bean.getRight_content(), bean.getId());
        return r;
    }

    public int delete(ResBean bean) {
        int r = DBTool.update("DELETE FROM respository WHERE id=?", bean.getId());
        return r;
    }

    /**
     * 通过生命期判断，删除过期的资源文件
     */
    public static void deleteOnLifetime() {
        // 删除所有的过期文件
        int r = DBTool.update("DELETE FROM respository WHERE lifetime>0 AND createtime < NOW()-INTERVAL lifetime DAY");
        Logger.info("删除了" + r + "个资源");
    }

    public void attachCreatorList(ResBean bean) {
        if (bean != null && bean.getCreator_content() != null) {
            bean.setCreatorList(GSON.toObject(bean.getCreator_content(), typeColumnCreatorBean));
        }
    }

    public void attachRightList(ResBean bean) {
        if (bean != null && bean.getRight_content() != null) {
            bean.setRightList(GSON.toObject(bean.getRight_content(), typeColumnRightBean));
        }
    }

    public void attachMarkBean(ResBean bean) {
        if (bean != null && bean.getFormat_mark() != null) {
            bean.setMarkBean(GSON.toObject(bean.getFormat_mark(), ColumnMarkBean.class));
        }
    }

    public void attachSinger(ResBean bean) {
        if (bean != null && bean.getCreatorList() != null && !bean.getCreatorList().isEmpty()) {
//          if (bean.getCreatorList().size() >= 1) {
//              bean.setSinger(bean.getCreatorList().get(0).getName());
//          }
            List<String> singers = new ArrayList<>();
            bean.getCreatorList().forEach(x -> {
                if ("演唱".equals(x.getAction()) && Utils.isNotEmpty(x.getAction())) {
                    singers.add(x.getName());
                }
                if ("播音".equals(x.getAction()) && Utils.isNotEmpty(x.getAction())) {
                    singers.add(x.getName());
                }
            });
            if (!singers.isEmpty()) {
                bean.setSinger(Utils.asString(singers));
            }
        }
    }

    /**
     * 自动填充已选择的标签
     *
     * @param mbean
     * @param bean
     * @param tagGroups
     */
    public void attachSelectedTags(ManagerBean mbean, ResBean bean, List<TagGroupBean> tagGroups) {
        List<Integer> tagList = new ArrayList<>();
        //资源库自有标签
        if (Utils.isNotEmpty(bean.getType_tags())) {
            List<Integer> list = Utils.asListInteger(bean.getType_tags());
            if (list != null) {
                tagList.addAll(list);
            }
        }
        //频率管理员标签. 同一组标签以频率管理员的值为主.
        CustomTagBean customTagBean = loadCustomTag(mbean.getId(), bean.getId());
        if (customTagBean != null && customTagBean.getType_tags() != null) {
            List<Integer> customTags = Utils.asListInteger(customTagBean.getType_tags());
            if (customTags != null) {
                for (Integer code : customTags) {
                    Optional<TagGroupBean> has = tagGroups.stream().filter(x -> TagGroupBusiness.getInstance().containsTag(x, code)).findAny();
                    if (has.isPresent()) {
                        has.get().getChildren().forEach(x -> {
                            tagList.remove(x.getCode());
                        });
                    }
                }
                tagList.addAll(customTags);
            }
        }
        bean.setTagList(tagList);
    }

    public void attachTags(ResBean bean) {
        String type_tags = bean.getType_tags();
        if (type_tags != null) {
            bean.setTagBeanList(new ArrayList<>());
            List<String> tags = Utils.asList(type_tags);
            bean.getTagBeanList().addAll(TagBusiness.getInstance().listByCodes(tags));
        }
    }

    public ResBean load(long id) {
        ResBean bean = DBTool.find(ResBean.class, "SELECT * FROM respository WHERE id=?", id);
        if (bean != null) {
        }
        return bean;
    }

    /**
     * 审核操作
     *
     * @param resBean
     * @return
     */
    public int auditOperator(ResBean resBean) {
        int r = DBTool.update("UPDATE respository SET audit_status=?,reject_cause=? WHERE id=?", resBean.getAudit_status(), resBean.getReject_cause(), resBean.getId());
        return r;
    }

    public PageBean pagination(int category, long range, Long managerId, List<Integer> selectedTags, String keyword, Integer auditStatus, PageBean pageBean) {
        StringBuilder find_conditions = null;
        if (selectedTags != null && selectedTags.size() > 0) {
            find_conditions = new StringBuilder("AND ( ");
            for (int i = 0; i < selectedTags.size(); i++) {
                find_conditions.append("FIND_IN_SET(" + selectedTags.get(i) + ", type_tags) ");
                if (i + 1 < selectedTags.size()) {
                    find_conditions.append(" AND ");
                }
            }
            find_conditions.append(")");
        } else {
            find_conditions = new StringBuilder();
        }

        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sql = SQL.of("SELECT * FROM respository").and("visibility in", "0,1,2").append(find_conditions.toString());
        // 判断资源范围
        if (range == 1) {
            sql.and("system=", 1);
        } else if (range == 2) {
            sql.and("manager_id=", managerId);
        } else if (range == 3) {
            String group = ManagerGroupBusiness.getInstance().group(managerId);
            if (group == null) {
                sql.or("manager_id=", managerId, "system=", 1);
            } else {
                sql.or("manager_id in", group, "system=", 1);
            }
        } else if (range == 4) {
            String group = ManagerGroupBusiness.getInstance().group(managerId);
            if (group == null) {
                sql.and("manager_id=", managerId);
            } else {
                sql.and("manager_id in", group);
            }
        }

        if (category > 0) {
            sql.and("category=", category);
        }

        sql.or("title_proper LIKE", keyword, "creator_content LIKE", keyword);
        // 加入审核状态筛选
        if (auditStatus != null) {
            sql.and("audit_status=", auditStatus);
        }
        // 排序和分页
        sql.append("ORDER BY id DESC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<ResBean> list = DBTool.list(ResBean.class, sql.sql(), sql.params());

        List<Long> res_ids = list.stream().map(ResBean::getId).collect(Collectors.toList());
        Map<Long, CustomTagBean> map_by_res = mapByResId(managerId, res_ids);
        list.forEach(x -> {
            CustomTagBean ctbean = map_by_res.get(x.getId());
            List<String> list1 = Utils.asList(x.getType_tags());
            if (ctbean != null && ctbean.getType_tags() != null) {
                List<String> list2 = Utils.asList(ctbean.getType_tags());
                if (list2 != null && list2.size() > 0) {
                    list1.addAll(list2);
                }
                x.setType_tags(Utils.asString(list1));
                if (ctbean.getUpdatetime() != null) {
                    x.setUpdatetime(ctbean.getUpdatetime());
                }
            }

        });

        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(list);
        return pageBean;
    }

    public int visibility(ResBean bean, int visibility) {
        int r = DBTool.update("UPDATE respository SET visibility=? WHERE id=?", visibility, bean.getId());
        return r;
    }

    public List<ResBean> listFilterRes(ResBean resBean, List<Long> resIds, Long manager_id) {
        if (resIds.size() == 0) {
            return DBTool.list(ResBean.class, "SELECT id, type_tags FROM respository WHERE category=? AND manager_id=?", resBean.getCategory(), manager_id);
        } else {
            return DBTool.list(ResBean.class, "SELECT id, type_tags FROM respository WHERE category=? AND manager_id=? AND id NOT IN(" + SQL.toInString(resIds) + ")", resBean.getCategory(), manager_id);
        }
    }

    public List<ResBean> list(ResBean resBean) {
        return DBTool.list(ResBean.class, "SELECT * FROM respository WHERE manager_id=? AND category=?", resBean.getManager_id(), resBean.getCategory());
    }

    public List<ResBean> list(Long manager_id, String keyword, Long res_category) {
        SQL sql = SQL.of("SELECT * FROM respository").and("manager_id=", manager_id).and("title_proper LIKE", keyword).and("category=", res_category).limit(0, 100);
        return DBTool.list(ResBean.class, sql.sql(), sql.params());
    }

    public List<ResBean> listByManagerId(Long manager_id) {
        return DBTool.list(ResBean.class, "SELECT * FROM respository WHERE manager_id=?", manager_id);
    }

    public List<ResBean> listByIds(List<String> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        } else {
            return DBTool.list(ResBean.class, "SELECT * FROM respository WHERE id IN(" + SQL.toInString(ids) + ")");
        }
    }

    public Map<Long, ResBean> mapByIds(List<Long> ids) {
        Map<Long, ResBean> map = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            List<ResBean> list = DBTool.list(ResBean.class, "SELECT * FROM respository WHERE id IN(" + SQL.toInString(ids) + ")");
            for (ResBean rb : list) {
                map.put(rb.getId(), rb);
            }
        }
        return map;
    }

    public List<ResBean> listByIdsOrder(List<String> resIds) {
        List<ResBean> result = new ArrayList<>();
        if (resIds.size() == 0) {
            return new ArrayList<>();
        } else {
            List<ResBean> resBeanList = DBTool.list(ResBean.class, "SELECT * FROM respository WHERE id IN(" + SQL.toInString(resIds) + ") order by sequence");

            for (String id : resIds) {
                for (ResBean resBean : resBeanList) {
                    if (resBean.getId().equals(Long.parseLong(id))) {
                        result.add(resBean);
                    }
                }
            }
            return result;
        }
    }

    //规则的标签越多，挑选的歌曲越多
    public List<ResBean> listByTags(Long manager_id, int category, List<Integer> tags, String order) {
        String fields = "r.id,r.uuid,r.category,r.sequence,r.manager_id,r.format_duration,r.format_starting_point,r.path,r.size,r.title_proper";
        StringBuilder find_conditions = new StringBuilder();
        String find_conditions_custom = null;
        if (!tags.isEmpty()) {
            find_conditions = new StringBuilder("AND ( ");
            for (int i = 0; i < tags.size(); i++) {
                find_conditions.append("FIND_IN_SET(" + tags.get(i) + ", r.type_tags) ");
                if (i + 1 < tags.size()) {
                    find_conditions.append(" AND ");
                }
            }
            find_conditions.append(")");
            find_conditions_custom = find_conditions.toString().replace("r.type_tags", "c.type_tags");
        }

        if (category == 0) {
            category = -1;
        }

        String managers = ManagerGroupBusiness.getInstance().group(manager_id);

        SQL sql = SQL.of("SELECT " + fields + " FROM respository r WHERE r.visibility=0").or("r.system=", 1, "r.manager_id in", managers).and("r.category=", category).append(find_conditions.toString())
                .append(" UNION ")
                .append("SELECT " + fields + " FROM respository r,res_tag_custom c WHERE r.visibility=0 AND r.id=c.res_id").or("r.system=", 1, "r.manager_id in", managers).and("r.category=", category).append(find_conditions_custom)
                .append(order);

        List<ResBean> list = DBTool.list(ResBean.class, sql.sql(), sql.params());

        return list;
    }

    /**
     * 未启用
     * 组和组之间是“并且”的关系，组内标签是“或者”的关系
     */
    public List<ResBean> listByTags2(Long manager_id, int category, List<Integer> tags, String order) {

        Map<Long, HashSet<Integer>> groups = new HashMap<>();
        for (Integer tag : tags) {
            TagGroupBean group = TagBusiness.getInstance().loadGroup(tag);
            if (group == null) continue;
            if (groups.get(group.getId()) == null) {
                groups.put(group.getId(), new HashSet<>());
            }
            groups.get(group.getId()).add(tag);
        }

        StringBuilder find_conditions = new StringBuilder();
        String find_conditions_custom = null;

        if (!groups.isEmpty()) {
            Collection<HashSet<Integer>> values = groups.values();
            int i = 0;
            find_conditions = new StringBuilder("AND ( ");

            for (HashSet<Integer> tagSet : values) {
                if (tagSet.isEmpty()) {
                    continue;
                }
                if (i > 0) {
                    find_conditions.append(" AND ( ");
                }
                int j = 0;
                for (Integer t : tagSet) {
                    find_conditions.append("FIND_IN_SET(" + t + ", r.type_tags)");
                    if (j + 1 < tagSet.size()) {
                        find_conditions.append(" OR ");
                    }
                    j++;
                }
                if (i + 1 < values.size()) {
                    find_conditions.append(" ) ");
                }
                i++;
            }

            find_conditions.append(" )");
            find_conditions_custom = find_conditions.toString().replace("r.type_tags", "c.type_tags");
        }

        System.out.println(find_conditions);
        //SQL sql = SQL.of("");
        //List<ResBean> list = DBTool.list(ResBean.class, sql.sql(), sql.params());
        return null;
    }

    public List<ResBean> listByTags(Long manager_id, int category, List<Integer> tags) {
        return listByTags(manager_id, category, tags, "ORDER BY sequence ASC");
    }

    public List<ResBean> list(Long channel_id, Long clock_id, Long manager_id) {
        return DBTool.list(ResBean.class, "SELECT * FROM respository WHERE channel_id=? AND clock_id=? AND manager_id=?", channel_id, clock_id, manager_id);
    }

    /**
     * 获取垫片资源文件
     *
     * @return
     */
    public List<ResBean> listByGasket() {
        // 获取res_tag_custom表中频率管理员打的垫片标签
        List<Long> resIds = DBTool.column(Long.class, "SELECT res_id FROM res_tag_custom where type_tags like '%1001200%'");
        List<ResBean> resBeanList = new ArrayList<>();
        if (resIds.size() > 0) {
            resBeanList = DBTool.list(ResBean.class, "SELECT * FROM respository WHERE id IN(" + SQL.toInString(resIds) + ")");
        }

        // 获取respository表中所有管理员打的垫片标签
        resBeanList.addAll(DBTool.list(ResBean.class, "SELECT * FROM respository WHERE type_tags LIKE '%1001200%'"));

        return resBeanList;
    }

    Type typeColumnCreatorBean = new TypeToken<List<ColumnCreatorBean>>() {
    }.getType();
    Type typeColumnRightBean = new TypeToken<List<ColumnRightBean>>() {
    }.getType();
    Type typeInteger = new TypeToken<List<Integer>>() {
    }.getType();

    private static ResBusiness biz = null;

    private ResBusiness() {
    }

    public synchronized static ResBusiness getInstance() {
        if (biz == null) {
            biz = new ResBusiness();
        }
        return biz;
    }
}

package hndt.radiolibs.bean;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**

 */
public class ResBean implements Serializable {
    Long id; //ID
    String uuid; //UUID
    Long manager_id;//操作人ID
    int category; //1新闻 2专题 3声乐 4器乐 5文学 6戏剧 7戏曲 8曲艺 9广播剧/电影/电视剧 10综艺节目 11音响效果 12配乐 13专业素材 14文献
    String title_proper; //正题名
    String title_parallel; //并列正题名,正题名的其他语种表示
    String title_other; //其他题名,从属于正题名的解释或补充
    String title_alternative; //交替题名,正题名的别名
    String title_series; //系列题名,音频资料所属系列的名称
    int sequence; //在系列中的顺序号
    String creator_content; //JSON
    String publisher; //出版者，使音频资料可以获得和利用的责任实体
    Timestamp date_published; //音频资料正式出版的日期;
    Timestamp date_recorded; //音频资料录制完成的日期;
    Timestamp date_debut; //音频资料第一次向公众播放的日期;
    String desc_abstract; //摘要 概括地说明音频资料的内容要点
    String desc_note; //附注,说明音频资料的性质、特征、用途以及范围等的文字
    String desc_awards; //获奖,音频资料的获奖情况
    String subject_contributor; //事件责任者,事件主要责任者或新闻发布者或所涉及的主要对象
    Timestamp subject_event_date; //事发日期
    String subject_event_place; //事发地点
    String subject_debut_column; //首播栏目,首次播出该节目且具有特定名称、标志、内容范围和相对固定的组织编排形式的节目板块的 名称
    String subject_lyrics; //音频资料的文字信息内容
    String subject_term; //主题词,揭示音频资料内容主要特征的规范化的词或词组
    String subject_keyword; //关键词,具有检索意义的词或词组
    String type_tags; //描述音频资料内容的特征、种类、风格、流派等。
    Long format_duration; //时长
    Long format_starting_point; //入点，音频资料正式有效内容的起始点时间;
    String format_mark; //JSON
    String format_coding; //含采样频率、量化比特、压缩方式及码率
    String format_media; //原始介质
    Integer format_background_voice; //有无现场声0-1, 0无 1有
    Integer format_channels; //声道总数量1-6
    String format_channels_content; //声道内容
    String format_languange;  //表达音频资料主要内容的语种或方言信息,如汉语
    Integer format_quality; //声音质量1-6
    String id_isrc; //国际标准音像制品编码
    String id_isbn; //国际标准书号
    String id_original; //音频资料的原始出版编号
    String source_acquired; //资料获取方式,获得音频资料的途径和方法, 如购买,自采,交换,受赠,合作,委托制作,其他;
    String source_provider; //提供音频资料的单位或个人的名称;
    String right_content; //JSON
    int system; //是否系统库
    String path; //存放路径
    Long size; //文件大小
    Integer visibility; //0公开 1内部可见 2需要权限 3完全隐藏
    Integer lifetime; //生命期(天) 0永久
    Timestamp createtime; //创建时间
    Timestamp updatetime; //创建时间

    // transient
    Long duration; //servlet里用
    String name; //servlet里用
    List<ColumnCreatorBean> creatorList;
    ColumnMarkBean markBean;
    List<ColumnRightBean> rightList;
    List<Integer> tagList;
    List<TagBean> tagBeanList;
    String waveData; // 波形图片
    String singer; //歌手

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getManager_id() {
        return manager_id;
    }

    public void setManager_id(Long manager_id) {
        this.manager_id = manager_id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTitle_proper() {
        return title_proper;
    }

    public void setTitle_proper(String title_proper) {
        this.title_proper = title_proper;
    }

    public String getTitle_parallel() {
        return title_parallel;
    }

    public void setTitle_parallel(String title_parallel) {
        this.title_parallel = title_parallel;
    }

    public String getTitle_other() {
        return title_other;
    }

    public void setTitle_other(String title_other) {
        this.title_other = title_other;
    }

    public String getTitle_alternative() {
        return title_alternative;
    }

    public void setTitle_alternative(String title_alternative) {
        this.title_alternative = title_alternative;
    }

    public String getTitle_series() {
        return title_series;
    }

    public void setTitle_series(String title_series) {
        this.title_series = title_series;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getCreator_content() {
        return creator_content;
    }

    public void setCreator_content(String creator_content) {
        this.creator_content = creator_content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Timestamp getDate_published() {
        return date_published;
    }

    public void setDate_published(Timestamp date_published) {
        this.date_published = date_published;
    }

    public Timestamp getDate_recorded() {
        return date_recorded;
    }

    public void setDate_recorded(Timestamp date_recorded) {
        this.date_recorded = date_recorded;
    }

    public Timestamp getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Timestamp date_debut) {
        this.date_debut = date_debut;
    }

    public String getDesc_abstract() {
        return desc_abstract;
    }

    public void setDesc_abstract(String desc_abstract) {
        this.desc_abstract = desc_abstract;
    }

    public String getDesc_note() {
        return desc_note;
    }

    public void setDesc_note(String desc_note) {
        this.desc_note = desc_note;
    }

    public String getDesc_awards() {
        return desc_awards;
    }

    public void setDesc_awards(String desc_awards) {
        this.desc_awards = desc_awards;
    }

    public String getSubject_contributor() {
        return subject_contributor;
    }

    public void setSubject_contributor(String subject_contributor) {
        this.subject_contributor = subject_contributor;
    }

    public Timestamp getSubject_event_date() {
        return subject_event_date;
    }

    public void setSubject_event_date(Timestamp subject_event_date) {
        this.subject_event_date = subject_event_date;
    }

    public String getSubject_event_place() {
        return subject_event_place;
    }

    public void setSubject_event_place(String subject_event_place) {
        this.subject_event_place = subject_event_place;
    }

    public String getSubject_debut_column() {
        return subject_debut_column;
    }

    public void setSubject_debut_column(String subject_debut_column) {
        this.subject_debut_column = subject_debut_column;
    }

    public String getSubject_lyrics() {
        return subject_lyrics;
    }

    public void setSubject_lyrics(String subject_lyrics) {
        this.subject_lyrics = subject_lyrics;
    }

    public String getSubject_term() {
        return subject_term;
    }

    public void setSubject_term(String subject_term) {
        this.subject_term = subject_term;
    }

    public String getSubject_keyword() {
        return subject_keyword;
    }

    public void setSubject_keyword(String subject_keyword) {
        this.subject_keyword = subject_keyword;
    }

    public String getType_tags() {
        return type_tags;
    }

    public void setType_tags(String type_tags) {
        this.type_tags = type_tags;
    }

    public Long getFormat_duration() {
        return format_duration;
    }

    public void setFormat_duration(Long format_duration) {
        this.format_duration = format_duration;
    }

    public Long getFormat_starting_point() {
        return format_starting_point;
    }

    public void setFormat_starting_point(Long format_starting_point) {
        this.format_starting_point = format_starting_point;
    }

    public String getFormat_mark() {
        return format_mark;
    }

    public void setFormat_mark(String format_mark) {
        this.format_mark = format_mark;
    }

    public String getFormat_coding() {
        return format_coding;
    }

    public void setFormat_coding(String format_coding) {
        this.format_coding = format_coding;
    }

    public String getFormat_media() {
        return format_media;
    }

    public void setFormat_media(String format_media) {
        this.format_media = format_media;
    }

    public Integer getFormat_background_voice() {
        return format_background_voice;
    }

    public void setFormat_background_voice(Integer format_background_voice) {
        this.format_background_voice = format_background_voice;
    }

    public Integer getFormat_channels() {
        return format_channels;
    }

    public void setFormat_channels(Integer format_channels) {
        this.format_channels = format_channels;
    }

    public String getFormat_channels_content() {
        return format_channels_content;
    }

    public void setFormat_channels_content(String format_channels_content) {
        this.format_channels_content = format_channels_content;
    }

    public String getFormat_languange() {
        return format_languange;
    }

    public void setFormat_languange(String format_languange) {
        this.format_languange = format_languange;
    }

    public Integer getFormat_quality() {
        return format_quality;
    }

    public void setFormat_quality(Integer format_quality) {
        this.format_quality = format_quality;
    }

    public String getId_isrc() {
        return id_isrc;
    }

    public void setId_isrc(String id_isrc) {
        this.id_isrc = id_isrc;
    }

    public String getId_isbn() {
        return id_isbn;
    }

    public void setId_isbn(String id_isbn) {
        this.id_isbn = id_isbn;
    }

    public String getId_original() {
        return id_original;
    }

    public void setId_original(String id_original) {
        this.id_original = id_original;
    }

    public String getSource_acquired() {
        return source_acquired;
    }

    public void setSource_acquired(String source_acquired) {
        this.source_acquired = source_acquired;
    }

    public String getSource_provider() {
        return source_provider;
    }

    public void setSource_provider(String source_provider) {
        this.source_provider = source_provider;
    }

    public String getRight_content() {
        return right_content;
    }

    public void setRight_content(String right_content) {
        this.right_content = right_content;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getLifetime() {
        return lifetime;
    }

    public void setLifetime(Integer lifetime) {
        this.lifetime = lifetime;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    public List<ColumnCreatorBean> getCreatorList() {
        return creatorList;
    }

    public void setCreatorList(List<ColumnCreatorBean> creatorList) {
        this.creatorList = creatorList;
    }

    public ColumnMarkBean getMarkBean() {
        return markBean;
    }

    public void setMarkBean(ColumnMarkBean markBean) {
        this.markBean = markBean;
    }

    public List<ColumnRightBean> getRightList() {
        return rightList;
    }

    public void setRightList(List<ColumnRightBean> rightList) {
        this.rightList = rightList;
    }

    public List<Integer> getTagList() {
        return tagList;
    }

    public void setTagList(List<Integer> tagList) {
        this.tagList = tagList;
    }

    public String getWaveData() {
        return waveData;
    }

    public void setWaveData(String waveData) {
        this.waveData = waveData;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public List<TagBean> getTagBeanList() {
        return tagBeanList;
    }

    public void setTagBeanList(List<TagBean> tagBeanList) {
        this.tagBeanList = tagBeanList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResBean resBean = (ResBean) o;
        return Objects.equals(id, resBean.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

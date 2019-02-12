package hndt.radiolibs.biz;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;

public class EnumValue {

    public static final String SERVLET_POST = "POST";
    public static final String SERVLET_GET = "GET";
    public static final String ENCODING_UTF8 = "utf-8";
    public static final String LITERAL_MANAGER = "manager";
    public static final String LITERAL_TIPS = "tips";
    public static final String LITERAL_JSVOID = "javascript:void(0);";
    public static final String SAMPLING_FREQUENCY = "44100 Hz";
    public static final int WEEK_OF_DAY = 7;

    /**
     * 是否为系统默认创建的规则
     */
    public enum Special {
        SPECIAL(1, "特殊"),
        COMMON(0, "普通");

        private int code;
        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        Special(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Special instances(int code) {
            for (Special s : Special.values()) {
                if (Objects.equals(s.getCode(), code)) {
                    return s;
                }
            }
            return null;
        }
    }


    public enum Intervals {
        DISABLED(-1, "不可用"),
        WITHOUT_REPETITION(0, "不重复"),
        NUM_WITHOUT_REPETITION(1, "天内不重复");

        private int code;
        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        Intervals(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Intervals instances(int code) {
            for (Intervals i : Intervals.values()) {
                if (Objects.equals(i.getCode(), code)) {
                    return i;
                }
            }
            return null;
        }
    }

    /**
     * 顺序策略
     */
    public enum  Ordinal {
        ORDER(0, "顺序-从先到后"),
        DESC(1,"倒序-从新到旧"),
        REPEATE(2, "重播-重复上一次播放的音频"),
        RANDOM(3, "随机-从库中随机选取");

        private int code;
        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        Ordinal(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Ordinal instances(int code) {
            for (Ordinal o : Ordinal.values()) {
                if (Objects.equals(o.getCode(), code)) {
                    return o;
                }
            }
            return null;
        }
    }

    /**
     * 当前状态 1-有效 0-无效
     */
    public enum Status{
        EFFECTIVE(1, "有效"),
        INVALID(0, "无效");

        private int code;
        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        Status(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Status instances(int code) {
            for (Status s : Status.values()) {
                if (Objects.equals(s.getCode(), code)) {
                    return s;
                }
            }
            return null;
        }
    }


    /**
     * 标签属性
     */
    public enum Naturally {
        IMPERSONALITY(0, "自然客观属性"),
        SUBJECTIVITY(1, "主观属性");

        private int code;
        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        Naturally(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Naturally instances(int code) {
            for (Naturally s : Naturally.values()) {
                if (Objects.equals(s.getCode(), code)) {
                    return s;
                }
            }
            return null;
        }
    }

    /**
     * 0公开 1需要权限 2仅管理员 3隐藏
     */
    public enum Visibility {
        PUBLIC(0, "完全公开"),
        PROTECTED(1, "需要权限"),
        PRIVATE(2, "仅管理员");

        private int code;
        private String name;

        Visibility(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Visibility instances(int code) {
            for (Visibility f : Visibility.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 生命期 0公开 1需要权限 2仅管理员 3隐藏
     */
    public enum LifeTime {
        LIFE_10(10, "10天"),
        LIFE_30(30, "1月"),
        LIFE_90(90, "3月"),
        LIFE_180(180, "6月"),
        LIFE_360(360, "1年"),
        LIFE_3YEAR(360 * 3, "3年"),
        LIFE_FOREVER(0, "永久");

        private int code;
        private String name;

        LifeTime(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static LifeTime instances(int code) {
            for (LifeTime f : LifeTime.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    //
    public enum YesNo {
        YES(1, "是"),
        NO(0, "否");

        private int code;
        private String name;

        YesNo(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static YesNo instances(int code) {
            for (YesNo f : YesNo.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }


    /**
     * 1新闻 2专题 3声乐 4器乐 5文学 6戏剧 7戏曲 8曲艺 9广播剧/电影/电视剧 10综艺节目 11音响效果 12配乐 13专业素材 14文献
     */
    public enum Category {
        NEWS(1, "新闻"),
        SUBJECT(2, "专题"),
        SONG(3, "声乐"),
        INSTRUMENTAL(4, "器乐"),
        LITERATURE(5, "文学"),
        DRAMA(6, "戏剧"),
        OPERA(7, "戏曲"),
        QUYI(8, "曲艺"),
        STORY(9, "广播剧/电影/电视剧"),
        VARIETY(10, "综艺"),
        SOUND(11, "音响"),
        SOUNDTRACK(12, "配乐"),
        MATERIAL(13, "专业素材"),
        DOCUMENT(14, "文献");

        private int code;
        private String name;

        Category(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Category instances(int code) {
            for (Category f : Category.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    //
    public enum Section {
        FILE("file", "文件", "file"),
        TITLE("title", "题名", "info-circle"),
        CREATE("create", "创建者", "user"),
        TAG("tag", "类型", "check-circle"),
        PUBLISH("publish", "出版", "font"),
        FORMAT("format", "格式", "cube"),
        RIGHT("right", "授权", "unlock");
        private String code;
        private String name;
        private String icon;

        Section(String code, String name) {
            this.code = code;
            this.name = name;
        }

        Section(String code, String name, String icon) {
            this.code = code;
            this.name = name;
            this.icon = icon;
        }

        public static Section instances(String code) {
            for (Section f : Section.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }

    }


    //
    public enum Quality {
        LOW(1, "低"),
        MEDIUM(1, "中"),
        HIGH(2, "高");

        private int code;
        private String name;

        Quality(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Quality instances(int code) {
            for (Quality f : Quality.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }


    public enum Languange {
        PUTONGHUA(1, "普通话"),
        CANTONESE(2, "粤语"),
        TAIWAN(3, "闽南"),
        ENGLISH(4, "英语"),
        OTHER(9, "其他");

        private int code;
        private String name;

        Languange(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Languange instances(int code) {
            for (Languange f : Languange.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }


    /**
     * 角色
     */
    public enum Role {
        SYSTEM(1, "系统管理员"),
        MANAGER(2, "管理员"),
        UPLOADER(101, "资料管理员"),
        CHANNEL(102, "频率管理员"),
        OTHER(200, "其他");

        private int code;
        private String name;

        Role(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Role instances(int code) {
            for (Role f : Role.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 规则面板分割
     */
    public enum  SectionTyped{
        BASIC("basic", "基础", "user"),
        TAG("tag", "类型", "check-circle");

        private String code;
        private String name;
        private String icon;

        SectionTyped(String code, String name) {
            this.code = code;
            this.name = name;
        }

        SectionTyped(String code, String name, String icon) {
            this.code = code;
            this.name = name;
            this.icon = icon;
        }

        public static SectionTyped instances(String code) {
            for (SectionTyped f : SectionTyped.values()) {
                if (Objects.equals(f.getCode(), code)) {
                    return f;
                }
            }
            return null;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }

    }

    /**
     * 播放日期
     */
    public enum PlayDate {
        MONDAY(1, "周一"),
        TUESDAY(2, "周二"),
        WEDNESDAY(3, "周三"),
        THURSDAY(4, "周四"),
        FRIDAY(5, "周五"),
        SATURDAY(6, "周六"),
        WEEKEND(7, "周日");

        private int code;
        private String name;

        PlayDate(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static PlayDate instances(int code) {
            for (PlayDate p : PlayDate.values()) {
                if (Objects.equals(p.getCode(), code)) {
                    return p;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 持续时长
     */
    public enum Duration {
        FIVE(5, "5分钟"),
        TEN(10, "10分钟"),
        FIFTEEN(15, "15分钟"),
        THIRTY(30, "30分钟"),
        HOUR(60, "60分钟"),
        NINETY(90, "90分钟"),
        TWO_HOURS(120, "两小时"),
        SIX_HOURS(360, "六小时"),
        HALF_DAY(720, "十二小时"),
        ONE_DAY(1439, "全天"); //1440是全天

        private int code;
        private String name;

        Duration(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Duration instances(int code) {
            for (Duration p : Duration.values()) {
                if (Objects.equals(p.getCode(), code)) {
                    return p;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 规则颜色
     */
    public enum Color {
        C00("#FFFFFF", "白色"),
        C01("#03A89E", "锰蓝"),
        C02("#ED9121", "萝卜黄"),
        C03("#DA70D6", "淡紫色"),
        C04("#6B8E23", "草绿色"),
        C05("#808069", "暖灰"),
        C06("#B0171F", "印度红"),
        C07("#8A2BE2", "紫罗兰"),
        C08("#BC8F8F", "玫瑰红"),
        C09("#191970", "深蓝"),
        C10("#FFE384", "粉黄");

        private String code;
        private String name;

        Color(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Duration instances(int code) {
            for (Duration p : Duration.values()) {
                if (Objects.equals(p.getCode(), code)) {
                    return p;
                }
            }
            return null;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public enum ChannelId {
        ALL_CHANNEL(0, "所有频率");

        private int code;
        private String name;

        ChannelId(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static ChannelId instances(int code) {
            for (ChannelId c : ChannelId.values()) {
                if (Objects.equals(c.getCode(), code)) {
                    return c;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

}



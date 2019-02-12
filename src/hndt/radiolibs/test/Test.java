package hndt.radiolibs.test;

import com.alibaba.druid.util.HttpClientUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import hndt.radiolibs.bean.*;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.servlet.vo.ResVo;
import hndt.radiolibs.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) {

        LocalTime tmp = LocalTime.MAX;
        System.out.println(tmp);

//        LocalDateTime now = LocalDateTime.of(2018, 11, 11, 23, 57, 6);
//        LocalDateTime now2 = LocalDateTime.of(2018, 11, 11, 23, 58, 10);
//
//        Duration duration = Duration.between(now, now2);
//        LocalTime tmp = LocalTime.ofSecondOfDay(Math.abs(duration.toMillis()/1000));
//        System.out.println(tmp);
//
//        List<ResBean> list = RuntimeBusiness.getInstance().resByTyped(211L, 92L, TypedBusiness.getInstance().load(138), LocalDateTime.now());
//        list.forEach(x-> System.out.println(x.getTitle_proper()));
//
//        System.out.println(1544431603133L+Utils.DAY);


//        List<ResBean> list = DBTool.list(ResBean.class, "select * from respository where id=120018");
//        List<Long> res_ids = list.stream().map(ResBean::getId).collect(Collectors.toList());
//        Map<Long, CustomTagBean> map_by_res = ResBusiness.getInstance().mapByResId(211, res_ids);
//        list.forEach(x -> {
//            List<String> list1 = Utils.asList(x.getType_tags());
//            list1.addAll(Utils.asList(map_by_res.get(x.getId()).getType_tags()));
//            x.setType_tags(Utils.asString(list1));
//        });
//
//        System.out.println(list.get(0).getType_tags());

//        Long aLong = RuntimeBusiness.getInstance().currentPlayItem(93L);
//        System.out.println(aLong);
//
//        LocalTime localTime = LocalTime.ofSecondOfDay(1290);
//        System.out.println(localTime.format(DateTimeFormatter.ofPattern("m:s")));
//
//        System.out.println(LocalDateTime.now().format(Utils.DATEFORMAT1));

//        String group = ManagerGroupBusiness.getInstance().group(228);
//        System.out.println(group);
//
//        System.out.println(FileUtils.byteCountToDisplaySize(7878789));
//
//        List<String> list = new ArrayList<>();
//        list.add("a");
//        list.add("b");
//        System.out.println(Utils.asString(list));

//        ManagerBean user = ManagerBusiness.getInstance().load(211);
//        Logger.info(GSON.toJson(user));
//
//
//        long l = NumberUtils.toLong("38", 0);
//        System.out.println(l);
//        BigDecimal bd = new BigDecimal(23.823).setScale(2, BigDecimal.ROUND_HALF_UP);
//        String nowday = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
//        System.out.printf(nowday);
//
//        Date date = new Date(1534348800000L);
//        System.out.println(date);
//        Integer[] tags = {1001192, 1001193, 1001194, 1001197, 1001195, 1001196, 1001198, 1001204, 1001205, 1001041,1001055,1001335,1001205,1001203};
//        List<ResBean> list = ResBusiness.getInstance().listByTags2(211L, 3, Arrays.asList(tags), "ORDER BY id DESC");
//        System.out.println(list);


//        String a = new String("programBean");
//        String b = new String("programBean");
//        String c = new String("programBean");
//        String d = new String("programBean");
//
//        List<String> list = new ArrayList<>();
//        list.add(a);
//        list.add(b);
//        list.add(c);
//        list.add(d);
//        System.out.println(list.indexOf(c));
//
//        TypedBean bean1 = TypedBusiness.getInstance().load(86);
//        TypedBean bean2 = TypedBusiness.getInstance().load(86);
//        TypedBean bean3 = TypedBusiness.getInstance().load(86);
//        List<TypedBean> list2 = new ArrayList<>();
//        list2.add(bean1);
//        list2.add(bean2);
//        list2.add(bean3);
//        System.out.println(list2.indexOf(bean3));

//        ProgramBean programBean = ProgramBusiness.getInstance().load(211L, 84L, 5, LocalDate.now().plusDays(1));
//        System.out.println(GSON.toJson(programBean));

//        List<ResBean> list = RuntimeBusiness.getInstance().resByTyped(218L, 88L, TypedBusiness.getInstance().load(86L), LocalDateTime.now());
//        System.out.println(list);
//
//        ResBean lastResBean = RuntimeBusiness.getInstance().loadLastRes(88L, 86L, 0);
//        System.out.println(lastResBean);


//        String nowday = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        DBTool.update("DELETE FROM runtime WHERE createtime='" + nowday + " 00:00:00'");
//        RuntimeBusiness.getInstance().generate(88, null);

    }

    public static void main11(String[] args) {
//        ProgramBean bean = ProgramBusiness.getInstance().load(218L, 88L, 2);
//        System.out.println(GSON.toJson(bean));

        LocalTime time = LocalTime.parse("23:58:01");
        System.out.println(time.plusSeconds(216));

        ResVo resVo = WowzaRuntimeBusiness.getInstance().blankVo();
        //System.out.println(GSON.toJson(resVo));


        ChannelBean channel = ChannelBusiness.getInstance().load(85);
        channel.setStarttime(LocalTime.parse("00:00"));
        channel.setEndtime(LocalTime.parse("15:15"));
        LocalTime now = LocalTime.now();
        if (now.isAfter(channel.getStarttime()) && now.isBefore(channel.getEndtime())) {
            System.out.println(channel);
        }

    }

    ///uploads/system/2017/11/27/LRIS5lbD.mp3
    public static void main9(String[] args) {
        LocalTime now = LocalTime.now().withNano(0);
        String text = HttpClient.getInstance().urlGet("http://localhost:8080/api/vlive/playitem/next?channel_id=83&prev=mp3:system/2017/11/27/zuOewcgh.mp3");
        System.out.println(text);
    }

    public static void main10(String[] args) {

        LocalTime start = LocalTime.parse("11:22:56");
        LocalTime end = LocalTime.parse("11:22:59");
        System.out.println(end.until(start, ChronoUnit.SECONDS));

        long diff = ChronoUnit.SECONDS.between(start, end);
        System.out.println(diff);

        System.out.println(LocalTime.parse("09:11"));

        LocalTime time = LocalTime.parse("11:36:01");
        System.out.printf("time=");
        System.out.println(time.plusSeconds(216).plusSeconds(314).plusSeconds(184).plusSeconds(185));

        LocalTime time2 = LocalTime.parse("11:36:01.556");
        System.out.println(time2.plusSeconds(1).isAfter(LocalTime.now()));

    }

    public static void main6(String[] args) {
        ManagerBean user = ManagerBusiness.getInstance().load(1L);
        System.out.println(GSON.toJson(user));

        for (int i = 0; i < 100; i++) {
            int j = RandomUtils.nextInt(120883, 121049);
            System.out.println(j);
        }


    }

    public static void main5(String[] args) {
        //WowzaRuntimeBusiness.getInstance().reload();
        LocalTime now = LocalTime.now().withNano(0);
        LocalTime lt = LocalTime.parse("11:18:56");
        System.out.println(now.toSecondOfDay());
        System.out.println(lt.toSecondOfDay());
        long diff = ChronoUnit.SECONDS.between(now, lt);
        System.out.println(diff);

        LocalTime start = LocalTime.parse("11:22:56");
        LocalTime end = LocalTime.parse("11:20:59");
        LocalTime time = LocalTime.parse("11:21:57");

        System.out.println((time.isAfter(start) && time.isBefore(end)) || (time.isAfter(end) && time.isBefore(start)));

        Period p = Period.of(2018, 12, 13);
        System.out.println(p);
        System.out.println(p.negated());

        System.out.println(p.getDays());
        System.out.println(Instant.now());

        System.out.println(4500 * 0.064);
    }

    public static void main4(String[] args) {
//      DBTool.update("DELETE FROM runtime WHERE createtime='2017-12-07 00:00:00'");
//      RuntimeBusiness.getInstance().generate(80, null);
        String nowday = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nowtime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(nowday);
        System.out.println(nowtime);

        HttpServletRequest request;

    }

    public void try_with_resources() {
        File file = new File("./tmp.txt");
        try (FileInputStream inputStream = new FileInputStream(file);) {
            // use the inputStream to read a file
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static boolean isGoodJson(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            System.out.println("bad json: " + json);
            return false;
        }
    }

    public static void main3(String[] args) throws Exception {
        List<ResBean> resBeanList = DBTool.list(ResBean.class, "SELECT lifetime, createtime, lifetime FROM respository");
        Long time;
        List<Long> ids = new ArrayList<>();


        for (ResBean resBean : resBeanList) {
            time = resBean.getCreatetime().getTime() + resBean.getLifetime() * 60 * 60 * 1000 * 24;
            if (new Timestamp(System.currentTimeMillis()).getTime() < time) {// 已经过期，添加到删除列表
                ids.add(resBean.getId());
            }
        }
        // 删除所有的过期文件
        if (ids.size() != 0 || ids != null) {
            DBTool.update("delete from respository where id in(" + SQL.toInString(ids) + ")");
        }
//        ChannelBean bean = ChannelBusiness.getInstance().load(69);
//        ChannelBusiness.getInstance().attachShim(bean);
//        System.out.println(GSON.toJson(bean));
//        List<ResBean> list = ResBusiness.getInstance().listByTags(204L, -1, Collections.singletonList(1001200));
//        System.out.println(list);

        //RuntimeBusiness.getInstance().generate(80L,new Timestamp(System.currentTimeMillis()));
        //RuntimeBusiness.getInstance().generate(77L,new Timestamp(System.currentTimeMillis()));
        //RuntimeBusiness.getInstance().generate(68L,new Timestamp(System.currentTimeMillis()));

//        Long.parseLong("1509724800000")

//        long r = 1510329600000L;
//        LocalDate localDate = new Date(r).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        System.out.println(localDate);

//        List<Long> ids = DBTool.column(Long.class, "SELECT res_id FROM res_tag_custom");
//        System.out.println(ids);
//
//        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 0, 0, 0);
//        System.out.println(localDateTime);
//
//        LocalDateTime localDateTime2 = LocalDate.now().atTime(0, 0, 0);
//        System.out.println(localDateTime2);

//        QueryRunner runner = new QueryRunner(new ComboPooledDataSource());
//        List<Object>list = runner.query("select * from account where money>?", new ColumnListHandler(3),500);
//        System.out.println(list);


//        ChannelBean cbean = ChannelBusiness.getInstance().load(0L);
//        cbean = Optional.ofNullable(cbean).orElse(new ChannelBean());
//
//        ChannelBean channelBean = Objects.requireNonNull(cbean);
//        String str = Objects.toString(cbean);
//        System.out.println(str);

//        String wave = DBTool.field(String.class, "SELECT wave FROM respository_file_meta WHERE id=?", 9);
//
//        System.out.println(wave);

//        ManagerBean mbean = ManagerBusiness.getInstance().load(201);
//        ManagerBusiness.getInstance().attachRoleBeanList(mbean);
//        ManagerBusiness.getInstance().attachUrlList(mbean);
//        System.out.println(GSON.toJson(mbean.getUrlList()));
//        mbean.getUrlList().clear();
//        mbean.getUrlList().add("/resp/res_*");

//        System.out.println(mbean.getUrlList());
//        boolean pass = false;
//        pass = ManagerBusiness.getInstance().allowVisit(mbean, "/he.do");
//        System.out.println(pass);


//        pass = ManagerBusiness.getInstance().allowVisit(mbean, "/vlive/buin/res_file.xhtml");
//        System.out.println(pass);
//
//        pass = ManagerBusiness.getInstance().allowVisit(mbean, "/tag_group_list/push.do");
//        System.out.println(pass);

//        pass = ManagerBusiness.getInstance().allowVisit(mbean, "/resp/res_edit.xhtml");
//        System.out.println(pass);


//        ManagerBean bean = new ManagerBean();
//
//        Optional.empty().ifPresent(x-> System.out.println("op"));
//        Optional.of(bean).map(ManagerBean::getRoleList).ifPresent(System.out::println);
//        String savedData = DBTool.field(String.class, "SELECT wave FROM respository_file_meta WHERE id=?", 12);
//        System.out.println("savedData:" + savedData);


//        String mp3 = "/Users/pysh/Documents/PiaoYangGuoHai.mp3";
//        File file = new File(mp3);
//        file.createNewFile();
//        String result = ProcessExecutor.getInstance().executeFileWave(mp3);
//        System.out.println(result);
//          sdf


//        ColumnCreatorBean ccbean = new ColumnCreatorBean();
//        ccbean.setName("");
//        System.out.println(ResBusiness.getInstance().isEmptyBean(ccbean));


//        List<EnumValue.Role> roles = new ArrayList<>(Arrays.asList(EnumValue.Role.values()));
//        int role_id = 2;
//        int ordinal = EnumValue.Role.instances(role_id).ordinal();
//        roles.removeIf(x -> x.ordinal() <= ordinal);
//        roles.forEach(x -> System.out.println(x.getName() + " " + x.ordinal()));

//        ColumnCreatorBean ccbean = new ColumnCreatorBean();
//        ccbean.setName("f");
//        System.out.println(ResBusiness.getInstance().isEmptyBean(ccbean));

//        System.out.println(list.contains(integer));

//        System.out.println(LocalTime.parse("05:01:02",DateTimeFormatter.ofPattern("HH:mm:ss")));
        // System.out.println(LocalTime.now().withHour(0).withNano(0).withMinute(2).withSecond(1));
//        System.out.println(Duration.ofMinutes(1).plusSeconds(15).getSeconds());

//        Duration duration = Duration.ofSeconds(61);

//        System.out.println(LocalTime.ofSecondOfDay(91).getSecond());
//        System.out.println(LocalTime.ofSecondOfDay(91).getMinute());
        //System.out.println(duration.get(ChronoUnit.SECONDS));
//        System.out.println(Duration.ofSeconds(61).getSeconds());

//        String[] arr = {"a", "b", "c"};
//        List<Integer> integers = Arrays.asList(5, 6, 7);
//        List<Long> longs = Arrays.asList(8L, 9L);
//        List<? extends Serializable> list = Arrays.asList("ty", LocalDate.now());

    }


    public String value(String property) {
        return "";
    }

    public static void main2(String[] args) {
//        System.out.println(Utils.nowDateString());
//        long r = DBTool.insert("INSERT INTO file(creator_content) VALUES(?)", "{}");
//        System.out.println(r);
//        LocalDateTime ldt = Utils.parse("2017-02-18 15:22:00", Utils.DATEFORMAT1);
//        Timestamp ts = Timestamp.valueOf(ldt);
//        System.out.println(ts);


        String srcFile = "/Users/pysh/project/电台/WowzaRecorder/doc/jingji_1600_1730_3XIJVlFf.m4a";
        String text = ProcessExecutor.getInstance().executeFileInfo(srcFile);
        System.out.println(text);

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
                String name = comp.substring(0, idx);
                String value = comp.substring(idx + 1);
                System.out.println("--name=" + name.trim() + "--value=" + value.trim());
            }
        }
        //
        String v = "13:11:01.67";
        System.out.println(LocalTime.parse(v).toSecondOfDay());
        //Duration dp = Duration.parse(v);
        //System.out.println(dp);


    }
}

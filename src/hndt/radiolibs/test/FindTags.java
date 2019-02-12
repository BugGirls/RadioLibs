package hndt.radiolibs.test;

import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindTags {
    public static void main(String[] args) {

        //1001042 李美熹、萧敬腾、孙楠、黄绮珊、陶喆 - 漂洋过海来看你, 拉丁舞曲 - 伦巴+桑巴+牛仔+斗牛
        //1001078 拉丁舞曲 - 牛仔伦巴恰恰
        //1001200 拉丁舞曲 - 牛仔伦巴恰恰
        new FindTags().listByTags(204L, 1, Arrays.asList(1001042, 1001078, 1001200));
    }

    public List<ResBean> listByTags(Long manager_id, int category, List<Integer> tags) {

        StringBuilder find_conditions = new StringBuilder("AND ( ");
        for (int i = 0; i < tags.size(); i++) {
            find_conditions.append("FIND_IN_SET(" + tags.get(i) + ", r.type_tags) ");
            if (i + 1 < tags.size()) {
                find_conditions.append(" OR ");
            }
        }
        find_conditions.append(")");
        String find_conditions_custom = find_conditions.toString().replace("r.type_tags", "c.type_tags");
        SQL sql = SQL.of("SELECT r.* FROM respository r WHERE").or("r.system=",1,"r.manager_id=", manager_id).and("r.category=", category).append(find_conditions.toString())
                .append(" UNION ").append("SELECT r.* FROM respository r,res_tag_custom c WHERE r.id=c.res_id").or("r.system=",1, "r.manager_id=", manager_id).and("r.category=", category).append(find_conditions_custom);

        List<ResBean> list = DBTool.list(ResBean.class, sql.sql(), sql.params());
        for (ResBean resBean : list) {
            System.out.println(resBean.getId() + " , " + resBean.getTitle_proper());
        }

        return list;
    }
}

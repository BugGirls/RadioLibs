package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.ProgramBean;
import hndt.radiolibs.biz.ProgramBusiness;
import hndt.radiolibs.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出Excel
 *
 * @author Hystar
 * @date 2019/3/18
 */
@WebServlet("/manager/file/export_excel")
public class ExportExcelServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String programId = request.getParameter("programId");

        ProgramBean programBean = ProgramBusiness.getInstance().load(Long.parseLong(programId));

        // 获取数据
        List list = new ArrayList<>();

        //excel标题
        String[] title = {"名称","性别","年龄","学校","班级"};

        //excel文件名
        String fileName = "学生信息表"+System.currentTimeMillis()+".xls";

        //sheet名
         String sheetName = "学生信息表";

        String[][] content = new String[title.length][title.length];
        for (int i = 0; i < 1; i++) {
//            PageData obj = list.get(i);
            content[i][0] = "empress";
            content[i][1] = "男";
            content[i][2] = "20";
            content[i][3] = "1";
            content[i][4] = "2";
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

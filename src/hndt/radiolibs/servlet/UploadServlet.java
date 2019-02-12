package hndt.radiolibs.servlet;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.bean.RoleBean;
import hndt.radiolibs.biz.AppBusiness;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.IDGen;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hndt.radiolibs.util.Utils.DATEFORMAT3;
import static hndt.radiolibs.util.Utils.DATEFORMAT4;
import static hndt.radiolibs.util.Utils.DATEFORMAT5;


@WebServlet("/manager/file/*")
public class UploadServlet extends BaseServlet {
    String baseDir = "/uploads";

    /**
     * 删除文件
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = null;
        String realPath = request.getSession().getServletContext().getRealPath("");
        Path path = Paths.get(realPath, request.getParameter("uri"));
        if (Files.exists(path)) {
            Files.delete(path);
            json = Utils.asJsonMap("result", 1);
        }
        return json;
    }

    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    String put(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = null;
        String actionPath = request.getSession().getServletContext().getRealPath("");
        List<Map> result = new ArrayList<>();

        // 设置上传文件的路径，资源库管理员上传的文件保存在/upload/system/yyyyMMdd/目录下，频率管理员上传的文件保存在/upload/netradio/yyyyMMdd/目录下
        if (request.getParameter("system").equals("1")) {// 频率管理员上传
            baseDir = "/uploads/system";
        } else if (request.getParameter("system").equals("0")) {// 资料库管理员上传
            baseDir = "/uploads/netradio";
        }
        String imageUploadDir = baseDir + "/" + LocalDateTime.now().getYear() + "/" + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getDayOfMonth() + "/";

        Path path = Paths.get(actionPath, imageUploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
        try {
            List<FileItem> list = fileUpload.parseRequest(request);
            for (FileItem fileItem : list) {
                if (!fileItem.isFormField()) {
                    String fileOriginalName = fileItem.getName();
                    String fileSuffix = fileOriginalName.substring(fileOriginalName.lastIndexOf(".") + 1, fileOriginalName.length());
                    String fileNewName = IDGen.id() + "." + fileSuffix;
                    File file = new File(actionPath + imageUploadDir, fileNewName);
                    fileItem.write(file);

                    Map fileInfoMap = Utils.asHashMap("result", 1, "size", fileItem.getSize(), "name", fileNewName, "type", fileItem.getContentType(), "url", imageUploadDir + fileNewName);
                    result.add(fileInfoMap);
                    Logger.info(GSON.toJson(fileInfoMap));

                    /**
                    // 判断上传的文件采样率是否为44100Hz或44.1KHz的文件
                    if(ResBusiness.getInstance().is44100HZ(file.getPath())) {
                        Map fileInfoMap = Utils.asHashMap("result", 1, "size", fileItem.getSize(), "name", fileNewName, "type", fileItem.getContentType(), "url", imageUploadDir + fileNewName);
                        result.add(fileInfoMap);
                        Logger.info(GSON.toJson(fileInfoMap));
                    } else {// 如果上传的文件采样率不是44100HZ，则删除上传的文件
                        if (Files.exists(path)) {
                            Utils.deleteFile(file.getPath());
                        }
                        return null;
                    }**/
                }
            }
            json = (GSON.toJson(result));
        } catch (Exception e) {
            Logger.error(e);
        }
        return json;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType(contentTypeJson);
        response.setCharacterEncoding(utf8);
        req.setCharacterEncoding(utf8);
        PrintWriter out = response.getWriter();
        String uri = req.getRequestURI();
        String text = "";

        if (uri.endsWith("/put")) {
            text = put(req, response);
        } else if (uri.endsWith("/delete")) {
            text = delete(req, response);
        }

        out.print(text);// 返回json数据
        out.close();
    }

}

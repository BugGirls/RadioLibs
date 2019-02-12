package hndt.radiolibs.util;

import hndt.radiolibs.biz.EnumValue;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@WebServlet(name = "DownloadLogServlet", urlPatterns = {"/log/*"})
public class DownloadLogServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/json; charset=utf-8");
        String uri = request.getRequestURI();
        if (!"/log/latest".equals(uri)) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            return;
        }

        Path logsPath = Paths.get(System.getProperty("catalina.base"), "logs", "/");
        Path path = logsPath.resolve("catalina.out");
        if (!Files.exists(path)) {
            path = Files.list(logsPath).filter(f -> {
                String filename = f.getFileName().toString();
                return filename.startsWith("catalina") && filename.endsWith("log");
            }).sorted((Path p1, Path p2) -> {
                return p1.toFile().lastModified() > p1.toFile().lastModified() ? -1 : 1;
            }).findFirst().orElse(null);
        }
        if (path == null) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            return;
        }

        File cfile = path.toFile();
        if (!cfile.exists()) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            return;
        }

        int readLines = 599;
        PrintWriter out = response.getWriter();

        List<String> lines = new ArrayList<>(readLines);
        RandomAccessFile rf = new RandomAccessFile(cfile, "r");
        long length = rf.length();
        long pos = length - 1;
        while (pos > 0) {
            pos--;
            rf.seek(pos);
            if (rf.readByte() == 10) {
                lines.add(rf.readLine());
                if (lines.size() >= readLines) {
                    break;
                }
            }
        }
        if (pos == 0) {
            rf.seek(0);
            lines.add(rf.readLine());
        }
        rf.close();
        Collections.reverse(lines);
        out.println("============================" + cfile.getAbsolutePath() + " " + Utils.nowTimeString() + "============================");
        for (String line : lines) {
            String t = new String(line.getBytes("ISO-8859-1"), EnumValue.ENCODING_UTF8);
            out.println(t);
        }
        out.close();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    }

}

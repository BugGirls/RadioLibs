package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.biz.AppBusiness;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ProcessExecutor;
import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.IDGen;
import hndt.radiolibs.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class ResFileController extends ResController {
    String info;// 上传文件的信息

    public ResFileController() {
        if (bean != null) {
            //ResBusiness.getInstance().attachWaveData(bean);
        }
    }

    /**
     * 文件上传完成后执行的异步操作
     */
    public void onInfoChange() {
        // 只能上传一个文件，因此list只有一个值
        List<FileInfo> list = GSON.toList(info, FileInfo.class);
        FileInfo map = list.get(0);
        bean.setPath(map.getUrl());
        bean.setSize((map.getSize()));
        ResBusiness.getInstance().durationAndBitrate(AppBusiness.getInstance().getRealRootPath() + bean.getPath(), bean);
        //String data = ProcessExecutor.getInstance().executeFileWave(bean.getPath());
        //bean.setWaveData(data);
    }

    public void setSystem() {
        if (bean != null) {
            bean.setSystem(1);
        }
    }

    public void save() {
        int r = ResBusiness.getInstance().saveFile(bean);
        addTip(r);
        tab(bean, EnumValue.Section.FILE);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    class FileInfo {
        String url;
        Long size;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

    }
}



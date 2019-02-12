package hndt.radiolibs.util;


import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageBean implements Serializable {
    int linesPerPage = 20; //每页的条数
    int page = 1; //当前页码
    List list; //当前页数据
    int pages = 0; //总页数
    long rows = 0; //总条数

    String prev;
    String next;

    Map<String, Object> extra = new HashMap<>();

    public PageBean() {
    }

    public PageBean(int linesPerPage) {
        this.linesPerPage = linesPerPage;
    }

    public PageBean(int linesPerPage, int page) {
        this.linesPerPage = linesPerPage;
        this.page = page;
    }

    public int getPages() {
        if (rows > 0) {
            pages = (int) ((rows % linesPerPage) == 0 ? (rows / linesPerPage) : (rows / linesPerPage) + 1);
        }
        return pages == 0 ? 1 : pages;
    }

    public long getStart() {
        long start = linesPerPage * (page - 1);
        return start < 0 ? 0 : start;
    }

    public int getLinesPerPage() {
        return linesPerPage;
    }

    public void setPage(int page) {
        if (page < 1) {
            page = 1;
        }
        if (page > pages && pages > 0) {
            page = pages;
        }
        this.page = page;
    }

    public void next() {
        if (page < pages) {
            page = page + 1;
        }
    }

    public void previous() {
        if (page > 0) {
            page = page - 1;
        }
    }

    public int getPage() {
        return page;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(long rows) {
        this.rows = rows;
        getPages();
        if (page > pages) {
            page = pages;
        }
    }

    public List getList() {
        return list;
    }

    public <T> List<T> getList(T t) {
        return (List<T>) list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

}

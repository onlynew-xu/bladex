package com.steelman.iot.platform.entity;

import com.github.pagehelper.Page;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class Pager<T> {
    private int page;//分页起始页
    private int size;//每页记录数
    private List<T> rows;//返回的记录集合
    private long total;//总记录条数
    private int pages;
    //是否为第一页
    private boolean isFirstPage = false;
    //是否为最后一页
    private boolean isLastPage = false;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


    /**
     * @description: 包装Page对象
     * @author: lsj
     * @time: 2021/4/1 0001 16:55
     */
    public Pager(List<T> list) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.page = page.getPageNum();
            this.size = page.getPageSize();

            this.pages = page.getPages();
            this.rows = page;
            this.total = page.getTotal();
        } else if (list instanceof Collection) {
            this.page = 1;
            this.size = list.size();
            this.pages = 1;
            this.rows = list;
            this.total = list.size();
        }
        if (list instanceof Collection) {
            //判断页面边界
            judgePageBoudary();
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = page == 1;
        isLastPage = page == pages;
    }
}

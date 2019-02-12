/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hndt.radiolibs.util;

import java.util.LinkedHashMap;
import java.util.Map;

//Least Recently Used
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private int capacity;
    private static final long serialVersionUID = 1L;

    //传入指定的缓存最大容量
    public LRULinkedHashMap(int capacity) {
        //初始容量、每次加载因子、是否使用访问顺序排序
        super(99, 0.75f, true);
        this.capacity = capacity;
    }

    //实现LRU的关键方法，如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

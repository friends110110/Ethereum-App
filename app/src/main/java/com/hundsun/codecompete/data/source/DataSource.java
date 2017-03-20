package com.hundsun.codecompete.data.source;

import com.hundsun.codecompete.data.Session;

/**
 * Created by Administrator on 2016/7/31.
 */
public interface DataSource {
    /**
     * 初始化数据：1.mock 2.从数据库中拿
     * 并且给相应的赋值
     */
    public Session getSession();
}

package com.hundsun.codecompete.utils;

import com.hundsun.codecompete.data.Session;
import com.hundsun.codecompete.data.source.local.Repository;
import org.ethereum.service.JsonRPC;

/**
 * Created by Administrator on 2016/7/30.
 */
public class RefreshDataUtils {

    /**
     * 根据session信息，刷新以太币方面的数据 以太币、投票币等等
     * 根据 wallet获取其它数据,例如 以太币、投票积分等
     * 可被调用 以更新
     */
    public static void refreshSession() {
        Session session= Repository.getInstance().getSession();
        String etheCoin = JsonRPC.eth_getBalance(session.getAddress());
        session.setEtheCoin(etheCoin);
    }
}

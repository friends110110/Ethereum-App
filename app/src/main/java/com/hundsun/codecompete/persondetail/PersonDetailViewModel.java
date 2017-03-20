package com.hundsun.codecompete.persondetail;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.hundsun.codecompete.base.BaseViewModel;
import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.data.Session;
import com.hundsun.codecompete.data.source.local.Repository;
import com.hundsun.codecompete.utils.RefreshDataUtils;

/**
 * Created by Administrator on 2016/7/29.
 */
public class PersonDetailViewModel extends BaseObservable implements BaseViewModel{
    Session session;
    public PersonDetailViewModel(){
    }
    public String getPortraitUrl(){
        return session.getPortraitUrl();
    }

    public Session getSession() {
        return session;
    }

    /**
     * 显示账号名称名称，如果没有就显示个人信息四个字
     * @return 账号名称
     */
    public String getAccountName(){
        String accountName=session.getAccountName();
        return accountName==null? ConstantValue.VALUE_PERSON_INFO:accountName;
    }
    public void modifyAccountName(String accountName) {
        session.setAccountName(accountName);
    }
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if(imageUrl!=null){
            Glide.with(view.getContext()).load(imageUrl).into(view);
        }
    }

    public void loginOut() {
        Repository.getInstance().clearSession();
    }

    @Override
    public void start() {
        //初始化时 将用户上下文信息构造session
        session= Repository.getInstance().getSession();
    }

    public void refreshData() {
        RefreshDataUtils.refreshSession();
        notifyChange();
    }
}

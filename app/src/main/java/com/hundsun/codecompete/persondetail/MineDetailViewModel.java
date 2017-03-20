package com.hundsun.codecompete.persondetail;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.hundsun.codecompete.QR.GenerateQRActivity;
import com.hundsun.codecompete.base.BaseViewModel;
import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.data.Session;
import com.hundsun.codecompete.data.source.local.Repository;
import com.hundsun.codecompete.persondetail.moremenu.AccountDetailActivity;
import com.hundsun.codecompete.utils.ForwardUtils;

/**
 * Created by Administrator on 2016/7/29.
 */
public class MineDetailViewModel extends BaseObservable implements BaseViewModel{
    Session session;
    public MineDetailViewModel(){

    }

    public Session getSession() {
        return session;
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if(imageUrl!=null){
            Glide.with(view.getContext()).load(imageUrl).into(view);
        }
    }

    @Override
    public void start() {
        //初始化时 将用户上下文信息构造session
        session= Repository.getInstance().getSession();
        if(session.getAccountName()==null){
            session.setAccountName(ConstantValue.VALUE_PERSON_INFO);
        }
    }

    /**
     * activity_mine_detail.xml中 @{detailViewModel.onClickMineQR}
     * @param view
     */
    public void onClickMineQR(View view){
      String address= session.getAddress();
      Intent intent=new Intent();
      intent.putExtra(ConstantValue.QR_GENERATE_CODE_FLAG,address);
      ForwardUtils.forward(view.getContext(), GenerateQRActivity.class,intent);
    }

    /**
     *
     * @param view
     */
    public void onClickAccountDetail(View view){
        ForwardUtils.forward(view.getContext(), AccountDetailActivity.class,null);
    }
}

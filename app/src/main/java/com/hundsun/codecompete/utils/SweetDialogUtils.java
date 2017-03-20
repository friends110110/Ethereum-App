package com.hundsun.codecompete.utils;

import android.view.View;
import com.hundsun.codecompete.R;

import android.content.Context;
import android.graphics.Color;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/7/31.
 */
public class SweetDialogUtils {
    /**
     * 确认对话框的回调
     */
    public interface ConfirmDialog {
        void confirmCallback();
    }
    /**
     * show the loading dialog
     * @param context
     */
    public static SweetAlertDialog createLoadingDialog(Context context, boolean isShow){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        return pDialog;
    }

    /**
     * show the confirm dialog
     * @param context
     */
    public static SweetAlertDialog showConfirmDialog(Context context, boolean isShow, final ConfirmDialog confirmCallback){
        SweetAlertDialog pDialog =new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(context.getString(R.string.a_u_sure_tip))
                .setContentText(context.getString(R.string.login_out_user_tip))
                .setConfirmText(context.getString(R.string.login_out_tip))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        confirmCallback.confirmCallback();
                        sDialog.dismissWithAnimation();
                    }
                }).setCancelText(context.getString(R.string.cancel))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
        pDialog.show();
        return pDialog;
    }

    public static SweetAlertDialog showSuccessDialog(Context context){
        SweetAlertDialog pDialog=new SweetAlertDialog(context)
                .setTitleText("Good job!")
                .setContentText("登陆成功!");
        pDialog.show();
        pDialog.findViewById(R.id.confirm_button).setVisibility(View.GONE);
        return pDialog;
    }
}

package com.hundsun.codecompete.utils;

import com.hundsun.codecompete.R;
import com.hundsun.codecompete.data.source.local.Repository;
import com.hundsun.codecompete.login.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/29.
 */
public class ForwardUtils {
    public static void forward(Context context, Class activityClazz, Intent intent){
        //请先登录
        if (null==Repository.getInstance().getSession()&&activityClazz!= LoginActivity.class){
            Toast.makeText(context,context.getString(R.string.please_login_first_tips),Toast.LENGTH_LONG).show();
            return;
        }
        if (null==intent){
            intent=new Intent();
        }
        intent.setClass(context,activityClazz);
        context.startActivity(intent);
    }

    public static void forwardForResult(Activity activity, Class activityClazz, Intent intent, int requestCode) {
        //请先登录
        if (null==Repository.getInstance().getSession()&&activityClazz!= LoginActivity.class){
            Toast.makeText(activity,activity.getString(R.string.please_login_first_tips),Toast.LENGTH_LONG).show();
            return;
        }
        if (null==intent){
            intent=new Intent();
        }
        intent.setClass(activity,activityClazz);
        activity.startActivityForResult(intent,requestCode);
    }
}

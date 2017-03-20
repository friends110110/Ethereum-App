package com.hundsun.codecompete.main;

import android.content.ContentResolver;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.hundsun.codecompete.QR.CustomeQRActivity;
import com.hundsun.codecompete.QR.GenerateQRActivity;
import com.hundsun.codecompete.R;
import com.hundsun.codecompete.base.BaseActivity;
import com.hundsun.codecompete.common.view.imageroll.RollViewPager;
import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.hundsun.codecompete.databinding.ActivityMainBinding;
import com.hundsun.codecompete.ethetransfer.EtheTransferActivity;
import com.hundsun.codecompete.login.LoginActivity;
import com.hundsun.codecompete.persondetail.MineDetailActivity;
import com.hundsun.codecompete.utils.ForwardUtils;
import com.hundsun.codecompete.utils.LogUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding activityMainBinding;
    RollViewPager rollViewPager;


    @Override
    protected void initView() {
        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
//        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //点击头像跳到登录页面
        activityMainBinding.navView.getHeaderView(0).findViewById(R.id.portrait_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardUtils.forward(MainActivity.this, LoginActivity.class,null);
            }
        });

        rollViewPager= (RollViewPager) findViewById(R.id.roll_viewpage);

        initRollImages();
    }



    @Override
    protected void displayHomeAsUpEnabled() {
    }

    private void initRollImages(){
        int resImage[]=new int[]{R.mipmap.scene1,R.mipmap.scene2,R.mipmap.scene3};
        LinearLayout dotsLl= (LinearLayout) findViewById(R.id.dots_ll);
        rollViewPager.setData(this,dotsLl,resImage, new RollViewPager.OnPagerClickCallback() {
            @Override
            public void onPagerClick(int position) {
                LogUtils.i(String.valueOf(position));
            }
        });
//        rollViewPager.startRoll();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ethe_transfer) {
            ForwardUtils.forward(this, EtheTransferActivity.class,null);
            // Handle the camera action
        } else if (id == R.id.nav_product_suggest) {

        } else if (id == R.id.nav_manage) {//用户个人设置
            //如果还未登录提示用户先登录，以后打开
//            if (null==UserInfo.getInstance().getSession()){
//                Snackbar.make(this.getWindow().getDecorView(),getString(R.string.please_login_first_tips),Snackbar.LENGTH_LONG).show();
//            }else{
                ForwardUtils.forward(this, MineDetailActivity.class,null);
//            }
        } else if (id == R.id.nav_share) {//分享

        } else if (id == R.id.nav_about) {//关于

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        rollViewPager.startRoll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rollViewPager.stopRoll();
    }

    /**
     * 菜单相关操作
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_qr_scan) {
            ForwardUtils.forwardForResult(this, CustomeQRActivity.class,null, ConstantValue.REQUEST_CODE);
        }else if(id==R.id.action_generate_qr){
            Intent intent=new Intent();
            intent.putExtra(ConstantValue.QR_GENERATE_CODE_FLAG,"王芳");
            ForwardUtils.forward(this, GenerateQRActivity.class,intent);
        }
        else if(id==R.id.action_settings){

        }else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    protected void op(MenuItem item) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == ConstantValue.REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        /**
         * 选择系统图片并解析
         */
        else if (requestCode == ConstantValue.REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                    CodeUtils.analyzeBitmap(mBitmap, new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

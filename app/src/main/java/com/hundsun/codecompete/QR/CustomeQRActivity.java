package com.hundsun.codecompete.QR;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.hundsun.codecompete.R;
import com.hundsun.codecompete.base.BaseActivity;
import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 定制化显示扫描界面
 */
public class CustomeQRActivity extends BaseActivity {


    @Override
    protected void initView() {
        setContentView(R.layout.activity_custome_qr);
        CaptureFragment captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

    }


    /**
     * 菜单相关操作
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qr_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_select_from_album){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, ConstantValue.REQUEST_IMAGE);
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                return true;
//            case R.id.action_select_from_album:
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                startActivityForResult(intent, ConstantValue.REQUEST_IMAGE);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 选择系统图片并解析
         */
        if (requestCode == ConstantValue.REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                    CodeUtils.analyzeBitmap(mBitmap, new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(CustomeQRActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                            bundle.putString(CodeUtils.RESULT_STRING, result);
                            resultIntent.putExtras(bundle);
                            CustomeQRActivity.this.setResult(RESULT_OK, resultIntent);
                            CustomeQRActivity.this.finish();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(CustomeQRActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                            bundle.putString(CodeUtils.RESULT_STRING, "");
                            resultIntent.putExtras(bundle);
                            CustomeQRActivity.this.setResult(RESULT_OK, resultIntent);
                            CustomeQRActivity.this.finish();
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
    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CustomeQRActivity.this.setResult(RESULT_OK, resultIntent);
            CustomeQRActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CustomeQRActivity.this.setResult(RESULT_OK, resultIntent);
            CustomeQRActivity.this.finish();
        }
    };
}

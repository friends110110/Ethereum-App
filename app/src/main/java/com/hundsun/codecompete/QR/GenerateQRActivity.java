package com.hundsun.codecompete.QR;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.hundsun.codecompete.R;
import com.hundsun.codecompete.base.BaseActivity;
import com.hundsun.codecompete.data.ConstantValue.ConstantValue;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class GenerateQRActivity extends BaseActivity {

//    public EditText editText = null;
//    public Button button = null;
//    public Button button1 = null;
    public ImageView imageView = null;
    public Bitmap mBitmap = null;

    /**
     * 初始化组件
     */
    @Override
    protected void initView() {
        setContentView(R.layout.activity_generate_qr);
//        editText = (EditText) findViewById(R.id.edit_content);
//        button = (Button) findViewById(R.id.button_content);
//        button1 = (Button) findViewById(R.id.button1_content);
        imageView = (ImageView) findViewById(R.id.image_content);

        mBitmap = CodeUtils.createImage(getIntent().getStringExtra(ConstantValue.QR_GENERATE_CODE_FLAG), 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        imageView.setImageBitmap(mBitmap);
        /**
         * 生成二维码图片
         */
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String textContent = editText.getText().toString();
//                if (TextUtils.isEmpty(textContent)) {
//                    Toast.makeText(GenerateQRActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                editText.setText("");
//                mBitmap = CodeUtils.createImage(textContent, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//                imageView.setImageBitmap(mBitmap);
//            }
//        });

        /**
         * 生成不带logo的二维码图片
         */
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String textContent = editText.getText().toString();
//                if (TextUtils.isEmpty(textContent)) {
//                    Toast.makeText(GenerateQRActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                editText.setText("");
//                mBitmap = CodeUtils.createImage(textContent, 400, 400, null);
//                imageView.setImageBitmap(mBitmap);
//            }
//        });
    }



}

package com.hundsun.codecompete.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.cedarsoftware.util.io.JsonReader;
import com.hundsun.codecompete.R;
import com.hundsun.codecompete.base.BaseActivity;
import com.hundsun.codecompete.databinding.ActivityLoginBinding;
import com.hundsun.codecompete.utils.SweetDialogUtils;
import com.hundsun.codecompete.utils.ThreadPoolUtils;

import java.io.IOException;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    ActivityLoginBinding activityLoginBinding;
    LoginViewModel loginViewModel;
    private EditText mPasswordView;
    // UI references.
    private EditText mV3View;
    private View mProgressView;
    private View mLoginFormView;
    SweetAlertDialog sweetLoadingDialog;

    @Override
    protected void initView() {
        activityLoginBinding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        mV3View=activityLoginBinding.etV3str;
        mPasswordView=activityLoginBinding.password;
        mProgressView=activityLoginBinding.loginProgress;
        mLoginFormView=activityLoginBinding.loginForm;
        activityLoginBinding.restoreWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        activityLoginBinding.createNewWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){createNewWallet();}
        });
        activityLoginBinding.loginMyPrivateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                ThreadPoolUtils.execute(new Runnable() {
                    @Override
                    public void run() {
                        loginViewModel.loginMyPrivateKeyWallet();
                        Snackbar.make(mLoginFormView,getResources().getString(R.string.success_create_wallet_tips),Snackbar.LENGTH_LONG).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginSuccessDialog();
                            }
                        });
                    }
                });
            }
        });
        loginViewModel=new LoginViewModel();
        activityLoginBinding.setLoginViewModel(loginViewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginViewModel.start();
    }

    private void createNewWallet(){
        final EditText editText=new EditText(LoginActivity.this);
        new AlertDialog.Builder(LoginActivity.this).setTitle(getResources().getString(R.string.prompt_enter_secret)).setIcon(
                android.R.drawable.ic_dialog_info).setView(
                editText).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 弹框输入密码
                if(!loginViewModel.createWallet(editText.getText().toString())){
                    Snackbar.make(mLoginFormView,getResources().getString(R.string.fail_create_wallet_tips),Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(mLoginFormView,getResources().getString(R.string.success_create_wallet_tips),Snackbar.LENGTH_LONG).show();
                    loginSuccessDialog();
                }
            }
        }).setNegativeButton(getResources().getString(R.string.quit_create_secret), null).show();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mV3View.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String v3Str = mV3View.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(v3Str)) {
            mV3View.setError(getString(R.string.error_field_v3_required));
            focusView = mV3View;
            cancel = true;
        } else if (!isV3StrValid(v3Str)) {
            mV3View.setError(getString(R.string.error_invalid_v3str));
            focusView = mV3View;
            cancel = true;
        }

        if (!cancel &&TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_passwd_required));
            focusView=mPasswordView;
            cancel=true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    final boolean isRestoreSuccess=loginViewModel.restoreWallet(mV3View.getText().toString(),mPasswordView.getText().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!isRestoreSuccess){
                                Snackbar.make(mLoginFormView,getResources().getString(R.string.error_incorrect_password),Snackbar.LENGTH_LONG).show();
                                mPasswordView.setError(getString(R.string.error_incorrect_password));
                                showProgress(false);
                            }else{
                                Snackbar.make(mLoginFormView,getResources().getString(R.string.success_login_wallet_tips),Snackbar.LENGTH_LONG).show();
                                loginSuccessDialog();
                            }
                        }
                    });
                }
            });
        }
    }


    private boolean isV3StrValid(String v3Str) {
        //TODO: Replace this with your own logic
        try {
            Map json=JsonReader.jsonToMaps(v3Str);
            // 3. Check Version
            if (!"3".equals(json.get("version").toString())) {
                //Invalid Keyfile Version"
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        if (sweetLoadingDialog==null){
            sweetLoadingDialog=SweetDialogUtils.createLoadingDialog(this,show);
        }
        if(show==true){
            sweetLoadingDialog.show();
        }else{
            sweetLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (sweetLoadingDialog!=null){
            sweetLoadingDialog.dismiss();
        }
        if (successDialog!=null){
            successDialog.dismiss();
        }
        super.onDestroy();
    }
    SweetAlertDialog successDialog;
    private Handler mHandler=new Handler();

    /**
     * 登陆成功 闪出登陆成功框 然后 1000 毫秒后消失
     */
    public void loginSuccessDialog() {
        if (sweetLoadingDialog!=null){
            sweetLoadingDialog.dismiss();
        }
        successDialog=SweetDialogUtils.showSuccessDialog(LoginActivity.this);
        //1秒后消失
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                successDialog.dismiss();
                LoginActivity.this.finish();
            }
        },1000);
    }
}


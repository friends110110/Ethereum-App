package com.hundsun.codecompete.common.view;

import android.widget.EditText;
import com.hundsun.codecompete.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import com.hundsun.codecompete.utils.EditTextShakeUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class EnterPasswdDialogFragment extends DialogFragment{

    /**
     * 继承该接口，弹出对话框的操作
     */
    public interface Validate{
        //验证密码
        boolean validatePasswd(String passwd);
        //密码验证成功的下一步操作
        void nextStep();
    }
    private Validate validate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        validate= (Validate) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_frag_enter_passwd,null);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        final EditText editText= (EditText) view.findViewById(R.id.id_txt_password);
        view.findViewById(R.id.id_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate.validatePasswd(editText.getText().toString())){
                    validate.nextStep();
                    dialog.dismiss();
                }else{//密码错误
                    editText.setError(getString(R.string.error_incorrect_password));
                    EditTextShakeUtils editTextShakeUtils=new EditTextShakeUtils(getActivity());
                    editTextShakeUtils.shake(editText);
                }
            }
        });
        return dialog;
    }


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        return super.onCreateView(inflater, container, savedInstanceState);
////        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view=inflater.inflate(R.layout.dialog_frag_enter_passwd,null);
//        view.findViewById(R.id.id_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        return view;
//    }

}

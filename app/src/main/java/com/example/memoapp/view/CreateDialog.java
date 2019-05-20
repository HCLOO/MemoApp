package com.example.memoapp.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.memoapp.R;
import com.example.memoapp.database.DBManager;
import com.example.memoapp.listener.OnCreateTaskListener;

public class CreateDialog extends Dialog {

    private EditText editText;
    private TextView TVSubmit;
    private ImageView IVCancel;
    private OnCreateTaskListener listener;

    public CreateDialog(@NonNull Context context) {
        super(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_create,null);
        initView(view);
    }

    private void initView(View view) {
        setContentView(view);
        editText=(EditText) findViewById(R.id.ret_content);
        TVSubmit=(TextView) findViewById(R.id.tv_submit);
        IVCancel=(ImageView)findViewById(R.id.iv_cancel);
        TVSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager.getInstance().insertToInfoTable(editText.getText().toString());
                dismiss();
                listener.onCreateTask();
            }
        });
        IVCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setOnCreateTaskListener(OnCreateTaskListener listener) {
        this.listener=listener;
    }
}

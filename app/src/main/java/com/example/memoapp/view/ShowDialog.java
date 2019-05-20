package com.example.memoapp.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.memoapp.R;

public class ShowDialog extends Dialog {

    private TextView TVTitle;
    private TextView TVContent;
    private TextView TVDate;
    private ImageView IVCancel;

    public ShowDialog(@NonNull Context context) {
        super(context);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_show,null);
        initView(view);
    }

    private void initView(View view) {
        setContentView(view);
        TVTitle=(TextView)findViewById(R.id.tv_title);
        TVContent=(TextView)findViewById(R.id.tv_content);
        TVDate=(TextView)findViewById(R.id.tv_date);
        IVCancel=(ImageView)findViewById(R.id.iv_cancel);
        IVCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setShowTitle(String title) {
        TVTitle.setText(title);
    }

    public void setShowContent(String content) {
        TVContent.setText(content);
    }

    public void setShowDate(String date) {
        TVDate.setText(date);
    }
}

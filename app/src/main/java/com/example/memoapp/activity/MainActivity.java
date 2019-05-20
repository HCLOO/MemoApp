package com.example.memoapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import com.example.memoapp.R;
import com.example.memoapp.adapter.ShowAdapter;
import com.example.memoapp.bean.InfoBean;
import com.example.memoapp.database.DBManager;
import com.example.memoapp.listener.OnCreateTaskListener;
import com.example.memoapp.listener.OnItemChangeListener;
import com.example.memoapp.listener.OnShowTaskListener;
import com.example.memoapp.receiver.AlarmReceiver;
import com.example.memoapp.util.BarUtil;
import com.example.memoapp.view.ShowDialog;
import com.example.memoapp.view.SwipeRecyclerView;
import com.example.memoapp.view.CreateDialog;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView IVCreate;
    private CreateDialog createDialog;
    private ShowDialog showDialog;
    private SwipeRecyclerView recyclerView;
    private ShowAdapter adapter;
    private List<InfoBean> showList=new ArrayList<>();
    private final int INTERVAL_TIME=60000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BarUtil.setColor(this,getResources().getColor(R.color.barColor),0);

        showDialog=new ShowDialog(this);
        createDialog=new CreateDialog(this);
        createDialog.setOnCreateTaskListener(new OnCreateTaskListener() {
            @Override
            public void onCreateTask() {
                resetShowData();
            }
        });
        IVCreate=(ImageView)findViewById(R.id.iv_create);
        IVCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog.show();
            }
        });
        initShowData();
        recyclerView=(SwipeRecyclerView)findViewById(R.id.rv_show);
        adapter=new ShowAdapter(this, showList, new OnShowTaskListener() {
            @Override
            public void onShowTask(InfoBean bean) {
                showDialog.setShowTitle(getResources().getString(R.string.tv_task)+bean.getId());
                showDialog.setShowContent(bean.getContent());
                showDialog.setShowDate(bean.getDate());
                showDialog.show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemClickListener(new OnItemChangeListener() {

            @Override
            public void onShowClick(int position) {
                adapter.showItem(position);
            }

            @Override
            public void onMarkClick(int position) {
                adapter.markItem(position);
            }

            @Override
            public void onDeleteClick(int position) {
                adapter.removeItem(position);
            }
        });
        //开启通知闹钟
        startNotificationAlarm();
    }

    private void initShowData() {
        if(showList!=null) {
            showList.clear();
            showList.addAll(DBManager.getInstance().getInfoList());
        }
    }

    private void resetShowData() {
        if(showList!=null) {
            showList.clear();
            showList.addAll(DBManager.getInstance().getInfoList());
        }
        adapter.notifyDataSetChanged();
    }

    private void startNotificationAlarm() {
        Intent mIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, mIntent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarm!=null)
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL_TIME, sender);
        //param1：闹钟类型，param1：闹钟首次执行时间，param1：闹钟两次执行的间隔时间，param1：闹钟响应动作。
    }
}

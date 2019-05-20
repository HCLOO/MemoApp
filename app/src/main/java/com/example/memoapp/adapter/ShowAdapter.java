package com.example.memoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.example.memoapp.R;
import com.example.memoapp.bean.InfoBean;
import com.example.memoapp.database.DBManager;
import com.example.memoapp.listener.OnShowTaskListener;
import com.example.memoapp.util.DBConstant;
import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<InfoBean> beanList;
    private ShowViewHolder viewHolder;
    private OnShowTaskListener listener;

    public ShowAdapter(Context mContext, List<InfoBean> beans, OnShowTaskListener listener) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        beanList = beans;
        this.listener=listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShowViewHolder(mInflater.inflate(R.layout.recyclerview_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        viewHolder = (ShowViewHolder) holder;
        viewHolder.content.setText(beanList.get(position).getContent());
        viewHolder.title.setText(mContext.getResources().getString(R.string.tv_task)+beanList.get(position).getId());
        viewHolder.date.setText(beanList.get(position).getDate());
        if(beanList.get(position).getAttribute()== DBConstant.DONE) {
            Glide.with(mContext).load(R.drawable.finish).into(viewHolder.icon);
            viewHolder.attribute.setText(mContext.getResources().getString(R.string.tv_mark_no));
            viewHolder.attribute.setBackgroundColor(mContext.getResources().getColor(R.color.noMarkColor));
        } else {
            Glide.with(mContext).load(R.drawable.unfinish).into(viewHolder.icon);
            viewHolder.attribute.setText(mContext.getResources().getString(R.string.tv_mark_done));
            viewHolder.attribute.setBackgroundColor(mContext.getResources().getColor(R.color.markColor));
        }
    }

    @Override
    public int getItemCount() {
        return beanList != null ? beanList.size() : 0;
    }

    public void showItem(int position) {
        if(listener!=null)
            listener.onShowTask(beanList.get(position));
    }

    public void removeItem(int position) {
        DBManager.getInstance().deleteToInfoTable(beanList.get(position).getId());
        beanList.remove(position);
        notifyDataSetChanged();
    }
    public void markItem(int position){
        if(beanList.get(position).getAttribute()==DBConstant.DONE) {
            DBManager.getInstance().updateToInfoTable(beanList.get(position).getId(),DBConstant.DEFAULT);
            InfoBean bean = new InfoBean(beanList.get(position).getId(),beanList.get(position).getContent(),
                    DBConstant.DEFAULT,beanList.get(position).getDate());
            beanList.remove(position);
            beanList.add(bean);
        } else {
            DBManager.getInstance().updateToInfoTable(beanList.get(position).getId(),DBConstant.DONE);
            InfoBean bean = new InfoBean(beanList.get(position).getId(),beanList.get(position).getContent(),
                    DBConstant.DONE,beanList.get(position).getDate());
            beanList.remove(position);
            beanList.add(0,bean);
        }
        notifyDataSetChanged();
    }
}

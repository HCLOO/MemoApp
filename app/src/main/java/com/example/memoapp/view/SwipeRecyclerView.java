package com.example.memoapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.example.memoapp.R;
import com.example.memoapp.adapter.ShowViewHolder;
import com.example.memoapp.listener.OnItemChangeListener;

public class SwipeRecyclerView extends RecyclerView {
    private Context mContext;
    //上一次的触摸点
    private int mLastX, mLastY;
    //当前触摸的item的位置
    private int mPosition;
    //item对应的布局
    private LinearLayout mItemLayout;
    //删除按钮
    private TextView mDelete;
    //顶置状态按钮
    private TextView mAttribute;
    //查看按钮
    private TextView mShow;
    //最大滑动距离（删除+顶置的宽度）
    private int mMaxLength;
    //是否在垂直滑动列表
    private boolean isDragging;
    //item是在否跟随手指移动
    private boolean isItemMoving;

    //item是否开始自动滑动
    private boolean isStartScroll;
    //侧滑按钮状态   0：关闭 1：将要关闭 2：将要打开 3：打开
    private int mDeleteBtnState;

    //检测手指在滑动过程中的速度
    private VelocityTracker mVelocityTracker;

    private Scroller mScroller;
    //封装侧滑接口1、顶置 2、删除
    private OnItemChangeListener mListener;


    public SwipeRecyclerView(Context context) {
        this(context,null);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mScroller = new Scroller(context, new LinearInterpolator());
        mVelocityTracker = VelocityTracker.obtain();
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //将当前的移动事件传递给VelocityTracker
        mVelocityTracker.addMovement(e);
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果侧滑状态是关闭的
                if (mDeleteBtnState == 0) {
                    //获取点击对象的视图
                    View view = findChildViewUnder(x, y);
                    if (view == null) {
                        return false;
                    }

                    ShowViewHolder viewHolder = (ShowViewHolder) getChildViewHolder(view);
                    //获取外层item布局
                    mItemLayout = viewHolder.layout;
                    //获取布局position
                    mPosition = viewHolder.getAdapterPosition();
                    //删除按钮
                    mDelete = (TextView) mItemLayout.findViewById(R.id.tv_delete);
                    //顶置按钮
                    mAttribute = (TextView) mItemLayout.findViewById(R.id.tv_attribute);
                    //查看按钮
                    mShow = (TextView) mItemLayout.findViewById(R.id.tv_show);
                    //最大滑动距离
                    mMaxLength = mDelete.getWidth()+mAttribute.getWidth()+mShow.getWidth();

                    mDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onDeleteClick(mPosition);
                            //删除后 滑回开始位置
                            mItemLayout.scrollTo(0, 0);
                            mDeleteBtnState = 0;
                        }
                    });
                    mAttribute.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //分享后 滑回开始位置
                            mListener.onMarkClick(mPosition);
                            mItemLayout.scrollTo(0,0);
                            mDeleteBtnState = 0;
                        }
                    });

                    mShow.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mListener.onShowClick(mPosition);
                            mItemLayout.scrollTo(0,0);
                            mDeleteBtnState = 0;
                        }
                    });
                }
                //已经打开的状态
                else if (mDeleteBtnState == 3){
                    //用Scroller自动200时间内自动滑回起始位置
                    mScroller.startScroll(mItemLayout.getScrollX(), 0, -mMaxLength, 0, 200);
                    invalidate();
                    mDeleteBtnState = 0;
                    return false;
                }else{
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                int dy = mLastY - y;

                int scrollX = mItemLayout.getScrollX();
                if (Math.abs(dx) > Math.abs(dy)) {//左边界检测
                    isItemMoving = true;
                    if (scrollX + dx <= 0) {
                        mItemLayout.scrollTo(0, 0);
                        return true;
                    } else if (scrollX + dx >= mMaxLength) {//右边界检测
                        //当滑动的距离大于最大距离,停留在最大距离处
                        mItemLayout.scrollTo(mMaxLength, 0);
                        return true;
                    }
                    mItemLayout.scrollBy(dx, 0);//item跟随手指滑动
                }
                break;
            case MotionEvent.ACTION_UP:

                isItemMoving = false;

                mVelocityTracker.computeCurrentVelocity(1000);//计算手指滑动的速度
                float xVelocity = mVelocityTracker.getXVelocity();//水平方向速度（向左为负）
                float yVelocity = mVelocityTracker.getYVelocity();//垂直方向速度

                int deltaX = 0;
                int upScrollX = mItemLayout.getScrollX();

                if (Math.abs(xVelocity) > 100 && Math.abs(xVelocity) > Math.abs(yVelocity)) {
                    if (xVelocity <= -100) {//左滑速度大于100，则删除按钮显示
                        deltaX = mMaxLength - upScrollX;
                        mDeleteBtnState = 2;
                    } else if (xVelocity > 100) {//右滑速度大于100，则删除按钮隐藏
                        deltaX = -upScrollX;
                        mDeleteBtnState = 1;
                    }
                } else {
                    if (upScrollX >= mMaxLength / 2) {//item的左滑动距离大于删除按钮宽度的一半，则显示删除按钮
                        deltaX = mMaxLength - upScrollX;
                        mDeleteBtnState = 2;
                    } else if (upScrollX < mMaxLength / 2) {//否则隐藏
                        deltaX = -upScrollX;
                        mDeleteBtnState = 1;
                    }
                }

                //item自动滑动到指定位置
                mScroller.startScroll(upScrollX, 0, deltaX, 0, 200);
                isStartScroll = true;
                invalidate();

                mVelocityTracker.clear();
                break;
        }

        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(e);
    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mItemLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else if (isStartScroll) {
            isStartScroll = false;
            if (mDeleteBtnState == 1) {
                mDeleteBtnState = 0;
            }

            if (mDeleteBtnState == 2) {
                mDeleteBtnState = 3;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        isDragging = state == SCROLL_STATE_DRAGGING;
    }

    public void setOnItemClickListener(OnItemChangeListener listener) {
        mListener = listener;
    }
}

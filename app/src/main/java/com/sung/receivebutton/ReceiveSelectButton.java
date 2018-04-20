package com.sung.receivebutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by sung on 2018/4/11.
 * 进行中带进度
 */

public class ReceiveSelectButton extends CardView {
    public static final String TAG = ReceiveSelectButton.class.getSimpleName();

    public static final int SELECT_STATUS_UNFINISH = 0;
    public static final int SELECT_STATUS_CONTINUE = 1;
    public static final int SELECT_STATUS_FINISH = 2;
    public static final int SELECT_STATUS_WAIT = 3;
    public static final int SELECT_STATUS_AVALIABLE = 4;
    /**
     * 未完成  status=0 alpha=1 color=gray
     * 进行中  status=1 alpha=1 color=gray
     * 已完成  status=2 alpha=1 color=gray
     * 等待中  status=3 alpha=0.5 color=gray
     * 可领取  status=4 alpha=1 color=orange
     * */
    private int mode;
    /**
     * 透明度
     * */
    private float alpha = 1f;
    /**
     * 背景默认色
     * */
    private String back_color = "#eeeeee";
    /**
     * 前景进度色
     * */
    private String front_color = "#f19a5a";
    /**
     * 文字颜色
     * */
    private String text_color = "#ffffff";
    /**
     * 文字
     * */
    private String text = "未完成";
    /**
     * 字体大小
     * */
    private float text_size = 14f;
    /**
     * 当前进度/总进度
     * */
    private float current_progress = 0;
    private float total_progress = 100;
    /**
     * 是否圆角
     * */
    private boolean as_corner = false;
    /**
     * 画笔
     * */
    private Paint basePaint;
    private Paint frontPaint;
    private Paint textPaint;
    /**
     * view宽高
     * */
    private int width = 0;
    private int height = 0;
    private RectF mRectF;

    private OnProgressComplete onProgressComplete;

    private boolean countTime = false;
    private android.os.Handler countHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            current_progress = current_progress + 1;
            postInvalidate();
            if (current_progress >= total_progress){
                countTime = false;
                if (onProgressComplete != null){
                    onProgressComplete.onFullProgress();
                }
            }
            Log.e("progress count", "counting: "+current_progress );
        }
    };

    public ReceiveSelectButton(Context context) {
        super(context);
    }

    public ReceiveSelectButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReceiveSelectButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(){
        mode = SELECT_STATUS_UNFINISH;
        basePaint = new Paint();
        basePaint.setStyle(Paint.Style.FILL);
        frontPaint = new Paint();
        frontPaint.setStyle(Paint.Style.FILL);
        textPaint = new TextPaint();
        textPaint.setTextSize(text_size);
        textPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (basePaint == null || frontPaint == null || textPaint == null){
            initView();
        }

        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            mRectF = new RectF(0,0,width,height);
            text_size = height/2;
            textPaint.setTextSize(text_size);
        }

        textPaint.setColor(Color.parseColor(text_color));
        frontPaint.setColor(Color.parseColor(front_color));
        basePaint.setColor(Color.parseColor(back_color));

        int dynamic_progress = (int) (width / total_progress * current_progress);

        //背景底色
        canvas.drawRect(mRectF,basePaint);
        //未完成
        if (mode == SELECT_STATUS_UNFINISH){
            text = "未完成";
            textPaint.setColor(Color.parseColor("#28000000"));
        }
        //进行中
        if (mode == SELECT_STATUS_CONTINUE){
            text = "进行中";
            textPaint.setColor(Color.parseColor("#f19a5a"));
            frontPaint.setColor(Color.parseColor("#7ef19a5a"));
            canvas.drawRect(new RectF(0,0,dynamic_progress,height),frontPaint);
        }
        //已完成
        if (mode == SELECT_STATUS_FINISH){
            text = "已完成";
            textPaint.setColor(Color.parseColor("#28000000"));
        }
        //等待中
        if (mode == SELECT_STATUS_WAIT){
            text = "等待中";
            textPaint.setColor(Color.parseColor("#28000000"));
        }
        //可领取
        if (mode == SELECT_STATUS_AVALIABLE){
            text = "可领取";
            text_color = "#ffffff";
            canvas.drawRect(mRectF,frontPaint);
        }

        // 计算Baseline绘制的起点X轴坐标 ，计算方式：画布宽度的一半 - 文字宽度的一半
        int baseX = (int) (canvas.getWidth() / 2 - textPaint.measureText(text) / 2);
        // 计算Baseline绘制的Y坐标 ，计算方式：画布高度的一半 - 文字总高度的一半
        int baseY = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        // 画文字
        canvas.drawText(text,baseX,baseY,textPaint);
    }

    /**
     * 更新显示状态
     * */
    public void updateStatus(int status){
        this.mode = status;
        stopCountTime();
        postInvalidate();
    }

    /**
     * 更新前景颜色
     * */
    public void setFrontColor(String front_color) {
        this.front_color = front_color;
        postInvalidate();
    }

    /**
     * 更新进行中进度
     * */
    public void setCurrentProgress(float current_progress) {
        this.current_progress = current_progress;
        postInvalidate();
    }

    public void setTotalProgress(float total_progress){
        this.total_progress = total_progress;
        postInvalidate();
    }

    public int getStatus() {
        return mode;
    }

    public boolean isCountting(){
        return countTime;
    }

    /**
     * 开始走进度条
     * */
    public void startCountTime(){
        countTime = true;
        new Thread(new TimerCountRunnable()).start();
    }

    public void stopCountTime(){
        countTime = false;
        if (countHandler != null){
            countHandler.removeCallbacksAndMessages(null);
        }
        if (onProgressComplete != null){
            onProgressComplete.onStopProgress();
        }
    }

    class TimerCountRunnable implements Runnable {
        @Override
        public void run() {
            while (countTime){
                try {
                    Thread.sleep(1000);
                    if (countHandler != null)
                        countHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    Log.e("InterruptedException", "run: "+e.toString() );
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        countTime = false;
        if (countHandler != null){
            countHandler.removeCallbacksAndMessages(null);
            countHandler = null;
        }
    }

    public interface OnProgressComplete{
        void onFullProgress();
        void onStopProgress();
    }

    public void addOnProgressCompleteListener(OnProgressComplete onProgressComplete){
        this.onProgressComplete = onProgressComplete;
    }
}

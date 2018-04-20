package com.sung.receivebutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextPaint;
import android.util.AttributeSet;
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
    private float current_progress = 50;
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

        if (as_corner){
            //背景底色
            canvas.drawRoundRect(mRectF,height/2,height/2,basePaint);
            //未完成
            if (mode == SELECT_STATUS_UNFINISH){
                text = "未完成";
            }
            //进行中
            if (mode == SELECT_STATUS_CONTINUE){
                text = "进行中";
                textPaint.setColor(Color.parseColor("#f19a5a"));
                frontPaint.setColor(Color.parseColor("#7ef19a5a"));
                canvas.drawRoundRect(mRectF,height/2,height/2,frontPaint);
            }
            //已完成
            if (mode == SELECT_STATUS_FINISH){
                text = "已完成";
            }
            //等待中
            if (mode == SELECT_STATUS_WAIT){
                text = "等待中";
            }
            //可领取
            if (mode == SELECT_STATUS_AVALIABLE){
                text = "可领取";
                text_color = "#ffffff";
                canvas.drawRoundRect(mRectF,height/2,height/2,frontPaint);
            }
        }else {
            //背景底色
            canvas.drawRect(mRectF,basePaint);
            //未完成
            if (mode == SELECT_STATUS_UNFINISH){
                text = "未完成";
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
            }
            //等待中
            if (mode == SELECT_STATUS_WAIT){
                text = "等待中";
            }
            //可领取
            if (mode == SELECT_STATUS_AVALIABLE){
                text = "可领取";
                text_color = "#ffffff";
                canvas.drawRect(mRectF,frontPaint);
            }
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
}

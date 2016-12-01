package liutaw.com.understandview.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2016/12/1.
 */

public class RoundClockView extends View {

    private Paint mRoundPaint;//外圆线
    private Paint mCenterPointPaint;//中心正方形
    private Paint mSmallScalePaint;//小刻度
    private Paint mBigScalePaint;//大刻度
    private Paint mTextPaint;//文字
    private Paint mHourHandPaint;//时针
    private Paint mMinuteHandPaint;//分针

    private int hour = -1;//1-12 小时，注意范围,默认时间12点整
    private int minute = -1;//0-60 分钟，注意范围.默认时间12点整

    public RoundClockView(Context context) {
        super(context);
        init();
    }

    public RoundClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRoundPaint = new Paint();
        mRoundPaint.setColor(Color.parseColor("#000000"));
        mRoundPaint.setStyle(Paint.Style.STROKE);
        mRoundPaint.setStrokeWidth(dp2px(2));

        mCenterPointPaint = new Paint();
        mCenterPointPaint.setColor(Color.parseColor("#000000"));
        mCenterPointPaint.setStyle(Paint.Style.FILL);


        mBigScalePaint = new Paint();
        mBigScalePaint.setColor(Color.parseColor("#000000"));
        mBigScalePaint.setStyle(Paint.Style.STROKE);
        mBigScalePaint.setStrokeWidth(dp2px(4));

        mSmallScalePaint = new Paint();
        mSmallScalePaint.setColor(Color.parseColor("#000000"));
        mSmallScalePaint.setStyle(Paint.Style.STROKE);
        mSmallScalePaint.setStrokeWidth(dp2px(2));

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#000000"));
        mTextPaint.setTextSize(sp2px(8));

        mHourHandPaint = new Paint();
        mHourHandPaint.setColor(Color.parseColor("#000000"));
        mHourHandPaint.setStyle(Paint.Style.FILL);
        mHourHandPaint.setStrokeWidth(dp2px(2));

        mMinuteHandPaint = new Paint();
        mMinuteHandPaint.setColor(Color.parseColor("#000000"));
        mMinuteHandPaint.setStyle(Paint.Style.FILL);
        mMinuteHandPaint.setStrokeWidth(dp2px(1));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int realWidth;
        int realHeight;

        if (widthMode == MeasureSpec.EXACTLY) {
            realWidth = widthSize;
        } else {
            realWidth = dp2px(150);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            realHeight = heightSize;
        } else {
            realHeight = dp2px(150);
        }

        setMeasuredDimension(realWidth, realHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画出圆形仪表盘
        canvas.save();
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        drawRoundLine(canvas);
        drawCenterPoint(canvas);
        drawBigScale(canvas);
        drawSmallScale(canvas);
        //默认时间12点整
        drawHourMinuteHand(canvas, hour, minute);
    }


    private int radius;

    private void drawRoundLine(Canvas canvas) {
        canvas.save();

        radius = getRoundRadius();
        canvas.drawCircle(0.0f, 0.0f, radius, mRoundPaint);

        canvas.restore();
    }

    private void drawCenterPoint(Canvas canvas) {
        canvas.save();

        int rectRadius = dp2px(4);
        canvas.drawRect(-rectRadius, -rectRadius, rectRadius, rectRadius, mCenterPointPaint);

        canvas.restore();
    }

    private void drawBigScale(Canvas canvas) {
        drawBigScaleByAngle(canvas, 0, "12点");
        drawBigScaleByAngle(canvas, 90, "3点");
        drawBigScaleByAngle(canvas, 180, "6点");
        drawBigScaleByAngle(canvas, 270, "9点");
    }

    private float maxHourhandLength = 0.0f;//为了计算距离中心点还有多少距离，方便计算时针的长度
    private float maxMinuteHandLength = 0.0f;

    private void drawBigScaleByAngle(Canvas canvas, float angle, String text) {
        canvas.save();
        canvas.rotate(angle);
        float bigScaleWidth = dp2px(8);
        float textLineDividerWidth = dp2px(4);
        float lineYStart = radius - bigScaleWidth;
        float lineYEnd = radius;
        canvas.drawLine(0.0f, -lineYEnd, 0.0f, -lineYStart, mBigScalePaint);
//        String text = "12点";
        float textWidth = mTextPaint.measureText(text);//计算宽度
        float textHeight = getTextHeight(mTextPaint, text);//计算高度
        float textX = textWidth / 2;
        float textY = radius - bigScaleWidth - textLineDividerWidth - textHeight / 2;
        if (maxMinuteHandLength == 0.0f) {
            maxMinuteHandLength = radius - bigScaleWidth - textLineDividerWidth - textHeight;
            maxMinuteHandLength -= dp2px(2);//留出一点空隙，美观

            maxHourhandLength = maxMinuteHandLength * 3 / 5;//顺便计算一下时针的长度
        }
        canvas.drawText(text, -textX, -textY, mTextPaint);

        canvas.restore();
    }


    private void drawSmallScale(Canvas canvas) {

        drawSmallScaleByAngle(canvas, 30.0f, "1");
        drawSmallScaleByAngle(canvas, 60.0f, "2");

        drawSmallScaleByAngle(canvas, 120.0f, "4");
        drawSmallScaleByAngle(canvas, 150.0f, "5");

        drawSmallScaleByAngle(canvas, 210.0f, "7");
        drawSmallScaleByAngle(canvas, 240.0f, "8");

        drawSmallScaleByAngle(canvas, 300.0f, "10");
        drawSmallScaleByAngle(canvas, 330.0f, "11");

    }

    private void drawSmallScaleByAngle(Canvas canvas, float angle, String text) {
        canvas.save();
        canvas.rotate(angle);

        float smallScaleWidth = dp2px(6);
        float textLineDividerWidth = dp2px(4);
        float lineYStart = radius - smallScaleWidth;
        float lineYEnd = radius;
        //此处实际要改为负数，因为要翻转
        canvas.drawLine(0.0f, -lineYEnd, 0.0f, -lineYStart, mSmallScalePaint);
        float textWidth = mTextPaint.measureText(text);
        float textHeight = getTextHeight(mTextPaint, text);
        float textX = textWidth / 2;
        float textY = radius - smallScaleWidth - textLineDividerWidth - textHeight / 2;
        canvas.drawText(text, -textX, -textY, mTextPaint);

        canvas.restore();
    }

    private void drawHourHand(Canvas canvas, float angle) {
        canvas.save();
        canvas.rotate(angle);

        canvas.drawLine(0.0f, 0.0f, 0.0f, -maxHourhandLength, mHourHandPaint);

        canvas.restore();

    }

    private void drawMinuteHand(Canvas canvas, float angle) {

        canvas.save();
        canvas.rotate(angle);

        canvas.drawLine(0.0f, 0.0f, 0.0f, -maxMinuteHandLength, mMinuteHandPaint);

        canvas.restore();
    }

    private void drawHourMinuteHand(Canvas canvas, int hour, int minute) {

        canvas.save();
        if (hour > 12 || hour < -1 || minute < -1 || minute > 60) {
            //时间非法 不刷新
            Log.e(getClass().getSimpleName(), "hour must from 0~12 and minute must from 0~60,please check!");
            return;
        }

        //根据hour计算旋转的角度,hour从1-12，也就是将360度12等分，每等分30度
        //如果你不想要默认值，可以将-1从这里去掉
        if (hour == 12 || hour == -1) {
            drawHourHand(canvas, 0);
        } else {
            drawHourHand(canvas, hour * 30);
        }

        //根据minute计算旋转的角度,minute从0-60，也就是将360度60等分，每等分6度
        if (minute == 60 || minute == -1) {
            drawMinuteHand(canvas, 0);
        } else {
            drawMinuteHand(canvas, minute * 6);
        }

        canvas.restore();
    }


    private int getRoundRadius() {
        int halfWidth = getMeasuredWidth();
        int halfHeight = getMeasuredHeight();
        int recommendedRadius = Math.min(halfHeight, halfWidth) / 4;
        return recommendedRadius;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        this.invalidate();
    }


    protected final int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    protected final int sp2px(int sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics());
    }

    protected final int getTextHeight(Paint textPaint, String text) {
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds.height();
    }
}

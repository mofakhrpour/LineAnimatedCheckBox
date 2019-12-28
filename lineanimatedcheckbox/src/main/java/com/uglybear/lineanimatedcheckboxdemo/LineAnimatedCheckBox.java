package com.uglybear.lineanimatedcheckbox;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.AnimationUpdateListener;
import com.richpathanimator.RichPathAnimator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LineAnimatedCheckBox extends LinearLayout {

    private RichPathView richPathView;
    private TextView textView;
    private boolean isChecked = false;
    private float vectorScaleRate;

    public LineAnimatedCheckBox(Context context) {
        super(context);

        initView();
    }

    public LineAnimatedCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public LineAnimatedCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {

        setBackgroundResource(android.R.color.transparent);
        setPadding(0, dpToPx(6), 0, dpToPx(6));

        richPathView = new RichPathView(getContext());
        LinearLayout.LayoutParams layoutParams = new LayoutParams(dpToPx(20), dpToPx(20));
        layoutParams.setMargins(dpToPx(6), 0, dpToPx(6), 0);
        richPathView.setLayoutParams(layoutParams);
        richPathView.setVectorDrawable(R.drawable.checkbox_unchecked);

        richPathView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecked(!isChecked());
            }
        });

        addView(richPathView);

        textView = new TextView(getContext());
        LinearLayout.LayoutParams textLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textLayoutParams);
        textView.setText("CheckBox Title");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        textView.setTextColor(getResources().getColor(android.R.color.black));

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecked(!isChecked());
            }
        });

        addView(textView);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecked(!isChecked());
            }
        });
    }

    private int pxToDp(double px) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dp = (float) px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    private int dpToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int spToPx(int sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private void updateStrokeWidth(RichPath richPath, float strokeWidth){

        try {
            richPath.setStrokeWidth(strokeWidth * vectorScaleRate);

            Method method = richPath.getClass().getDeclaredMethod("updatePaint");
            method.setAccessible(true);
            method.invoke(richPath);
        }catch (Exception ignored){}

    }

    private float getVectorScaleRate(RichPath richPath) {

        try {
            Field field = richPath.getClass().getDeclaredField("paint");
            field.setAccessible(true);
            Paint paint = (Paint) field.get(richPath);

            return paint.getStrokeWidth() / richPath.getStrokeWidth();
        }catch (Exception ignored){}

        return 0;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        if (isChecked != checked) {

            if (checked) {

                richPathView.setVectorDrawable(R.drawable.checkbox_unchecked);

                RichPath rectBackground = richPathView.findRichPathByName("path_1");
                RichPath rectMain = richPathView.findRichPathByName("path_3");
                RichPath tick = richPathView.findRichPathByName("path_2");

                vectorScaleRate = getVectorScaleRate(rectBackground);

                RichPathAnimator

                        //rectMain
                        .animate(rectMain)
                        .interpolator(new AccelerateDecelerateInterpolator())
                        .trimPathEnd(0, 0.9f)
                        .duration(377)

                        .andAnimate(rectMain)
                        .interpolator(new AccelerateDecelerateInterpolator())
                        .trimPathStart(0, 0.9f)
                        .startDelay(100)
                        .duration(365)

                        .andAnimate(rectMain)
                        .interpolator(new AccelerateDecelerateInterpolator())
                        .custom(new AnimationUpdateListener() {
                            @Override
                            public void update(RichPath richPath, float v) {
                                updateStrokeWidth(richPath, v);
                            }
                        }, 2, 5)
                        .duration(173)

                        // rectBackground
                        .andAnimate(rectBackground)
                        .interpolator(new DecelerateInterpolator())
                        .strokeColor(Color.parseColor("#8e8e8e"), Color.parseColor("#000000"))
                        .startDelay(448)
                        .duration(363)

                        .andAnimate(rectBackground)
                        .interpolator(new AccelerateDecelerateInterpolator())
                        .custom(new AnimationUpdateListener() {
                            @Override
                            public void update(RichPath richPath, float v) {
                                updateStrokeWidth(richPath, v);
                            }
                        }, 2, 5)
                        .duration(173)

                        // tick
                        .andAnimate(tick)
                        .trimPathEnd(0, 1)
                        .startDelay(365)
                        .duration(446)
                        .interpolatorSet(new LinearOutSlowInInterpolator())

                        .andAnimate(tick)
                        .trimPathStart(0, 0.23f)
                        .startDelay(465)
                        .duration(326)
                        .interpolatorSet(new LinearOutSlowInInterpolator())
                        .start();

            }
            else
            {

                richPathView.setVectorDrawable(R.drawable.checkbox_checked);

                RichPath rectBackground = richPathView.findRichPathByName("path_1");
                RichPath rectMain = richPathView.findRichPathByName("path_3");
                RichPath tick = richPathView.findRichPathByName("path_2");

                vectorScaleRate = getVectorScaleRate(rectBackground);

                RichPathAnimator

                        // tick
                        .animate(tick)
                        .trimPathStart(0.23f, 0)
                        .duration(285)
                        .interpolatorSet(new AccelerateInterpolator())

                        .andAnimate(tick)
                        .trimPathEnd(1, 0)
                        .duration(411)
                        .interpolatorSet(new AccelerateInterpolator())

                        //rectMain
                        .andAnimate(rectMain)
                        .interpolator(new AccelerateDecelerateInterpolator())
                        .startDelay(627)
                        .custom(new AnimationUpdateListener() {
                            @Override
                            public void update(RichPath richPath, float v) {
                                updateStrokeWidth(richPath, v);
                            }
                        }, 5, 2)
                        .duration(173)

                        .andAnimate(rectMain)
                        .interpolator(new AccelerateDecelerateInterpolator())
                        .trimPathEnd(0.9f, 0)
                        .startDelay(395)
                        .duration(307)

                        // rectBackground
                        .andAnimate(rectBackground)
                        .interpolator(new DecelerateInterpolator())
                        .strokeColor(Color.parseColor("#000000"), Color.parseColor("#8e8e8e"))
                        .duration(10)

                        .andAnimate(rectBackground)
                        .interpolator(new AccelerateDecelerateInterpolator())
                        .startDelay(627)
                        .custom(new AnimationUpdateListener() {
                            @Override
                            public void update(RichPath richPath, float v) {
                                updateStrokeWidth(richPath, v);
                            }
                        }, 5, 2)
                        .duration(173)

                        .start();
            }

        }

        isChecked = checked;
    }

    public String getText() {
        return textView.getText().toString();
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public TextView getTextView() {
        return textView;
    }
}

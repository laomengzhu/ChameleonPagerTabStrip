package com.fan.chameleonpagertabstrip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

public class ChameleonTextView extends TextView {

    private LinearGradient mLinearGradient;
    private int mViewWidth = 0;

    public ChameleonTextView(Context context) {
        super(context);
    }

    public ChameleonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = w;
        }
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //set paint shader
        getPaint().setShader(mLinearGradient);
        super.onDraw(canvas);
    }

    public void freshTranslateData(int startColor, int endColor, float progress) {
        mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0, new int[]{startColor, startColor, endColor}, new float[]{0.0f, progress, progress}, Shader.TileMode.CLAMP);
        invalidate();
    }
}


package net.yrom.widget;

import net.yrom.widget.switchbutton.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class SwitchButton extends View {

    private Bitmap    mBackground, mBtnSlip;
    private Resources mResources;
    private Rect      rectOff, rectOn;
    private Matrix    mMatrix;
    private Paint     mPaint;
    // current position x of btn slip
    private float     curPosX;
    // position btnSlip could slipping to;
    private float     btnOnPos, btnOffPos;
    // state : true-> on | false -> off
    private boolean   isSlipping, curSwitchState, preSwitchState;
    private int       mClickTimeout;

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.SwitchButtonStyle);

    }

    public SwitchButton(Context context) {
        this(context, null);
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {
        mResources = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton, defStyle, 0);
        int backResId = a.getResourceId(R.styleable.SwitchButton_background, R.drawable.btn_background);
        int btnResId = a.getResourceId(R.styleable.SwitchButton_btnSlip, R.drawable.btn_slip);
        boolean defSwitchState = a.getBoolean(R.styleable.SwitchButton_switchState, true);
        preSwitchState = curSwitchState = defSwitchState;
        setImageResources(backResId, btnResId);
        a.recycle();
        mMatrix = new Matrix();
        mPaint = new Paint();
        mClickTimeout = ViewConfiguration.getPressedStateDuration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBackground.getWidth(), mBackground.getHeight());
    }

    public void setImageResources(int background, int btnSlip) {
        mBackground = BitmapFactory.decodeResource(mResources, background);
        mBtnSlip = BitmapFactory.decodeResource(mResources, btnSlip);

        // btn slipping left--> switch off
        rectOff = new Rect(0, 0, mBtnSlip.getWidth(), mBtnSlip.getHeight());
        rectOn = new Rect(mBackground.getWidth() - mBtnSlip.getWidth(), 0, mBackground.getWidth(), mBtnSlip.getHeight());
        btnOffPos = mBtnSlip.getWidth() / 2;
        btnOnPos = mBackground.getWidth() - mBtnSlip.getWidth() / 2;
    }

    @Override
    public boolean performClick() {
        updateSwitchState(!curSwitchState);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            curPosX = event.getX();
            isSlipping = true;
            break;
        case MotionEvent.ACTION_MOVE:
            curPosX = event.getX();
            break;
        case MotionEvent.ACTION_UP:
            long time = event.getEventTime() - event.getDownTime();

            isSlipping = false;

            // btn slip over middle, as switch on.
            boolean over = curPosX > mBackground.getWidth() / 2;
            curSwitchState = over;
            if (stateChangedlistener != null && curSwitchState != preSwitchState) {

                stateChangedlistener.onSwitchStateChanged(curSwitchState);
                preSwitchState = curSwitchState;
            } else if (time < mClickTimeout) {
                performClick();
            }
            break;

        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(mBackground, mMatrix, mPaint);
        float btnSlipLeft = 0;
        if (isSlipping) {
            if (curPosX > btnOnPos) {
                btnSlipLeft = rectOn.left;
            } else if (curPosX < btnOffPos) {
                btnSlipLeft = rectOff.left;
            } else {
                btnSlipLeft = curPosX - mBtnSlip.getWidth() / 2;
            }
        } else {
            if (curSwitchState) {
                // on
                btnSlipLeft = rectOn.left;
            } else {
                // off
                btnSlipLeft = rectOff.left;
            }
        }
        canvas.drawBitmap(mBtnSlip, btnSlipLeft, rectOff.top, mPaint);
    }

    public void setOnSwitchStateChangedListener(OnSwitchStateChangedListener listener) {
        this.stateChangedlistener = listener;
    }

    public interface OnSwitchStateChangedListener {

        /**
         * btn slipping left -> switch off -> state==false<br>
         * state : true-> on | false -> off
         * 
         * @param state
         */
        void onSwitchStateChanged(boolean state);
    }

    private OnSwitchStateChangedListener stateChangedlistener;

    public boolean isSlipping() {
        return isSlipping;
    }

    public void updateSwitchState(boolean state) {
        curSwitchState = state;
        if (stateChangedlistener != null && curSwitchState != preSwitchState) {
            stateChangedlistener.onSwitchStateChanged(curSwitchState);
            preSwitchState = curSwitchState;
        }
        invalidate();
    }

    public boolean isSwitchOn() {
        return curSwitchState;
    }

}

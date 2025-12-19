package ir.microsign.colorpicker.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.microsign.R;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;
import ir.microsign.utility.view;



public class ColorPickerView extends LinearLayout {
    final View viewHue;
    final SquareView viewSatVal;
    final ImageView viewCursor;
    final ImageView viewAlphaCursor;
    final View viewOldColor;
    final View viewNewColor;
    final View viewAlphaOverlay;
    final ImageView viewTarget;
    final ImageView viewAlphaCheckered;
    final ViewGroup viewContainer;
    final float[] currentColorHsv = new float[3];
    //	final AlertDialog dialog;
    private final boolean supportsAlpha;
    OnColorPickerListener listener;
    View mRootView = null;
    int alpha;
    int mColor = -1;

    /**
     * Create an AmbilWarnaDialog.
     *
     * @param context  activity context
     * @param color    current color
     * @param listener an OnAmbilWarnaListener, allowing you to get back error or OK
     */
    public ColorPickerView(final Context context, int color, OnColorPickerListener listener) {
        this(context, color, false, listener);
    }

    /**
     * Create an AmbilWarnaDialog.
     *
     * @param context       activity context
     * @param color         current mColor
     * @param supportsAlpha whether alpha/transparency controls are enabled
     * @param listener      an OnAmbilWarnaListener, allowing you to get back error or OK
     */

    public ColorPickerView(final Context context, int color, boolean supportsAlpha, OnColorPickerListener listener) {
        super(context);

        mColor = color;
        mRootView = view.getLayoutInflater(context).inflate(R.layout.layout_color_picker, this);

        this.supportsAlpha = supportsAlpha;
        this.listener = listener;

        if (!supportsAlpha) { // remove alpha if not supported
            mColor = mColor | 0xff000000;
        }

        Color.colorToHSV(mColor, currentColorHsv);
        alpha = Color.alpha(mColor);


        viewHue = mRootView.findViewById(R.id.img_viewHue);
        viewSatVal = (SquareView) mRootView.findViewById(R.id.sqr_SatBri);
        viewCursor = (ImageView) mRootView.findViewById(R.id.img_cursor);
        viewOldColor = mRootView.findViewById(R.id.oldColor);
        viewNewColor = mRootView.findViewById(R.id.newColor);
        viewTarget = (ImageView) mRootView.findViewById(R.id.img_target);
        viewContainer = (ViewGroup) mRootView.findViewById(R.id.rl_viewContainer);
        viewAlphaOverlay = mRootView.findViewById(R.id.overlay);
        viewAlphaCursor = (ImageView) mRootView.findViewById(R.id.img_alphaCursor);
        viewAlphaCheckered = (ImageView) mRootView.findViewById(R.id.img_alphaCheckered);
//		{ // hide/show alpha
//			viewAlphaOverlay.setVisibility(supportsAlpha? View.VISIBLE: View.GONE);
//			viewAlphaCursor.setVisibility(supportsAlpha? View.VISIBLE: View.GONE);
//			viewAlphaCheckered.setVisibility(supportsAlpha? View.VISIBLE: View.GONE);
//		}
        view.setVisibility(findViewById(R.id.rl_transparent), supportsAlpha);
        viewSatVal.setHue(getHue());
        viewOldColor.setBackgroundColor(mColor);
        viewNewColor.setBackgroundColor(mColor);

        viewHue.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP) {

                    float y = event.getY();
                    if (y < 0.f) y = 0.f;
                    if (y > viewHue.getMeasuredHeight()) {
                        y = viewHue.getMeasuredHeight() - 0.001f; // to avoid jumping the cursor from bottom to top.
                    }
                    float hue = 360.f - 360.f / viewHue.getMeasuredHeight() * y;
                    if (hue == 360.f) hue = 0.f;
                    setHue(hue);

                    // update mRootView
                    viewSatVal.setHue(getHue());
                    moveCursor();
                    viewNewColor.setBackgroundColor(getColor());
                    updateAlphaView();
                    return true;
                }
                return false;
            }
        });

        if (supportsAlpha) viewAlphaCheckered.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_MOVE)
                        || (event.getAction() == MotionEvent.ACTION_DOWN)
                        || (event.getAction() == MotionEvent.ACTION_UP)) {

                    float y = event.getY();
                    if (y < 0.f) {
                        y = 0.f;
                    }
                    if (y > viewAlphaCheckered.getMeasuredHeight()) {
                        y = viewAlphaCheckered.getMeasuredHeight() - 0.001f; // to avoid jumping the cursor from bottom to top.
                    }
                    final int a = Math.round(255.f - ((255.f / viewAlphaCheckered.getMeasuredHeight()) * y));
                    ColorPickerView.this.setAlpha(a);

                    // update mRootView
                    moveAlphaCursor();
                    int col = ColorPickerView.this.getColor();
                    int c = a << 24 | col & 0x00ffffff;
                    viewNewColor.setBackgroundColor(c);
                    return true;
                }
                return false;
            }
        });
        viewSatVal.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP) {

                    float x = event.getX(); // touch event are in dp units.
                    float y = event.getY();

                    if (x < 0.f) x = 0.f;
                    if (x > viewSatVal.getMeasuredWidth()) x = viewSatVal.getMeasuredWidth();
                    if (y < 0.f) y = 0.f;
                    if (y > viewSatVal.getMeasuredHeight()) y = viewSatVal.getMeasuredHeight();

                    setSat(1.f / viewSatVal.getMeasuredWidth() * x);
                    setVal(1.f - (1.f / viewSatVal.getMeasuredHeight() * y));

                    // update mRootView
                    moveTarget();
                    viewNewColor.setBackgroundColor(getColor());

                    return true;
                }
                return false;
            }
        });

//		addView(mRootView);
        Text.setText(findViewById(R.id.txt_ok), ir.microsign.R.string.ok, Font.TextPos.h1);
        Text.setText(findViewById(R.id.txt_cancel), ir.microsign.R.string.cancel, Font.TextPos.h1);
        Text.setText(findViewById(R.id.txt_title), R.string.color_picker_title, Font.TextPos.h1);

        findViewById(R.id.txt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onOk(getColor());
            }
        });
        findViewById(R.id.txt_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCanacel();
            }
        });
        // move cursor & target on first draw
        ViewTreeObserver vto = mRootView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                moveCursor();
                if (ColorPickerView.this.supportsAlpha) moveAlphaCursor();
                moveTarget();
                if (ColorPickerView.this.supportsAlpha) updateAlphaView();
                mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public void setOnColorPickerListener(OnColorPickerListener l) {
        listener = l;
    }

    protected void moveCursor() {
        float y = viewHue.getMeasuredHeight() - (getHue() * viewHue.getMeasuredHeight() / 360.f);
        if (y == viewHue.getMeasuredHeight()) y = 0.f;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewCursor.getLayoutParams();
        layoutParams.leftMargin = (int) (viewHue.getLeft() - Math.floor(viewCursor.getMeasuredWidth() / 2) - viewContainer.getPaddingLeft());
        layoutParams.topMargin = (int) (viewHue.getTop() + y - Math.floor(viewCursor.getMeasuredHeight() / 2) - viewContainer.getPaddingTop());
        viewCursor.setLayoutParams(layoutParams);
    }

    protected void moveTarget() {
        float x = getSat() * viewSatVal.getMeasuredWidth();
        float y = (1.f - getVal()) * viewSatVal.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewTarget.getLayoutParams();
        layoutParams.leftMargin = (int) (viewSatVal.getLeft() + x - Math.floor(viewTarget.getMeasuredWidth() / 2) - viewContainer.getPaddingLeft());
        layoutParams.topMargin = (int) (viewSatVal.getTop() + y - Math.floor(viewTarget.getMeasuredHeight() / 2) - viewContainer.getPaddingTop());
        viewTarget.setLayoutParams(layoutParams);
    }

    protected void moveAlphaCursor() {
        final int measuredHeight = this.viewAlphaCheckered.getMeasuredHeight();
        float y = measuredHeight - ((this.getAlpha() * measuredHeight) / 255.f);
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewAlphaCursor.getLayoutParams();
        layoutParams.leftMargin = (int) (this.viewAlphaCheckered.getLeft() - Math.floor(this.viewAlphaCursor.getMeasuredWidth() / 2) - this.viewContainer.getPaddingLeft());
        layoutParams.topMargin = (int) ((this.viewAlphaCheckered.getTop() + y) - Math.floor(this.viewAlphaCursor.getMeasuredHeight() / 2) - this.viewContainer.getPaddingTop());

        this.viewAlphaCursor.setLayoutParams(layoutParams);
    }

    public void onOk(int color) {
        if (listener != null)
            listener.onOk(this, color);
    }

    public void onCanacel() {
        if (listener != null)
            listener.onCancel(this);
    }

    public int getColor() {
        final int argb = Color.HSVToColor(currentColorHsv);
        return alpha << 24 | (argb & 0x00ffffff);
    }

    public void setColor(int color) {

        mColor = color;
        if (!supportsAlpha) { // remove alpha if not supported
            mColor = mColor | 0xff000000;
        }

        Color.colorToHSV(mColor, currentColorHsv);
        alpha = Color.alpha(mColor);
        viewOldColor.setBackgroundColor(mColor);
        viewNewColor.setBackgroundColor(mColor);
    }

    public float getHue() {
        return currentColorHsv[0];
    }

    public void setHue(float hue) {
        currentColorHsv[0] = hue;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public float getSat() {
        return currentColorHsv[1];
    }

    public void setSat(float sat) {
        currentColorHsv[1] = sat;
    }

    public float getVal() {
        return currentColorHsv[2];
    }

    public void setVal(float val) {
        currentColorHsv[2] = val;
    }

    private void updateAlphaView() {
        final GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{
                Color.HSVToColor(currentColorHsv), 0x0
        });
        viewAlphaOverlay.setBackgroundDrawable(gd);
    }

//	public void show() {
//		dialog.show();
//	}

//	public AlertDialog getDialog() {
//		return dialog;
//	}

    public interface OnColorPickerListener {
        void onCancel(ColorPickerView dialog);

        void onOk(ColorPickerView dialog, int color);
    }
}

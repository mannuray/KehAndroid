package com.dubmania.vidcraft.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.dubmania.vidcraft.R;
import com.google.common.collect.Iterables;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by rat on 9/11/2015.
 */
public class RecordMarkerBar<T extends Number> extends ImageView {

    public static final Integer DEFAULT_MINIMUM = 0;
    public static final Integer DEFAULT_MAXIMUM = 100;
    public static final int HEIGHT_IN_DP = 30;
    private static final int INITIAL_PADDING_IN_DP = 0;
    private final int LINE_HEIGHT_IN_DP = 7;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Bitmap thumbImage = BitmapFactory.decodeResource(getResources(), R.drawable.seek_thumb_normal);
    private final Bitmap thumbImageSelected = BitmapFactory.decodeResource(getResources(), R.drawable.seek_thumb_pressed);
    private final float thumbWidth = thumbImage.getWidth();
    private final float thumbHalfWidth = 0.5f * thumbWidth;
    private final float thumbHalfHeight = 0.5f * thumbImage.getHeight();
    private float INITIAL_PADDING;
    private float padding;
    private T absoluteMaxValue, absoluteProgressValue;
    private NumberType numberType;
    private double absoluteMaxValuePrim, absoluteProgressValuePrim;
    private double normalizedMaxValue = 1d;
    private double normalizedProgressValue = 0d;
    private ArrayList<Double> mMarkers;
    private int selectedMarker = 0;
    private OnRecordMarkerBarTouchListener<T> listener;

    /**
     * Default color of a {@link RecordMarkerBar}, #FF33B5E5. This is also known as "Ice Cream Sandwich" blue.
     */
    public static final int DEFAULT_COLOR = Color.argb(0xFF, 0x33, 0xB5, 0xE5);
    /**
     * An invalid pointer id.
     */
    public static final int INVALID_POINTER_ID = 255;

    // Localized constants from MotionEvent for compatibility
    // with API < 8 "Froyo".
    public static final int ACTION_POINTER_UP = 0x6, ACTION_POINTER_INDEX_MASK = 0x0000ff00, ACTION_POINTER_INDEX_SHIFT = 8;

    //private int mActivePointerId = INVALID_POINTER_ID;

    private int mScaledTouchSlop;

    private int mTextOffset;
    private int mTextSize;
    private int mDistanceToTop;
    private RectF mRect;

    private static final int DEFAULT_TEXT_SIZE_IN_DP = 14;
    private static final int DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP = 8;
    private static final int DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP = 8;
    private boolean mSingleThumb;

    public RecordMarkerBar(Context context) {
        super(context);
        init(context, null);
    }

    public RecordMarkerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecordMarkerBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private T extractNumericValueFromAttributes(TypedArray a, int attribute, int defaultValue) {
        TypedValue tv = a.peekValue(attribute);
        if (tv == null) {
            return (T) Integer.valueOf(defaultValue);
        }

        int type = tv.type;
        if (type == TypedValue.TYPE_FLOAT) {
            return (T) Float.valueOf(a.getFloat(attribute, defaultValue));
        } else {
            return (T) Integer.valueOf(a.getInteger(attribute, defaultValue));
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            setRangeToDefaultValues();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0);
            setRangeValues(
                    extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMaxValue, DEFAULT_MAXIMUM));
            mSingleThumb = a.getBoolean(R.styleable.RangeSeekBar_singleThumb, false);
            a.recycle();
        }
        mMarkers = new ArrayList<>();
        mMarkers.add(0d);

        setValuePrimAndNumberType();

        INITIAL_PADDING = PixelUtil.dpToPx(context, INITIAL_PADDING_IN_DP);

        mTextSize = PixelUtil.dpToPx(context, DEFAULT_TEXT_SIZE_IN_DP);
        mDistanceToTop = PixelUtil.dpToPx(context, DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP);
        mTextOffset = this.mTextSize + PixelUtil.dpToPx(context,
                DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP) + this.mDistanceToTop;

        float lineHeight = PixelUtil.dpToPx(context, LINE_HEIGHT_IN_DP);
        mRect = new RectF(padding,
                mTextOffset + thumbHalfHeight - lineHeight / 2,
                getWidth() - padding,
                mTextOffset + thumbHalfHeight + lineHeight / 2);

        // make RangeSeekBar focusable. This solves focus handling issues in case EditText widgets are being used along with the RangeSeekBar within ScollViews.
        setFocusable(true);
        setFocusableInTouchMode(true);
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setRangeValues(T maxValue) {
        this.absoluteMaxValue = maxValue;
        this.absoluteProgressValue = (T) DEFAULT_MINIMUM;
        setValuePrimAndNumberType();
    }

    @SuppressWarnings("unchecked")
    // only used to set default values when initialised from XML without any values specified
    private void setRangeToDefaultValues() {
        this.absoluteMaxValue = (T) DEFAULT_MAXIMUM;
        this.absoluteProgressValue = (T) DEFAULT_MINIMUM;
        setValuePrimAndNumberType();
    }

    private void setValuePrimAndNumberType() {
        absoluteMaxValuePrim = absoluteMaxValue.doubleValue();
        absoluteProgressValuePrim = absoluteProgressValue.doubleValue();
        numberType = NumberType.fromNumber(absoluteMaxValue);
    }

    public void resetSelectedValues() {
        setSelectedMaxValue(absoluteMaxValue);
        setCurrentProgressValue((T) DEFAULT_MINIMUM);
    }

    /**
     * Returns the absolute maximum value of the range that has been set at construction time.
     *
     * @return The absolute maximum value of the range.
     */
    public T getAbsoluteMaxValue() {
        return absoluteMaxValue;
    }

    /**
     * Returns the absolute progress value of the range that has been set at construction time.
     *
     * @return The absolute progress value of the range.
     */
    public T getAbsoluteProgressValue() {
        return absoluteProgressValue;
    }

    public void addMarker(T marker) {
        mMarkers.add(valueToNormalized(marker));
        selectedMarker = mMarkers.size() - 1;
        invalidate();
    }

    public int getNumberMarkers() {
        return mMarkers.size();
    }

    public ArrayList getMarkers() {
        return mMarkers;
    }

    public void removeMarkersFrom(int mark) {
        for (int i = mMarkers.size() - 1; i > mark; i--) {
            mMarkers.remove(i);
        }
    }

    public int getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(int marker) {
        selectedMarker = Math.max(0, Math.min(marker, mMarkers.size()));
        invalidate();
    }

    /**
     * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
     */

    public void setSelectedMaxValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0 == (absoluteMaxValuePrim)) {
            setNormalizedMaxValue(1d);
        } else {
            setNormalizedMaxValue(valueToNormalized(value));
        }
    }


    /**
     * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
     */
    public void setCurrentProgressValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0 == (absoluteMaxValuePrim)) {
            setNormalizedProgressValue(1d);
        } else {
            setNormalizedProgressValue(valueToNormalized(value));
        }
        invalidate();
    }

    /**
     * Returns the currently selected max value.
     *
     * @return The currently selected max value.
     */
    public T getCurrentProgressValue() {
        return normalizedToValue(normalizedProgressValue);
    }

    /**
     * Registers given listener callback to notify about changed selected values.
     *
     * @param listener The listener to notify about changed selected values.
     */
    public void setOnRecordBarTouchListener(OnRecordMarkerBarTouchListener<T> listener) {
        this.listener = listener;
    }

    /**
     * Handles thumb selection and movement. Notifies listener callback on certain events.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnabled()) {
            return false;
        }

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    //final int pointerIndex = event.findPointerIndex(mActivePointerId);
                    final float x = event.getX(); //event.getX(pointerIndex);
                    double mSelectedPos = screenToNormalized(x);
                    int mRecordPos = -1;
                    Double mLastRecordPos = Iterables.getLast(mMarkers);
                    for(double marker: mMarkers) {
                        //Log.i("Click", "marker pos is " + marker);
                        if(mSelectedPos >= marker - 0.05 && mSelectedPos <= marker + 0.05) {
                            mRecordPos = mMarkers.indexOf(marker);
                        }
                    }
                    listener.onOnRecordMarkerBarTouch(mSelectedPos, mRecordPos, mLastRecordPos);
                }
                invalidate();
                break;
        }
        return true;
    }

    /**
     * Ensures correct size of the widget.
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 200;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        int height = thumbImage.getHeight() + PixelUtil.dpToPx(getContext(), HEIGHT_IN_DP);
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    /**
     * Draws the widget on the given canvas.
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setTextSize(mTextSize);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);

        padding = INITIAL_PADDING + thumbHalfWidth; //INITIAL_PADDING + minMaxLabelSize + thumbHalfWidth;

        // draw seek bar background line
        mRect.left = padding;
        mRect.right = getWidth() - padding;
        canvas.drawRoundRect(mRect, 7, 7, paint);

        int colorToUseForButtonsAndHighlightedLine = DEFAULT_COLOR; //non default, filter is active

        // draw seek bar active range line
        mRect.left = normalizedToScreen(0);
        mRect.right = normalizedToScreen(normalizedMaxValue);

        paint.setColor(colorToUseForButtonsAndHighlightedLine);
        canvas.drawRoundRect(mRect, 7, 7, paint);

        // draw seek bar progress range line
        mRect.left = normalizedToScreen(0);
        mRect.right = normalizedToScreen(normalizedProgressValue);

        Log.i("Drawing ", "drawing progress bar");
        paint.setColor(Color.RED); // change it make it configurable
        canvas.drawRoundRect(mRect, 7, 7, paint);

        //Log.i("marker", " siez selec " + mMarkers.size() + " " + selectedMarker);
        for(double marker: mMarkers) {
            if(marker ==  mMarkers.get(selectedMarker))
                canvas.drawBitmap(thumbImageSelected, normalizedToScreen(marker) - thumbHalfWidth,
                        mTextOffset,
                        paint);
            else
                canvas.drawBitmap(thumbImage, normalizedToScreen(marker) - thumbHalfWidth,
                        mTextOffset,
                        paint);
        }
    }

    /**
     * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method. Other members of this class than the normalized min and max values don't need to be saved.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putDouble("MAX", normalizedMaxValue);
        return bundle;
    }

    /**
     * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable parcel) {
        final Bundle bundle = (Bundle) parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        normalizedMaxValue = bundle.getDouble("MAX");
    }

    /**
     * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized max value to set.
     */
    private void setNormalizedMaxValue(double value) {
        normalizedMaxValue = Math.max(0d, Math.min(1d, Math.max(value, 0)));
        normalizedProgressValue = Math.min(normalizedMaxValue, normalizedProgressValue);
        invalidate();
    }

    /**
     * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized max value to set.
     */
    private void setNormalizedProgressValue(double value) {
        normalizedProgressValue = Math.max(0d, Math.min(normalizedMaxValue, Math.max(value, 0)));
    }

    /**
     * Converts a normalized value to a Number object in the value space between absolute minimum and maximum.
     *
     * @param normalized
     * @return
     */
    @SuppressWarnings("unchecked")
    private T normalizedToValue(double normalized) {
        double v = 0 + normalized * (absoluteMaxValuePrim - 0);
        // TODO parameterize this rounding to allow variable decimal points
        return (T) numberType.toNumber(Math.round(v * 100) / 100d);
    }

    /**
     * Converts the given Number value to a normalized double.
     *
     * @param value The Number value to normalize.
     * @return The normalized double.
     */
    private double valueToNormalized(T value) {
        if (0 == absoluteMaxValuePrim - 0) {
            // prevent division by zero, simply return 0.
            return 0d;
        }
        return (value.doubleValue() - 0) / (absoluteMaxValuePrim - 0);
    }

    /**
     * Converts a normalized value into screen space.
     *
     * @param normalizedCoord The normalized value to convert.
     * @return The converted value in screen space.
     */
    private float normalizedToScreen(double normalizedCoord) {
        return (float) (padding + normalizedCoord * (getWidth() - 2 * padding));
    }

    /**
     * Converts screen space x-coordinates into normalized values.
     *
     * @param screenCoord The x-coordinate in screen space to convert.
     * @return The normalized value.
     */
    private double screenToNormalized(float screenCoord) {
        int width = getWidth();
        if (width <= 2 * padding) {
            // prevent division by zero, simply return 0.
            return 0d;
        } else {
            double result = (screenCoord - padding) / (width - 2 * padding);
            return Math.min(1d, Math.max(0d, result));
        }
    }

    /**
     * Callback listener interface to notify about changed range values.
     *
     * @param <T> The Number type the RangeSeekBar has been declared with.
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    public interface OnRecordMarkerBarTouchListener<T> {

        void onOnRecordMarkerBarTouch(double touchPosition, int mRecordPos, double lastRecordPos);
    }

    /**
     * Utility enumeration used to convert between Numbers and doubles.
     *
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    private enum NumberType {
        LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

        public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {
            if (value instanceof Long) {
                return LONG;
            }
            if (value instanceof Double) {
                return DOUBLE;
            }
            if (value instanceof Integer) {
                return INTEGER;
            }
            if (value instanceof Float) {
                return FLOAT;
            }
            if (value instanceof Short) {
                return SHORT;
            }
            if (value instanceof Byte) {
                return BYTE;
            }
            if (value instanceof BigDecimal) {
                return BIG_DECIMAL;
            }
            throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
        }

        public Number toNumber(double value) {
            switch (this) {
                case LONG:
                    return (long) value;
                case DOUBLE:
                    return value;
                case INTEGER:
                    return (int) value;
                case FLOAT:
                    return (float) value;
                case SHORT:
                    return (short) value;
                case BYTE:
                    return (byte) value;
                case BIG_DECIMAL:
                    return BigDecimal.valueOf(value);
            }
            throw new InstantiationError("can't convert " + this + " to a Number object");
        }
    }

}


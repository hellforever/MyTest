package com.example.zhuzijian.mytest;

/**
 * Created by zhuzijian on 15/7/12.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class XRTextView extends TextView {
    private static final String TAG = "ApolloHome";
    private CharSequence text;
    private float textSize;
    private float paddingLeft;
    private float paddingRight;
    private float marginLeft;
    private float marginRight;
    private ColorStateList textColor;
    private JSONArray colorIndex;
    private Paint paint1 = new Paint();
    private Paint paintColor = new Paint();
    private float textShowWidth;
    private float Spacing = 0;
    private float LineSpacing = 1.3f;//行与行的间距

    public XRTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        text = getText();
        textSize = getTextSize();
        textColor = getTextColors();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        try {
            marginLeft = ((LinearLayout.LayoutParams) getLayoutParams()).leftMargin;
            marginRight = ((LinearLayout.LayoutParams) getLayoutParams()).rightMargin;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        paint1.setTextSize(textSize);
        paint1.setColor(textColor.getDefaultColor());
        paint1.setAntiAlias(true);
        paintColor.setAntiAlias(true);
        paintColor.setTextSize(textSize);
        paintColor.setColor(Color.BLUE);
    }


    public JSONArray getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(JSONArray colorIndex) {
        this.colorIndex = colorIndex;
    }

    /**
     * 传入一个索引，判断当前字是否被高亮
     *
     * @param index
     * @return
     * @throws JSONException
     */
    public boolean isColor(int index) throws JSONException {
        if (colorIndex == null) {
            return false;
        }
        for (int i = 0; i < colorIndex.length(); i++) {
            JSONArray array = colorIndex.getJSONArray(i);
            int start = array.getInt(0);
            int end = array.getInt(1) - 1;
            if (index >= start && index <= end) {
                return true;
            }

        }


        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
//  super.onDraw(canvas);
        View view = (View) this.getParent();
        textShowWidth = view.getMeasuredWidth() - paddingLeft - paddingRight - marginLeft - marginRight;
        int lineCount = 0;

        text = this.getText().toString();//.replaceAll("\n", "\r\n");
        if (text == null) return;
        String[] textCharArray = text.toString().split(" ");
        // 已绘的宽度
        float drawedWidth = 0;
        float charWidth;
        for (int i = 0; i < textCharArray.length; i++) {
            charWidth = paint1.measureText(textCharArray[i]);

            if (textShowWidth - drawedWidth < charWidth) {
                lineCount++;
                drawedWidth = 0;
            }
            boolean color = false;
            try {
                color = isColor(i);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (color) {

                canvas.drawText(textCharArray[i], paddingLeft + drawedWidth,
                        (lineCount + 1) * textSize * LineSpacing, paintColor);
            } else {

                canvas.drawText(textCharArray[i], paddingLeft + drawedWidth,
                        (lineCount + 1) * textSize * LineSpacing, paint1);
            }
            drawedWidth += charWidth;

            if (lineCount == 1 && i != textCharArray.length - 1 && paint1.measureText(textCharArray[i + 1]) > textShowWidth - drawedWidth) {
                canvas.drawText("...", paddingLeft + drawedWidth,
                        (lineCount + 1) * textSize * LineSpacing, paint1);
                break;
            } else if ((i < textCharArray.length - 1) && !(paint1.measureText(textCharArray[i + 1]) > textShowWidth - drawedWidth)) {
                drawedWidth += 10;
            }
        }
        setHeight((int) ((lineCount + 1) * (int) textSize * LineSpacing + 10));
    }

    public float getSpacing() {
        return Spacing;
    }

    public void setSpacing(float spacing) {
        Spacing = spacing;
    }

    public float getMYLineSpacing() {
        return LineSpacing;
    }

    public void setMYLineSpacing(float lineSpacing) {
        LineSpacing = lineSpacing;
    }

    public float getMYTextSize() {
        return textSize;
    }

    public void setMYTextSize(float textSize) {
        this.textSize = textSize;
        paint1.setTextSize(textSize);
        paintColor.setTextSize(textSize);
    }
}
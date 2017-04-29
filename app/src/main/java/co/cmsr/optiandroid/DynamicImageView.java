package co.cmsr.optiandroid;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jonbuckley on 4/28/17.
 */

/// <summary>
/// DynamicImageView is a helper extension that overrides OnMeasure in order to scale the said image
/// to fit the entire width/or height of the parent container.
/// </summary>
public class DynamicImageView extends AppCompatImageView
{
    float aspectRatio;
    public DynamicImageView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        aspectRatio = (float) getDrawable().getIntrinsicWidth() / (float) getDrawable().getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        float spaceAspectRatio = (float) widthMeasureSpec / (float) heightMeasureSpec;

        int height, width;
        if (spaceAspectRatio < aspectRatio) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = height * getDrawable().getIntrinsicWidth() / getDrawable().getIntrinsicHeight();
        }
        setMeasuredDimension(width, height);
    }
}
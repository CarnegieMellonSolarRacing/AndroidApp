package co.cmsr.optiandroid.displays;

import android.animation.ArgbEvaluator;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.cmsr.optiandroid.R;
import co.cmsr.optiandroid.charts.DynamicBarChart;

/**
 * Created by jonbuckley on 4/30/17.
 */

public class TemperatureDisplay extends SimpleBarDisplay {
    public static final float BAR_CHART_WIDTH_FRACTION = .048f;
    public static final float BAR_CHART_HEIGHT_FRACTION = 0.135f;
    public static final int TEMP_MAX_COLOR = 0xe06666;
    public static final int TEMP_MIN_COLOR = 0xe06666;
    public static final String SUFFIX = " C";
    public static final String FORMAT_STRING = "00";

    public TemperatureDisplay(
            ImageView bar,
            TextView tempDisplay,
            String name,
            int graphicWidth,
            int graphicHeight,
            float tempMin,
            float tempMax) {

        super(bar,
            tempDisplay,
            name,
            graphicWidth,
            graphicHeight,
            tempMin,
            tempMax,
            TEMP_MIN_COLOR,
            TEMP_MAX_COLOR,
            BAR_CHART_WIDTH_FRACTION,
            BAR_CHART_HEIGHT_FRACTION,
            FORMAT_STRING,
            SUFFIX);
    }
}

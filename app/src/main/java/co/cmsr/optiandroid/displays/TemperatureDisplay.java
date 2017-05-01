package co.cmsr.optiandroid.displays;

import android.animation.ArgbEvaluator;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
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

public class TemperatureDisplay extends BarChartDisplay {
    public static final float BAR_CHART_WIDTH_FRACTION = 0.10f;
    public static final float BAR_CHART_HEIGHT_FRACTION = 0.142f;
    public static final int TEMP_MAX_COLOR = 0x06b703;
    public static final int TEMP_MIN_COLOR = 0xff9030;
    public static final String SUFFIX = " C";

    public TemperatureDisplay(
            BarChart barChart,
            TextView tempDisplay,
            String name,
            int graphicWidth,
            int graphicHeight,
            float tempMin,
            float tempMax) {

        super(barChart,
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
            SUFFIX);
    }
}

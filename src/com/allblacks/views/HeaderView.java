package com.allblacks.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.allblacks.R;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundForecastDateDTO;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundForecastDayDTO;

public class HeaderView extends LinearLayout implements AbsListView.OnScrollListener {

    private ImageView forecastIcon;
    private TextView day;
    private TextView tempAvg;
    private TextView rainAvg;
    private TextView temp;

    public HeaderView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        forecastIcon = (ImageView) findViewById(R.id.forecast_icon);
        day = (TextView) findViewById(R.id.forecast_day);
        tempAvg = (TextView) findViewById(R.id.forecast_tempavg);
        rainAvg = (TextView) findViewById(R.id.forecast_rainavg);
        temp = (TextView) findViewById(R.id.forecast_temp);
    }

    public void setDataSet(Object dataSet) {
        WeatherUndergroundForecastDayDTO weatherUndergroundForecastDayDTO = (WeatherUndergroundForecastDayDTO) dataSet;
        WeatherUndergroundForecastDateDTO forecastDate = weatherUndergroundForecastDayDTO.getForecastDate();
        day.setText(forecastDate.getWeekday() + " " + forecastDate.getMonthName() + " " + forecastDate.getDay());
        tempAvg.setText(weatherUndergroundForecastDayDTO.getLow().getCelsius() + "° / " + weatherUndergroundForecastDayDTO.getHigh().getCelsius() + "°");
        rainAvg.setText(weatherUndergroundForecastDayDTO.getRainDay().getMilimeters() + " / " + weatherUndergroundForecastDayDTO.getRainNight().getMilimeters());
        temp.setText(Double.valueOf(weatherUndergroundForecastDayDTO.getLow().getCelsius()) + Double.valueOf(weatherUndergroundForecastDayDTO.getHigh().getCelsius()) + "°");

        final float scale = getResources().getDisplayMetrics().density;
        forecastIcon.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (196 * scale + 0.5f)));
    }

    @Override
    public void onScroll(AbsListView abslistview, int i, int j, int k) {
        View view = abslistview.getChildAt(0);
        if (view == this) {
            int l = -view.getTop() / 2;
            mViewFlipper.setTranslationY(l);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView abslistview, int i) {
    }

    private boolean mIsAddedToLibraryObserverRegistered;
    private ViewFlipper mViewFlipper;
}

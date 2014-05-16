package com.allblacks.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allblacks.R;
import com.allblacks.utils.pinnedlist.CompositeAdapter;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundForecastDateDTO;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundForecastDayDTO;

import java.util.Collection;

/**
 * Created by Marc on 11/05/2014.
 */
public class WeatherHeaderAdapter extends ContextWrapper {

    public WeatherHeaderAdapter(Context base) {
        super(base);
    }

    protected View newHeaderView(Context context, int partition, Collection<?> elements, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.forecast_future, parent, false);
        return view;
    }

    protected void bindHeaderView(View view, int partition, Collection<?> elements) {

        ForecastListItem forecastListItem;
        if (view.getTag() == null) {
            forecastListItem = new ForecastListItem();
            forecastListItem.forecastIcon = (ImageView) view.findViewById(R.id.forecast_icon);
            forecastListItem.day = (TextView) view.findViewById(R.id.forecast_day);
            forecastListItem.tempAvg = (TextView) view.findViewById(R.id.forecast_tempavg);
            forecastListItem.rainAvg = (TextView) view.findViewById(R.id.forecast_rainavg);
            forecastListItem.temp = (TextView) view.findViewById(R.id.forecast_temp);
        }
        else {
            forecastListItem = (ForecastListItem) view.getTag();
        }

        WeatherUndergroundForecastDayDTO weatherUndergroundForecastDayDTO = null;//(WeatherUndergroundForecastDayDTO) getPartition(partition).getHeader();
        WeatherUndergroundForecastDateDTO forecastDate = weatherUndergroundForecastDayDTO.getForecastDate();
        forecastListItem.day.setText(forecastDate.getWeekday() + " " + forecastDate.getMonthName() + " " + forecastDate.getDay());
        forecastListItem.tempAvg.setText(weatherUndergroundForecastDayDTO.getLow().getCelsius() + "° / " + weatherUndergroundForecastDayDTO.getHigh().getCelsius() + "°");
        forecastListItem.rainAvg.setText(weatherUndergroundForecastDayDTO.getRainDay().getMilimeters() + " / " + weatherUndergroundForecastDayDTO.getRainNight().getMilimeters());
        forecastListItem.temp.setText(Double.valueOf(weatherUndergroundForecastDayDTO.getLow().getCelsius()) + Double.valueOf(weatherUndergroundForecastDayDTO.getHigh().getCelsius()) + "°");

        final float scale = getBaseContext().getResources().getDisplayMetrics().density;
        forecastListItem.forecastIcon.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (196 * scale + 0.5f)));

        view.setId(partition);
        view.setTag(forecastListItem);
    }

    class ForecastListItem {

        ImageView forecastIcon;
        TextView day;
        TextView tempAvg;
        TextView rainAvg;
        TextView temp;
    }
}

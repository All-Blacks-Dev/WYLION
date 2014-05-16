package com.allblacks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allblacks.R;
import com.allblacks.utils.pinnedlist.CompositeAdapter;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundForecastDateDTO;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundForecastDayDTO;

import java.util.Collection;
import java.util.List;

/**
 * Created by Marc on 4/05/2014.
 */
public class WeatherListAdapter extends CompositeAdapter {

    public WeatherListAdapter(Context context) {
        super(context);
    }

    public void setDataSet(List<WeatherUndergroundForecastDayDTO> dataSet) {
        Partition[] partitions = new Partition[1];
        partitions[0] = new Partition(false, null);
        partitions[0].setElements(dataSet.subList(1, dataSet.size()));
        setPartitions(partitions);
    }

    @Override
    protected View newHeaderView(Context context, int partition, Collection<?> elements, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.forecast_future, parent, false);
        return view;
    }

    @Override
    protected void bindHeaderView(View view, int partition, Collection<?> elements) {
    }

    @Override
    protected View newView(Context context, int partition, Object element, int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.forecast_future, parent, false);
        return view;
    }

    @Override
    protected void bindView(View v, int partition, Object element, int position) {

        ForecastListItem forecastListItem;
        if (v.getTag() == null) {
            forecastListItem = new ForecastListItem();
            forecastListItem.forecastIcon = (ImageView) v.findViewById(R.id.forecast_icon);
            forecastListItem.day = (TextView) v.findViewById(R.id.forecast_day);
            forecastListItem.tempAvg = (TextView) v.findViewById(R.id.forecast_tempavg);
            forecastListItem.rainAvg = (TextView) v.findViewById(R.id.forecast_rainavg);
            forecastListItem.temp = (TextView) v.findViewById(R.id.forecast_temp);
        }
        else {
            forecastListItem = (ForecastListItem) v.getTag();
        }

        WeatherUndergroundForecastDayDTO weatherUndergroundForecastDayDTO = (WeatherUndergroundForecastDayDTO) element;
        WeatherUndergroundForecastDateDTO forecastDate = weatherUndergroundForecastDayDTO.getForecastDate();
        forecastListItem.day.setText(forecastDate.getWeekday() + " " + forecastDate.getMonthName() + " " + forecastDate.getDay());
        forecastListItem.tempAvg.setText(weatherUndergroundForecastDayDTO.getLow().getCelsius() + "° / " + weatherUndergroundForecastDayDTO.getHigh().getCelsius() + "°");
        forecastListItem.rainAvg.setText(weatherUndergroundForecastDayDTO.getRainDay().getMilimeters() + " / " + weatherUndergroundForecastDayDTO.getRainNight().getMilimeters());
        forecastListItem.temp.setText(Double.valueOf(weatherUndergroundForecastDayDTO.getLow().getCelsius()) + Double.valueOf(weatherUndergroundForecastDayDTO.getHigh().getCelsius()) + "°");

        v.setId(1 + position);
        v.setTag(forecastListItem);
    }

    class ForecastListItem {

        ImageView forecastIcon;
        TextView day;
        TextView tempAvg;
        TextView rainAvg;
        TextView temp;
    }
}

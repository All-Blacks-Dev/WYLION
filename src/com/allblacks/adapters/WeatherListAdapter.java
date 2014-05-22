package com.allblacks.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allblacks.R;
import com.allblacks.utils.image.ImageWorker;
import com.allblacks.utils.pinnedlist.PinnedHeaderListAdapter;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundForecastDateDTO;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundForecastDayDTO;

import java.util.Collection;
import java.util.List;

/**
 * Created by Marc on 4/05/2014.
 */
public class WeatherListAdapter extends PinnedHeaderListAdapter {

    public WeatherListAdapter(Context context) {
        super(context);
    }

    public void setDataSet(List<WeatherUndergroundForecastDayDTO> dataSet) {
        setPinnedPartitionHeadersEnabled(true);

        Partition[] partitions = new Partition[1];
        partitions[0] = new Partition(false, null);
        partitions[0].setElements(dataSet.subList(1, dataSet.size()));

        setPartitions(partitions);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    protected View newHeaderView(Context context, int partition, Collection<?> elements, ViewGroup parent) {
        return null;
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
    protected void bindView(View view, int partition, Object element, int position) {
        bindView(view, element);
    }

    private void bindView(View view, Object element) {
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

        WeatherUndergroundForecastDayDTO weatherUndergroundForecastDayDTO = (WeatherUndergroundForecastDayDTO) element;
        WeatherUndergroundForecastDateDTO forecastDate = weatherUndergroundForecastDayDTO.getForecastDate();
        forecastListItem.day.setText(forecastDate.getWeekday() + " " + forecastDate.getMonthName() + " " + forecastDate.getDay());
        forecastListItem.tempAvg.setText(weatherUndergroundForecastDayDTO.getLow().getCelsius() + "° / " + weatherUndergroundForecastDayDTO.getHigh().getCelsius() + "°");
        forecastListItem.rainAvg.setText(weatherUndergroundForecastDayDTO.getRainDay().getMilimeters() + " / " + weatherUndergroundForecastDayDTO.getRainNight().getMilimeters());
        forecastListItem.temp.setText(Double.valueOf(weatherUndergroundForecastDayDTO.getLow().getCelsius()) + Double.valueOf(weatherUndergroundForecastDayDTO.getHigh().getCelsius()) / 2 + "°");

        //view.setBackgroundColor(getBackgroundByTemperature((Double.valueOf(weatherUndergroundForecastDayDTO.getLow().getCelsius()) + Double.valueOf(weatherUndergroundForecastDayDTO.getHigh().getCelsius())) / 2));
        view.setBackground(getBackgroundByTemperature((Double.valueOf(weatherUndergroundForecastDayDTO.getLow().getCelsius()) + Double.valueOf(weatherUndergroundForecastDayDTO.getHigh().getCelsius())) / 2));
        view.setTag(forecastListItem);
    }

    private Drawable getBackgroundByTemperature(double v) {
        if (v <= 15) {
            return new BitmapDrawable(BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ab_solid_light_holo));
        }
        if (v <= 25) {
            return new BitmapDrawable(BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ab_solid_orange_holo));
        }
        return new BitmapDrawable(BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ab_solid_dark_holo));
    }

    private int getBackgroundColorByTemperature(double v) {

        if (v <= 15) {
            return Color.parseColor("#aa3689c0");
        }
        if (v <= 25) {
            return Color.parseColor("#aaff7227");
        }
        return Color.parseColor("#aaff2828");
    }

    class ForecastListItem {

        ImageView forecastIcon;
        TextView day;
        TextView tempAvg;
        TextView rainAvg;
        TextView temp;
    }
}

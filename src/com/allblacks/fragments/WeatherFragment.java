package com.allblacks.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.allblacks.R;
import com.allblacks.adapters.WeatherListAdapter;
import com.allblacks.adapters.WeatherUpdater;
import com.allblacks.weather.controller.WeatherUndergroundController;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundWeatherDTO;
import com.allblacks.weather.model.Locality;

/**
 * Created by Marc on 3/05/2014.
 */
public class WeatherFragment extends Fragment  {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private WeatherListAdapter adapter;
    private WeatherUpdater weatherUpdater;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WeatherFragment newInstance(int sectionNumber) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WeatherUndergroundController.FORECAST_LONG.hashCode()) {
                if (msg.arg1 == WeatherUndergroundController.OK) {
                    adapter.setDataSet(((WeatherUndergroundWeatherDTO)msg.obj).getForecast().getSimpleForecast().getForecastDay());
                }
            }
        }
    };

    public WeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.simpleList);

        adapter = new WeatherListAdapter(this.getActivity());
        listView.setAdapter(adapter);

        Locality locality = new Locality();
        locality.setCountry("Belgium");
        locality.setTown("Erezee");

        weatherUpdater = new WeatherUpdater(handler, 5000, locality);
        weatherUpdater.run();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
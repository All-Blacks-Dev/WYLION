package com.allblacks.weather.controller;

import android.os.Handler;
import android.os.Message;

import com.allblacks.utils.json.impl.JSONMarshaller;
import com.allblacks.utils.web.HttpUtil;
import com.allblacks.weather.model.Locality;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundWeatherDTO;

/**
 * Created by Marc on 10/05/2014.
 */
public class WeatherUndergroundController {

    private static final String WUURL = "http://api.wunderground.com/api/[key]/geolookup/conditions/[query]/q/[country]/[town].json";
    public static final String FORECAST = "forecast";
    public static final String FORECAST_LONG = "forecast10day";

    public static final int OK = 400;
    public static final int ERROR = 500;

    protected static final String PH_QUERY = "[query]";
    protected static final String PH_KEY = "[key]";
    protected static final String PH_COUTRY = "[country]";
    protected static final String PH_TOWN = "[town]";

    public static void getForecast(final Handler handler, Locality locality) {

        String url = WUURL
                .replace(PH_QUERY, FORECAST)
                .replace(PH_KEY, "b18c1b3111bcf246")
                .replace(PH_COUTRY, locality.getCountry())
                .replace(PH_TOWN, locality.getTown());

        Message message = new Message();
        message.setTarget(handler);
        message.what = FORECAST.hashCode();

        try {

            String result = HttpUtil.getInstance().getDataAsString(url);
            JSONMarshaller jsonMarshaller = new JSONMarshaller();
            WeatherUndergroundWeatherDTO weatherUndergroundWeatherDTO = (WeatherUndergroundWeatherDTO) jsonMarshaller.unMarshall(result, WeatherUndergroundWeatherDTO.class);

            if(weatherUndergroundWeatherDTO != null) {
                message.obj = weatherUndergroundWeatherDTO;
                message.arg1 = OK;
            }
        }
        catch(Exception e) {
            message.obj = e.getLocalizedMessage();
            message.arg1 = ERROR;
        }
        finally {
            message.sendToTarget();
        }
    }

    public static void getForecastLong(final Handler handler, Locality locality) {

        String url = WUURL
                .replace(PH_QUERY, FORECAST_LONG)
                .replace(PH_KEY, "b18c1b3111bcf246")
                .replace(PH_COUTRY, locality.getCountry())
                .replace(PH_TOWN, locality.getTown());

        Message message = new Message();
        message.setTarget(handler);
        message.what = FORECAST_LONG.hashCode();

        try {

            String result = HttpUtil.getInstance().getDataAsString(url);
            JSONMarshaller jsonMarshaller = new JSONMarshaller();
            WeatherUndergroundWeatherDTO weatherUndergroundWeatherDTO = (WeatherUndergroundWeatherDTO) jsonMarshaller.unMarshall(result, WeatherUndergroundWeatherDTO.class);

            if(weatherUndergroundWeatherDTO != null) {
                message.obj = weatherUndergroundWeatherDTO;
                message.arg1 = OK;
            }
        }
        catch(Exception e) {
            message.obj = e.getLocalizedMessage();
            message.arg1 = ERROR;
        }
        finally {
            message.sendToTarget();
        }
    }
}

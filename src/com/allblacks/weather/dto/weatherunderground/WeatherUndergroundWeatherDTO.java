package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;
import com.allblacks.utils.json.impl.JSONType;
import com.allblacks.weather.dto.generic.WeatherDTO;

/**
 * Created by Marc on 26/04/2014.
 */
public class WeatherUndergroundWeatherDTO extends WeatherDTO {

    private WeatherUndergroundForecastDTO forecast;

    public WeatherUndergroundForecastDTO getForecast() {
        return forecast;
    }

    @JSONSetter(name = "forecast", type = JSONType.JSON_OBJECT)
    public void setForecast(WeatherUndergroundForecastDTO forecast) {
        this.forecast = forecast;
    }
}

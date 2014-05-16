package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;
import com.allblacks.utils.json.impl.JSONType;

/**
 * Created by Marc on 26/04/2014.
 */
public class WeatherUndergroundForecastDTO {

    private WeatherUndergroundSimpleForecastDTO simpleForecast;

    public WeatherUndergroundSimpleForecastDTO getSimpleForecast() {
        return simpleForecast;
    }

    @JSONSetter(name = "simpleforecast", type = JSONType.JSON_OBJECT)
    public void setSimpleForecast(WeatherUndergroundSimpleForecastDTO simpleForecast) {
        this.simpleForecast = simpleForecast;
    }
}

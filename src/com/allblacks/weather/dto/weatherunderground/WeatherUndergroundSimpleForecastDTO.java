package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;
import com.allblacks.utils.json.impl.JSONType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 26/04/2014.
 */
public class WeatherUndergroundSimpleForecastDTO {

    List<WeatherUndergroundForecastDayDTO> forecastDay;

    public List<WeatherUndergroundForecastDayDTO> getForecastDay() {
        return forecastDay;
    }

    @JSONSetter(name = "forecastday", type = JSONType.LIST, objectClazz = WeatherUndergroundForecastDayDTO.class, listClazz = ArrayList.class)
    public void setForecastDay(List<WeatherUndergroundForecastDayDTO> forecastDay) {
        this.forecastDay = forecastDay;
    }
}

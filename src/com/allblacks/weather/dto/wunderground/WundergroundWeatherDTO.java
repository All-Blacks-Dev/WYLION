package com.allblacks.weather.dto.wunderground;

import com.allblacks.utils.json.JSONSetter;
import com.allblacks.utils.json.impl.JSONType;
import com.allblacks.weather.dto.generic.WeatherDTO;

/**
 * Created by Marc on 26/04/2014.
 */
public class WundergroundWeatherDTO extends WeatherDTO {

    private WundergroundForecastDTO wundergroundForecastDTO;

    public WundergroundForecastDTO getWundergroundForecastDTO() {
        return wundergroundForecastDTO;
    }

    @JSONSetter(name = "forecast", type = JSONType.JSON_OBJECT)
    public void setWundergroundForecastDTO(WundergroundForecastDTO wundergroundForecastDTO) {
        this.wundergroundForecastDTO = wundergroundForecastDTO;
    }
}

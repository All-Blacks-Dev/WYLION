package com.allblacks.weather.dto.wunderground;

import com.allblacks.weather.dto.WeatherDTO;

/**
 * Created by Marc on 26/04/2014.
 */
public class WundergroundWeatherDTO extends WeatherDTO {

    private WundergroundForecastDTO wundergroundForecastDTO;

    public WundergroundForecastDTO getWundergroundForecastDTO() {
        return wundergroundForecastDTO;
    }

    public void setWundergroundForecastDTO(WundergroundForecastDTO wundergroundForecastDTO) {
        this.wundergroundForecastDTO = wundergroundForecastDTO;
    }
}

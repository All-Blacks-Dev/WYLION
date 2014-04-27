package com.allblacks.weather.dto.wunderground;

import com.allblacks.utils.json.JSONSetter;
import com.allblacks.utils.json.impl.JSONType;

/**
 * Created by Marc on 26/04/2014.
 */
public class WundergroundForecastDTO {

    private WundergroundSimpleForecastDTO wundergroundSimpleForecastDTO;

    public WundergroundSimpleForecastDTO getWundergroundSimpleForecastDTO() {
        return wundergroundSimpleForecastDTO;
    }

    @JSONSetter(name = "simpleforecast", type = JSONType.JSON_OBJECT)
    public void setWundergroundSimpleForecastDTO(WundergroundSimpleForecastDTO wundergroundSimpleForecastDTO) {
        this.wundergroundSimpleForecastDTO = wundergroundSimpleForecastDTO;
    }
}

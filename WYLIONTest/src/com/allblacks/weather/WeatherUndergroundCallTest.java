package com.allblacks.weather;

import android.test.AndroidTestCase;

import com.allblacks.utils.json.impl.JSONMarshaller;
import com.allblacks.utils.web.HttpUtil;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundWeatherDTO;

import junit.framework.Assert;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Marc on 10/05/2014.
 */
public class WeatherUndergroundCallTest extends AndroidTestCase {

    private static final String WUURL = "http://api.wunderground.com/api/[key]/geolookup/conditions/[query]/q/[country]/[town].json";
    private static final String FORECAST = "forecast";
    private static final String FORECAST_LONG = "forecast10day";

    private static final String PH_QUERY = "[query]";
    private static final String PH_KEY = "[key]";
    private static final String PH_COUTRY = "[country]";
    private static final String PH_TOWN = "[town]";

    public void testHttpCall() throws IOException, IllegalAccessException, ParseException, InstantiationException {
        String url = WUURL
                .replace(PH_QUERY, FORECAST)
                .replace(PH_KEY,"b18c1b3111bcf246")
                .replace(PH_COUTRY,"America")
                .replace(PH_TOWN, "New-York");

        String result = HttpUtil.getInstance().getDataAsString(url);
        Assert.assertNotNull(result);

        JSONMarshaller jsonMarshaller = new JSONMarshaller();
        WeatherUndergroundWeatherDTO weatherUndergroundWeatherDTO = (WeatherUndergroundWeatherDTO) jsonMarshaller.unMarshall(result, WeatherUndergroundWeatherDTO.class);

        assertNotNull(weatherUndergroundWeatherDTO);
    }
}

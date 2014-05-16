package com.allblacks.adapters;

import android.os.Handler;

import com.allblacks.weather.controller.WeatherUndergroundController;
import com.allblacks.weather.model.Locality;

/**
 * Created by Marc on 10/05/2014.
 */
public class WeatherUpdater {

    private Handler handler;
    private Integer timerMillis;
    private Locality locality;

    public WeatherUpdater(Handler handler, Integer timerMillis, Locality locality) {
        this.handler = handler;
        this.timerMillis = timerMillis;
        this.locality = locality;
    }

    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //while(true) {
                    WeatherUndergroundController.getForecastLong(handler, locality);
                    Thread.sleep(timerMillis);
                    //}
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

package com.kieslect.weather.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.weather.service.IWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class WeatherController {

    @Autowired
    private IWeatherService weatherService;

    @GetMapping("/getWeatherInfo")
    public R<?> getWeatherInfo(@RequestParam(value = "id", required = false) int id,
                @RequestParam("latitude") double latitude,
                               @RequestParam("longitude") double longitude,
                               @RequestParam(value = "lang", required = false) String lang,
                               @RequestParam(value = "unit", required = false) String unit) {
        return R.ok(weatherService.getWeatherInfo(id,latitude, longitude,lang,unit));
    }

}

package com.kieslect.weather.service;

public interface IWeatherService {

    Object getWeatherInfoWithCache( double latitude, double longitude);
}

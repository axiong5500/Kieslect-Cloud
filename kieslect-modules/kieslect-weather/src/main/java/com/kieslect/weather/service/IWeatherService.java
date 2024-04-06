package com.kieslect.weather.service;

public interface IWeatherService {

    Object getWeatherInfoWithCache( double latitude, double longitude);

    Object getWeatherInfo(double latitude, double longitude, String lang, String unit);

    Object getCity(String location, String lang, String unit);
}

package com.kieslect.weather.service;

public interface IWeatherService {

    Object getWeatherInfo(int id,double latitude, double longitude, String lang, String unit);

}

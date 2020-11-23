package com.microservice.weather.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservice.weather.model.WeatherForecast;
import com.microservice.weather.util.WeatherUtil;

@RestController
public class WeatherController {

	private static String url = "https://samples.openweathermap.org/data/2.5/forecast?q=%s&appid=d2929e9483efc82c82c32ee7e02d";

	@GetMapping("/weather/{cityAndState}")
	public ResponseEntity<List<WeatherForecast>> getWeatherByCity(@PathVariable("cityAndState") String cityAndState) {
		RestTemplate template = new RestTemplate();
		List<WeatherForecast> list = WeatherUtil
				.getForecastList(template.getForObject(String.format(url, cityAndState), String.class));
		return new ResponseEntity<List<WeatherForecast>>(list, HttpStatus.OK);
	}

}

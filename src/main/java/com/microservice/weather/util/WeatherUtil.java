package com.microservice.weather.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.microservice.weather.model.WeatherForecast;

public final class WeatherUtil {

	public static List<WeatherForecast> getForecastList(String response) {
		List<WeatherForecast> weatherForecastList = new ArrayList<>();
		JSONObject obj = new JSONObject(response);
		JSONArray list = obj.getJSONArray("list");
		String initDate = list.getJSONObject(0).getString("dt_txt").split(" ")[0];
		Double maxTemp = 0.0;
		Double minTemp = Double.MAX_VALUE;
		String sky = "";
		Map<String, Integer> skyMap = new HashMap<>();
		for (int i = 0; i < list.length(); i++) {
			String dt_txt = list.getJSONObject(i).getString("dt_txt").split(" ")[0];
			if (!initDate.equalsIgnoreCase(dt_txt)) {
				WeatherForecast forecast = new WeatherForecast();
				forecast.setDate(initDate);
				forecast.setMaxTemp(getInCelcius(maxTemp));
				forecast.setMinTemp(getInCelcius(minTemp));
				forecast.setSky(getMaxEntryInMapBasedOnValue(skyMap).getKey());
				forecast.setMessage(setMessage(forecast.getSky(), forecast.getMaxTemp()));
				initDate = dt_txt;
				maxTemp = 0.0;
				minTemp = Double.MAX_VALUE;
				skyMap = new HashMap<>();
				weatherForecastList.add(forecast);
				if(weatherForecastList.size() == 3)
					break;
			}
			JSONObject main = list.getJSONObject(i).getJSONObject("main");
			Double temp_max = main.getDouble("temp_max");
			Double temp_min = main.getDouble("temp_min");
			JSONArray weather = list.getJSONObject(i).getJSONArray("weather");
			sky = weather.getJSONObject(0).getString("main");
			if (skyMap.containsKey(sky)) {
				skyMap.put(sky, skyMap.get(sky) + 1);
			} else {
				skyMap.put(sky, 1);
			}

			if (temp_max > maxTemp)
				maxTemp = temp_max;

			if (minTemp > temp_min)
				minTemp = temp_min;

		}
		return weatherForecastList;
	}

	private static String setMessage(String key, String maxTemp) {
		if (key.equalsIgnoreCase("rain"))
			return "Carry Umbrela";
		if (Double.parseDouble(maxTemp) > 40.0)
			return "Use sunscreen lotion";

		return "Clear Day";
	}

	private static String getInCelcius(Double temp) {
		return String.format("%.2f", temp - 273.15F);
	}

	public static Map.Entry<String, Integer> getMaxEntryInMapBasedOnValue(Map<String, Integer> map) {
		Map.Entry<String, Integer> entryWithMaxValue = null;
		for (Map.Entry<String, Integer> currentEntry : map.entrySet()) {
			if (entryWithMaxValue == null || currentEntry.getValue().compareTo(entryWithMaxValue.getValue()) > 0) {
				entryWithMaxValue = currentEntry;
			}
		}
		return entryWithMaxValue;
	}

}

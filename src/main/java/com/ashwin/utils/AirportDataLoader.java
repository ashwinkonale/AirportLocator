package com.ashwin.utils;

import com.ashwin.AppConfig;
import com.ashwin.config.RedisConfig;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AirportDataLoader {
    private static final String IS_AIRPORT_DATA_LOADED = "is_airport_data_loaded";
    private final BufferedReader br;
    private final Jedis jedis;
    private static final String DELIMITER = ",";
    public static final String AIRPORTS = "airports";

    public AirportDataLoader(AppConfig appConfig) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(appConfig.airportDataFilePath));
        RedisConfig redisConfig = appConfig.redisConfig;
        jedis = new Jedis(redisConfig.host, redisConfig.port);
    }

    public void load() throws IOException {
        if (isDataAlreadyLoaded()) return;
        String line;
        String header = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] data = line.split(DELIMITER);
            jedis.geoadd(AIRPORTS, Double.valueOf(data[2]), Double.valueOf(data[1]), data[0]);
        }
        br.close();
        jedis.set(IS_AIRPORT_DATA_LOADED, String.valueOf(true));
    }

    private boolean isDataAlreadyLoaded() {
        System.out.println("Airport data has already been loaded");
        return Boolean.parseBoolean(jedis.get(IS_AIRPORT_DATA_LOADED));
    }
}

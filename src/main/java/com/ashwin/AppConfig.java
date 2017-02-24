package com.ashwin;

import com.ashwin.config.RedisConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AppConfig implements Serializable{
    @JsonProperty("airports.data.path")
    public String airportDataFilePath;

    @JsonProperty("users.data.path")
    public String usersDataFilePath;

    @JsonProperty("output.path")
    public String outputFilePath;

    @JsonProperty("redis")
    public RedisConfig redisConfig;

    public AppConfig() {
    }
}

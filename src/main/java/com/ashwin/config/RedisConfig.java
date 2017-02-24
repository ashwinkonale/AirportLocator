package com.ashwin.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RedisConfig implements Serializable{
    @JsonProperty("host")
    public String host;

    @JsonProperty("port")
    public int port;

    public RedisConfig() {
    }
}

package com.ashwin.flink;

import com.ashwin.AirportLocator;
import com.ashwin.config.RedisConfig;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;

public class AirportLocateMapFunction extends RichMapFunction<Tuple3<String, Double, Double>, Tuple2<String, String>> {
    private final RedisConfig redisConfig;
    private AirportLocator airportLocator;

    public AirportLocateMapFunction(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    public void open(Configuration parameters) throws Exception {
        this.airportLocator = new AirportLocator(this.redisConfig);
    }

    public void close() throws Exception {
        if (this.airportLocator != null)
            this.airportLocator.closePool();
    }

    public Tuple2<String, String> map(Tuple3<String, Double, Double> user) throws Exception {
        return new Tuple2<String, String>(user.f0, airportLocator.locate(user.f1, user.f2));
    }
}

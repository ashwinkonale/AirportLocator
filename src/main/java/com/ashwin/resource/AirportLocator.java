package com.ashwin.resource;

import com.ashwin.config.RedisConfig;
import com.ashwin.redis.RedisFactory;
import com.ashwin.utils.AirportDataLoader;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.io.Serializable;
import java.util.List;

public class AirportLocator implements Serializable{
    private final JedisPool jedisPool;

    public AirportLocator(RedisConfig redisConfig) {
        this.jedisPool = new RedisFactory(redisConfig).getJedisPool();
    }

    public String locate(Double lat, Double lon) {
        int tries = 5;
        int multiplicationFactor = 2;
        int radiusInKm = 20;
        int try_count = 1;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            while (try_count <= tries) {
                final String located = locate(lat, lon, radiusInKm, jedis);
                if (located == null)
                    radiusInKm = radiusInKm * multiplicationFactor;
                else
                    return located;
            }
        } finally {
            if(jedis != null) jedis.close();
        }
        return null;
    }

    private String locate(Double lat, Double lon, int radius, Jedis jedis) {
        final List<GeoRadiusResponse> geoRadius = jedis.georadius(AirportDataLoader.AIRPORTS, lon, lat, radius, GeoUnit.KM, GeoRadiusParam.geoRadiusParam().sortAscending());
        if (geoRadius.iterator().hasNext())
            return geoRadius.iterator().next().getMemberByString();
        return null;
    }

    public void closePool(){
        this.jedisPool.destroy();
    }
}

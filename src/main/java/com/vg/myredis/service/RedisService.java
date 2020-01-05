package com.vg.myredis.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Simple service for working with Redis
 */
public class RedisService {
    private String hostname;
    private Jedis jedis;
    private Gson gson;

    public RedisService(String hostname, int port) {
        this.hostname = hostname;
        this.jedis = new Jedis(hostname, port);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public String ping() {
        return jedis.ping();
    }

    public void close() {
        jedis.close();
    }

    /**
     * This command is blocking and is not good in production env of any kind.
     *
     * @return Set<String> of redis keys
     */
    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }



    public String getKey(String key) {
        String keyValue = jedis.get(key);
        JsonParser parser = new JsonParser();

        JsonObject json;
        String prettyJson = "";
        if (keyValue != null) {
            try {
                json = parser.parse(keyValue).getAsJsonObject();
                prettyJson = gson.toJson(json);

            } catch (IllegalStateException ex) {
                prettyJson = keyValue;
            }
        }
        return prettyJson;
    }
    public Long deleteKey(String key){
        return jedis.del(key);
    }

    public String setKey(String key, String value) {
        return jedis.set(key, value);
    }

    public String getKeysInfo() {
        String redisKeysInfo = jedis.info("keyspace");
        String[] infoParts = redisKeysInfo.split(",");
        for (String part : infoParts) {
            if (part.contains("keys")) {
                String[] tmp = part.split("=");
                return tmp[1] + " keys found";
            }
        }
        return " 0 keys found";
    }

    public String getInfo(String section) {
        return jedis.info(section);
    }

    public String getHostname() {
        return hostname;
    }
}
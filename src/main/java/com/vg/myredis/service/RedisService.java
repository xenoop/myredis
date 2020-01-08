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


    /**
     * instance de la connexion à la base de données
     * @param hostname
     * @param port
     */
    public RedisService(String hostname, int port) {
        this.hostname = hostname;
        this.jedis = new Jedis(hostname, port);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * on ping la base pour savoir si la connexion est bien établie
     * @return
     */
    public String ping() {
        return jedis.ping();
    }

    /**
     * on ferme la connexion
     */
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

    /**
     * this method gets a value from redis using a certain key
     *
     * @param key : the key of the value / la clé qu'on va utiliser pour accéder a une valeur
     * @return the value of that certain key / la valeur qu'on veut acceder.
     */

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

    /**
     * cette methode supprime un cle
     * @param key
     * @return
     */
    public Long deleteKey(String key){
        return jedis.del(key);
    }

    /**
     *  cette methode ajoute une cle valeur dans la base redis
     * @param key
     * @param value
     * @return
     */
    public String setKey(String key, String value) {
        return jedis.set(key, value);
    }


    /**
     * cette methode retourne les differentes informationbs sur les clés existantes dans la base
     * @return
     */
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
}
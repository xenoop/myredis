# myRedis
Simple Redis UI client built using JavaFX. [Jedis](https://github.com/xetorthio/jedis) client is used to connect to Redis.

## Features
- Get list of keys (this uses jedis.keys which is blocking)
- Redis key value is formatted in case of JSON object for ease of reading
- Shows Redis server stats: server/clients/cluster etc.

## Building project
Since project is Maven based any IDE would do, just run
```bash
mvn clean install
```

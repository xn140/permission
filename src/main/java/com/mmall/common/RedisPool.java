package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by xuning on 2018/6/13.
 */
@Component
@Slf4j
public class RedisPool {
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis instance(){

        return shardedJedisPool.getResource();
    }

    public void safeClose(ShardedJedis shardedJedis){

        try{
            if(shardedJedis!=null){
                shardedJedis.close();

            }
        }catch(Exception e){
            log.error("return redis resource Exception:" ,e);

        }

    }
}

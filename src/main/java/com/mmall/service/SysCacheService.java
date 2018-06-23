package com.mmall.service;

import com.google.common.base.Joiner;
import com.mmall.beans.CacheKeyConstants;
import com.mmall.common.RedisPool;
import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;


public interface SysCacheService {



    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix) ;

    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) ;

    public String getFromCache(CacheKeyConstants prefix, String... keys);
}

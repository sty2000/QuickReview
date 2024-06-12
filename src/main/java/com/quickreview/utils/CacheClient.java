package com.quickReview.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.quickReview.utils.RedisConstants.CACHE_NULL_TTL;
import static com.quickReview.utils.RedisConstants.LOCK_SHOP_KEY;

@Slf4j
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        // set expire time
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        // write to redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    public <R,ID> R queryWithPassThrough(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        // 1.lood for the shop cache in Redis
        String json = stringRedisTemplate.opsForValue().get(key);
        // 2.sees if it exists
        if (StrUtil.isNotBlank(json)) {
            // 3.yes, return directly
            return JSONUtil.toBean(json, type);
        }
        // if empty
        if (json != null) {
            return null;
        }

        // 4.no, query the database by id
        R r = dbFallback.apply(id);
        // 5.no exist, return error
        if (r == null) {
            // write the empty value to Redis
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            // return error message
            return null;
        }
        // 6.yes, write to Redis
        this.set(key, r, time, unit);
        return r;
    }

    public <R, ID> R queryWithLogicalExpire(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        // 1.look for the shop cache in Redis
        String json = stringRedisTemplate.opsForValue().get(key);
        // 2.sees if it exists
        if (StrUtil.isBlank(json)) {
            // 3.yes, return directly
            return null;
        }
        // 4.hit, parse the data
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        // 5.sees if it is expired
        if(expireTime.isAfter(LocalDateTime.now())) {
            // 5.1.no, return directly
            return r;
        }
        // 5.2.yes, clear the cache and rebuild
        // 6.rebuild the cache
        // 6.1.get the lock
        String lockKey = LOCK_SHOP_KEY + id;
        boolean isLock = tryLock(lockKey);
        // 6.2.sees if the lock is obtained
        if (isLock){
            // 6.3.yes, open a new thread to rebuild the cache
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // Query the database
                    R newR = dbFallback.apply(id);
                    // rebuild the cache
                    this.setWithLogicalExpire(key, newR, time, unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    // free the lock
                    unlock(lockKey);
                }
            });
        }
        // 6.4.return the expired data
        return r;
    }

    public <R, ID> R queryWithMutex(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        // 1.look for the shop cache in Redis
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        // 2.sees if it exists
        if (StrUtil.isNotBlank(shopJson)) {
            // 3.yes, return directly
            return JSONUtil.toBean(shopJson, type);
        }
        // see if empty
        if (shopJson != null) {
            return null;
        }

        // 4.cache rebuild
        // 4.1.get the lock
        String lockKey = LOCK_SHOP_KEY + id;
        R r = null;
        try {
            boolean isLock = tryLock(lockKey);
            // 4.2.sees if the lock is obtained
            if (!isLock) {
                // 4.3.no, wait for 50ms and try again
                Thread.sleep(50);
                return queryWithMutex(keyPrefix, id, type, dbFallback, time, unit);
            }
            // 4.4.yes, query the database by id
            r = dbFallback.apply(id);
            // 5.does not exist, return error
            if (r == null) {
                // write the empty value to Redis
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                // return error message
                return null;
            }
            // 6.yes, write to Redis
            this.set(key, r, time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            // 7.free the lock
            unlock(lockKey);
        }
        // 8.return
        return r;
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}

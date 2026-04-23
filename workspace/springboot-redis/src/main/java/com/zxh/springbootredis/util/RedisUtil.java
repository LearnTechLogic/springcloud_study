package com.zxh.springbootredis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return 陈工失败
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                Boolean boo = redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return boo != null && boo;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            Boolean boo = redisTemplate.hasKey(key);
            return boo != null && boo;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public boolean del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));

                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据前缀删除key
     * @param prex
     */
    public boolean deleteByPrex(String prex) {
        try {
            prex = prex+"**";
            Set<String> keys = redisTemplate.keys(prex);
            redisTemplate.delete(keys);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try{
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 加锁
     *
     * @param key         键
     * @param value         值
     * @param releaseTime 锁过期时间 秒
     * @return 结果
     */
    public boolean lock(String key, Object value, long releaseTime) {
        try {
            Boolean boo = redisTemplate.opsForValue().setIfAbsent(key, value, releaseTime, TimeUnit.SECONDS);
            return boo != null && boo;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解锁
     *
     * @param key key
     */
    public boolean unLock(String key) {
        try {
            Boolean boo = redisTemplate.delete(key);
            return boo != null && boo;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 向指定list的队列头部批量添加value
     *
     * @param key key
     */
    public boolean rightPush(String key, String value) {
        try {
            Long aLong = redisTemplate.opsForList().rightPush(key, value);
            return aLong != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 向指定list的队列头部批量添加value
     *
     * @param key key
     */
    public boolean leftPush(String key, String value) {
        try {
            Long aLong = redisTemplate.opsForList().leftPush(key, value);
            return aLong != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 移除并获取指定list中(队列-头部/尾部)第一个元素
     *
     * @param key key
     */
    public Object leftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 指定key+！
     *
     * @param key key
     */
    public Object incr(String key,Long num) {
        try {
            return redisTemplate.opsForValue().increment(key,num);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 根据参数查询相关KEY集合
     */
    public Set<String> getKeyListByStr(String key){
        key = key+"**";
        return redisTemplate.keys(key);
    }


}
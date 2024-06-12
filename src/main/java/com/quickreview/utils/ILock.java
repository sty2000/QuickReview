package com.quickReview.utils;

public interface ILock {

    /**
     * try to get lock
     * @param timeoutSec teh expire time of the lock, it will be released automatically after expiration
     * @return true means get lock successed, false means get lock failed
     */
    boolean tryLock(long timeoutSec);

    /**
     * 释放锁
     */
    void unlock();
}

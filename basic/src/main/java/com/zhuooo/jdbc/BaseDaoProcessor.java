package com.zhuooo.jdbc;

import com.zhuooo.jdbc.dao.BaseCacheDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.List;

/**
 * 加载缓存
 */
@Component
public class BaseDaoProcessor implements BeanPostProcessor {
    private final List<BaseCacheDao> list = new LinkedList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BaseCacheDao) {
            BaseCacheDao cacheDao = (BaseCacheDao) bean;
            list.add(cacheDao);
            cacheDao.loadCache();
        }

        return bean;
    }

    @Scheduled(cron = "30 * * * * ?")
    private void reload() {
        System.out.println("Scheduled reloading ");
        for (BaseCacheDao cacheDao : list) {
            cacheDao.loadCache();
        }
    }

    public <T> void load(String key, T value) {
        if (value != null && key != null) {
            JdbcCache.put(key, value);
        }
    }

}

package com.hetero.heteroiconnect;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

//	@Bean
//	public CacheManager cacheManager() {
//		ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
//		cacheManager.setCacheNames(Arrays.asList("workplaces", "taskalignments", "categories", "priorities",
//				"tasktypes", "outcomes", "activities", "plannedadhoc", "departments", "designations", "managers",
//				"assettypes", "domains"));
//		return cacheManager; 
//	}

	@Bean
	public CacheManager cacheManager() {
		ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
		cacheManager.setCacheNames(Arrays.asList("workplaces", "taskalignments", "categories", "priorities",
				"tasktypes", "outcomes", "activities", "plannedadhoc", "usermanual", "gender", "forms"));
		return cacheManager;
	}
}

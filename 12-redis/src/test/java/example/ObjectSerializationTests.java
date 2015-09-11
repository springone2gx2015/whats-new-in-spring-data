/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import example.Vehicle.Car;
import example.Vehicle.Truck;
import redis.embedded.RedisServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Thomas Darimont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({ RedisTestConfiguration.class, ObjectSerializationTests.Config.class })
public class ObjectSerializationTests {

	private RedisServer redisServer;

	@Autowired private RedisOperations<String, Object> redis;

	@Configuration
	static class Config {

		@Bean(name = "redisTemplate")
		RedisOperations<String, Object> genericRedisTemplate(RedisConnectionFactory rcf) {

			RedisTemplate<String, Object> rt = new RedisTemplate<String, Object>();
			rt.setConnectionFactory(rcf);
			rt.setKeySerializer(new StringRedisSerializer());

			// Fixed to Vehicle type - no type information carried to redis
			rt.setValueSerializer(new Jackson2JsonRedisSerializer<>(Vehicle.class));

			// Type is store in redis
			// rt.setValueSerializer(new GenericJackson2JsonRedisSerializer());

			return rt;
		}
	}

	@Before
	public void setup() throws Exception {

		redisServer = new RedisServer(6379);
		redisServer.start();
	}

	@After
	public void teardown() {
		redisServer.stop();
	}

	@Test
	public void readWrite() {

		// monitor commands sent to redis via redis-cli > monitor

		Vehicle car = new Car();
		car.setBrand("Porsche");

		Vehicle truck = new Truck();
		truck.setBrand("MAN");

		redis.opsForValue().set("v1", car);
		redis.opsForValue().set("v2", truck);

		assertThat(redis.opsForValue().get("v1"), is(car));
		assertThat(redis.opsForValue().get("v2"), is(truck));
	}
}

package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.embedded.RedisServer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RedisTestConfiguration.class, ObjectSerializationTests.Config.class })
public class ObjectSerializationTests {

	private RedisServer redisServer;

	@Autowired private RedisOperations<String, Object> redis;

	static class Config {

		@Bean(name = "redisTemplate")
		RedisOperations<String, Object> genericRedisTemplate(RedisConnectionFactory rcf) {

			RedisTemplate<String, Object> rt = new RedisTemplate<String, Object>();
			rt.setConnectionFactory(rcf);
			rt.setKeySerializer(new StringRedisSerializer());

			// Fixed to Vehicle type - no type information carried to redis
			rt.setValueSerializer(new Jackson2JsonRedisSerializer(Vehicle.class));

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

		//monitor commands sent to redis via redis-cli > monitor

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

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

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.embedded.RedisServer;

/**
 * @author Thomas Darimont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RedisTestConfiguration.class })
public class RedisOperationsTests {

	private RedisServer redisServer;

	@Autowired private RedisConnectionFactory redisConnectionFactory;
	@Autowired private RedisOperations<String, String> redis;

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
	public void zrangeByLex() {

		 BoundZSetOperations<String, String> zsets = redis.boundZSetOps("myzset");
		 zsets.add("a", 0.0);
		 zsets.add("b", 0.0);
		 zsets.add("c", 0.0);
		 zsets.add("d", 0.0);
		 zsets.add("e", 0.0);
		 zsets.add("f", 0.0);
		 zsets.add("g", 0.0);
		 zsets.persist();

		RedisConnection redisConnection = redisConnectionFactory.getConnection();

		Set<byte[]> result = redisConnection.zRangeByLex("myzset".getBytes(), Range.range().lte("c"));
		for (byte[] item : result) {
			System.out.println(new String(item));
		}

		System.out.println("###");

		Set<byte[]> result2 = redisConnection.zRangeByLex("myzset".getBytes(), Range.range().lt("c"));
		for (byte[] item : result2) {
			System.out.println(new String(item));
		}

		System.out.println("###");

		Set<byte[]> result3 = redisConnection.zRangeByLex("myzset".getBytes(), Range.range().gt("aaa").lt("g"));
		for (byte[] item : result3) {
			System.out.println(new String(item));
		}
	}
}

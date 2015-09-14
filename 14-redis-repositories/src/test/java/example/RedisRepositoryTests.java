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

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.embedded.RedisServer;

/**
 * @author Thomas Darimont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RedisTestConfiguration.class)
public class RedisRepositoryTests {

	RedisServer redisServer;

	@Autowired RedisConnectionFactory redisConnectionFactory;
	@Autowired RedisOperations<String, String> redis;
	@Autowired KeyValueTemplate kvTemplate;

	@Autowired PersonRepository repository;

	@Before
	public void setup() throws Exception {

		redisServer = new RedisServer(6379);
		redisServer.start();

		kvTemplate.delete(Person.class);
		kvTemplate.delete(City.class);
	}

	@After
	public void teardown() {
		redisServer.stop();
	}

	@Test
	public void simpleFindByMultipleProperties() {

		Person egwene = new Person();
		egwene.firstname = "egwene";
		egwene.lastname = "al'vere";
		egwene.city = new City("new york");
		

		Person marin = new Person();
		marin.firstname = "marin";
		marin.lastname = "al'vere";

		repository.save(Arrays.asList(egwene, marin));

		assertThat(repository.findByLastname("al'vere").size(), is(2));

		assertThat(repository.findByFirstnameAndLastname("egwene", "al'vere").size(), is(1));
		assertThat(repository.findByFirstnameAndLastname("egwene", "al'vere").get(0), is(egwene));
	}
}

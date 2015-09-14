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

import java.util.Arrays;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.index.RedisIndexDefinition;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import example.RedisTestConfiguration.CustomIndexConfiguration;

/**
 * @author Thomas Darimont
 */
@SpringBootApplication
@EnableRedisRepositories(indexConfiguration = CustomIndexConfiguration.class)
public class RedisTestConfiguration {

	@Autowired RedisConnectionFactory factory;

	/**
	 * Clear database before shut down.
	 */
	@PreDestroy
	public void flushTestDb() {
		factory.getConnection().flushDb();
	}

	static class CustomIndexConfiguration extends IndexConfiguration {

		@Override
		protected Iterable<RedisIndexDefinition> initialConfiguration() {
			return Arrays.asList(new RedisIndexDefinition("persons", "lastname"));
		}
	}
}

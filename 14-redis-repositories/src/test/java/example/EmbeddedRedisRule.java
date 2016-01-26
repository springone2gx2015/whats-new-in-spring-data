/*
 * Copyright 2016 the original author or authors.
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

import redis.embedded.RedisServer;

import java.io.IOException;

import org.junit.rules.ExternalResource;

/**
 * JUnit rule implementation to start and shut down an embedded Redis instance.
 * 
 * @author Christoph Strobl
 * @author Oliver Gierke
 */
public class EmbeddedRedisRule extends ExternalResource {

	private RedisServer server;

	/*
	 * (non-Javadoc)
	 * @see org.junit.rules.ExternalResource#before()
	 */
	@Override
	protected void before() throws IOException {

		this.server = new RedisServer(6379);
		this.server.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.junit.rules.ExternalResource#after()
	 */
	@Override
	protected void after() {
		this.server.stop();
	}
}

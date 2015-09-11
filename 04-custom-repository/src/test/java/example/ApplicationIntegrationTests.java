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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test to show the usage of Java 8 date time APIs with Spring Data JPA auditing.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@Transactional
public class ApplicationIntegrationTests {

	@SpringBootApplication
	@EnableJpaRepositories(repositoryBaseClass = CustomBaseRepository.class)
	static class Config {}

	@Autowired CustomerRepository repository;

	@Test
	public void providesFindOneWithOptional() {

		Customer carter = repository.save(new Customer("Carter", "Beauford", "carter@dmband.com"));

		assertThat(repository.findOne(carter.getId()), is(notNullValue()));
		assertThat(repository.findOne(carter.getId() + 4711), is(nullValue()));
	}
}

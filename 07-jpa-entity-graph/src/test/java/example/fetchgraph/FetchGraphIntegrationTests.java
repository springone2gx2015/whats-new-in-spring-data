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
package example.fetchgraph;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collections;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test showing the usage of JPA 2.1 fetch graph support through Spring Data JPA repositories.
 * 
 * @author Thomas Darimont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringApplicationConfiguration
public class FetchGraphIntegrationTests {

	@SpringBootApplication
	static class Config {}

	@Autowired EntityManager em;
	@Autowired ProductRepository repository;

	@Test
	public void shouldFetchAssociationMarkedAsLazyViaNamedEntityFetchGraph() {

		Product xps = new Product("Dell XPS 15");
		Collections.addAll(xps.getTags(), new Tag("cool"), new Tag("macbook-killer"), new Tag("speed"));

		xps = repository.save(xps);

		em.flush();
		em.detach(xps);

		Product loadedXps = repository.findOne(xps.getId());
		em.detach(loadedXps);

		try {
			loadedXps.getTags().toString();
			fail("Expected LazyInitializationException to occur when trying to access uninitialized association 'tags'.");
		} catch (Exception expected) {
			System.out.println(expected.getMessage());
		}

		// Here we use the findOneById that uses a NamedEntityGraph
		Product loadedXpsWithFetchGraph = repository.findOneById(xps.getId());

		assertThat(loadedXpsWithFetchGraph.getTags(), hasSize(3));
	}

	@Test
	public void shouldFetchAssociationMarkedAsLazyViaCustomEntityFetchGraph() {

		Product xps = new Product("Dell XPS 15");
		Collections.addAll(xps.getTags(), new Tag("cool"), new Tag("macbook-killer"), new Tag("speed"));

		xps = repository.save(xps);

		em.flush();
		em.detach(xps);

		Product loadedXps = repository.findOne(xps.getId());
		em.detach(loadedXps);

		try {
			loadedXps.getTags().toString();
			fail("Expected LazyInitializationException to occur when trying to access uninitialized association 'tags'.");
		} catch (Exception expected) {
			System.out.println(expected.getMessage());
		}

		// Here we use getOneById which uses an ad-hoc declarative fetch graph definition
		Product loadedXpsWithFetchGraph = repository.getOneById(xps.getId());

		assertThat(loadedXpsWithFetchGraph.getTags(), hasSize(3));
	}
}

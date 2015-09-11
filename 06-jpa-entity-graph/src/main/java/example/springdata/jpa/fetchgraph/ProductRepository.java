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
package example.springdata.jpa.fetchgraph;

import javax.persistence.FetchType;
import javax.persistence.NamedEntityGraph;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Thomas Darimont
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * Here we use the {@link EntityGraph} annotation to specify that we want to use the {@link NamedEntityGraph}
	 * <code>product-with-tags</code> specified on the {@link Product} entity.
	 * 
	 * @param id
	 * @return
	 */
	@EntityGraph("product-with-tags")
	Product findOneById(Long id);

	/**
	 * Here we use the {@link EntityGraph} annotation to specify that we want the {@link Product#tags} association which
	 * is marked as {@link FetchType#LAZY} to be fetched eagerly.
	 * 
	 * @param id
	 * @return
	 */
	@EntityGraph(attributePaths = "tags")
	Product getOneById(Long id);
}

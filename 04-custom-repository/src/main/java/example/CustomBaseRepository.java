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

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * A custom extension of {@link SimpleJpaRepository}. See {@link ApplicationIntegrationTests.Config} for how to register
 * it.
 * 
 * @author Oliver Gierke
 * @see ApplicationIntegrationTests.Config
 */
@Slf4j
class CustomBaseRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

	/**
	 * Creates a new {@link CustomBaseRepository} for the given {@link JpaEntityInformation} and {@link EntityManager}.
	 * 
	 * @param entityInformation must not be {@literal null}.
	 * @param entityManager must not be {@literal null}.
	 */
	public CustomBaseRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#findOne(java.io.Serializable)
	 */
	@Override
	public T findOne(ID id) {

		log.info("CustomBaseRepository.findOne(…) called!");

		return super.findOne(id);
	}
}

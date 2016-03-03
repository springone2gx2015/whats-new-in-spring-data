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

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.CrudRepository;

import com.querydsl.core.types.dsl.StringPath;

/**
 * Repository to manage {@link User}s. Also implements {@link QueryDslPredicateExecutor} to enable predicate filtering
 * on Spring MVC controllers as well as {@link QuerydslBinderCustomizer} to tweak the way predicates are created for
 * properties.
 * 
 * @author Christoph Strobl
 * @author Oliver Gierke
 */
public interface UserRepository
		extends CrudRepository<User, String>, QueryDslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.querydsl.binding.QuerydslBinderCustomizer#customize(org.springframework.data.querydsl.binding.QuerydslBindings, com.mysema.query.types.EntityPath)
	 */
	@Override
	default public void customize(QuerydslBindings bindings, QUser user) {

		// TODO: 02 - Binding customizations for Querydsl

		// Customize binding by referring to property
		bindings.bind(user.firstname).first((path, value) -> path.eq(value));

		// Customize binding by type
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));

		// Exclude properties from binding
		bindings.excluding(user.password);
	}
}

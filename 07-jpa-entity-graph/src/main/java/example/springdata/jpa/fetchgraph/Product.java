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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Thomas Darimont
 */
@Data
@NoArgsConstructor
@Entity
@NamedEntityGraphs(@NamedEntityGraph(name = "product-with-tags", attributeNodes = { @NamedAttributeNode("tags") }))
public class Product {

	@Id @GeneratedValue//
	Long id;

	String name;

	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Tag.class, cascade = CascadeType.ALL)//
	Set<Tag> tags = new HashSet<>();

	public Product(String name) {
		this.name = name;
	}
}

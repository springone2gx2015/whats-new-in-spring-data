package example;

import java.util.Collections;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Data;

@Data
@KeySpace("persons")
class Person {

	@Id String id;
	@Indexed String firstname;
	String lastname;

	Map<String, String> attributes = Collections.emptyMap();

	City city;

	@Reference Person mother;
}

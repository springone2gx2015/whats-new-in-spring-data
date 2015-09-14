package example;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@KeySpace("cities")
class City {
	
	@Id String id;
	String name;
	
	public City(String name) {
		this.name = name;
	}
	
}

package example;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

interface PersonRepository extends CrudRepository<Person, String> {

	List<Person> findByFirstname(String firstname);

	List<Person> findByLastname(String lastname);

	List<Person> findByFirstnameAndLastname(String firstname, String lastname);
}

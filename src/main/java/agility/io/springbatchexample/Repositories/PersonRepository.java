package agility.io.springbatchexample.Repositories;

import agility.io.springbatchexample.domains.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}

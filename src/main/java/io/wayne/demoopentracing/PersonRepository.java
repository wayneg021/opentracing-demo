package io.wayne.demoopentracing;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface PersonRepository extends CassandraRepository<Person, Long> {
}

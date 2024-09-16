package com.indium.repository;

import com.indium.Entity.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event,Integer> {
}

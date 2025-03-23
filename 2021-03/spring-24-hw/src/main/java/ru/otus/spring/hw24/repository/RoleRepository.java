package ru.otus.spring.hw24.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.hw24.domain.Role;

import java.util.List;

@RepositoryRestResource(path = "role")
public interface RoleRepository extends JpaRepository<Role, Long> {
    @RestResource(path = "by-name", rel = "by-name")
    List<Role> findByNameContainsIgnoreCase(@Param("name") String name);
}

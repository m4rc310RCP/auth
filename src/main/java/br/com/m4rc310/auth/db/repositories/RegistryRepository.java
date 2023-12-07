package br.com.m4rc310.auth.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.m4rc310.auth.db.models.Registry;

public interface RegistryRepository extends JpaRepository<Registry, Long> {

}

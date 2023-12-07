package br.com.m4rc310.auth.db.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.m4rc310.auth.db.models.User;
import br.com.m4rc310.auth.db.models.auth.AuthUser;
import br.com.m4rc310.auth.db.models.auth.AuthUserId;

public interface AuthUserRepository extends JpaRepository<AuthUser, AuthUserId> {
	Long findTopByIdUserOrderByIdSequenceDesc(User user);
	Optional<AuthUser> findByIdUserUsername(String username);
}

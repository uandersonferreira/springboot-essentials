package br.com.uanderson.springboot.repository;

import br.com.uanderson.springboot.domain.DevDojoUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevDojoUserDetailsRepository extends JpaRepository<DevDojoUserDetails, Long> {
    DevDojoUserDetails findByUsername(String username);
}

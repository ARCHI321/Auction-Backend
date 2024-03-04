package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.Authority;
import com.herovired.Auction.Management.System.models.UserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    @Query(value = "SELECT * FROM authorities a WHERE a.username = :username", nativeQuery = true)
    Optional<Authority> findByUserName(String username);
}

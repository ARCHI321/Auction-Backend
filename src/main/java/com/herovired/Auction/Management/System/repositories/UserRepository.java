package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserName(String username);
    Optional<User> findByUserId(String userId);

    String findNameByUserId(String userId);

}

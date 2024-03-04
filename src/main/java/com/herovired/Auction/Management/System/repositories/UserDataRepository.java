package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.User;
import com.herovired.Auction.Management.System.models.UserData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDataRepository extends CrudRepository<UserData, Long> {

    Optional<UserData> findByUserName(String username);

}

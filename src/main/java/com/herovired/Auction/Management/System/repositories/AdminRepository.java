package com.herovired.Auction.Management.System.repositories;

import com.herovired.Auction.Management.System.models.Admin;
import com.herovired.Auction.Management.System.models.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Long> {

}
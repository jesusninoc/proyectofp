package com.diegoflores.onlineacademy.repository;

import com.diegoflores.onlineacademy.domain.Admin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Admin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {}

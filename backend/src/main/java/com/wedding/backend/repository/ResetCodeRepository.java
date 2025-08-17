package com.wedding.backend.repository;

import com.wedding.backend.model.Guest;
import com.wedding.backend.model.ResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetCodeRepository extends JpaRepository<ResetCode, String> {
    Optional<ResetCode> findByGuestAndStatus(Guest guest, String status);
}

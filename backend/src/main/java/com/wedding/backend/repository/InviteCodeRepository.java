package com.wedding.backend.repository;

import com.wedding.backend.model.Guest;
import com.wedding.backend.model.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteCodeRepository extends JpaRepository<InviteCode, Integer> {
    Optional<InviteCode> findByGuestAndStatus(Guest guest, String status);
}

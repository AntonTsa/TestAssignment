package ua.anton.tsa.testassigment.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.anton.tsa.testassigment.model.User;

import java.time.LocalDate;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Page<User> findAllByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);
}

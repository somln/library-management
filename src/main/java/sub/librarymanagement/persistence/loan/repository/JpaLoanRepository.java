package sub.librarymanagement.persistence.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sub.librarymanagement.persistence.loan.entity.Loan;

import java.util.List;
import java.util.Optional;

public interface JpaLoanRepository extends JpaRepository<Loan, Long> {
    Optional<Object> findByIdAndReturnedFalse(Long bookId);

    List<Loan> findByBookId(Long bookId);
}

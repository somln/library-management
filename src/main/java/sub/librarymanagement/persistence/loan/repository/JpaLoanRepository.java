package sub.librarymanagement.persistence.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sub.librarymanagement.persistence.loan.entity.Loan;

public interface JpaLoanRepository extends JpaRepository<Loan, String> {
}

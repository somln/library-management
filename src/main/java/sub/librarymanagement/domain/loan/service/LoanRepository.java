package sub.librarymanagement.domain.loan.service;

import sub.librarymanagement.persistence.loan.entity.Loan;

import java.security.cert.Extension;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {

    Optional<Object> findByBookIdAndReturnedFalse(Long bookId);

    List<Loan> findByBookId(Long bookId);

    void save(Loan loan);

    Loan findById(Long loanId);
}

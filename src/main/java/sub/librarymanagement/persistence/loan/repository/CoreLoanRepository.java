package sub.librarymanagement.persistence.loan.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.loan.service.LoanRepository;
import sub.librarymanagement.persistence.loan.entity.Loan;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class CoreLoanRepository implements LoanRepository {

    private final JpaLoanRepository jpaLoanRepository;


    @Override
    public Optional<Object> findByBookIdAndReturnedFalse(Long bookId) {
        return jpaLoanRepository.findByIdAndReturnedFalse(bookId);
    }

    @Override
    public List<Loan> findByBookId(Long bookId) {
        return jpaLoanRepository.findByBookId(bookId);
    }

    @Override
    public void save(Loan loan) {
        jpaLoanRepository.save(loan);
    }

    @Override
    public Loan findById(Long loanId) {
        return jpaLoanRepository.findById(loanId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.LOAN_NOT_FOUND));
    }
}

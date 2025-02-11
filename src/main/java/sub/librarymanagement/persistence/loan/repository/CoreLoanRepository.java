package sub.librarymanagement.persistence.loan.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.domain.loan.service.LoanRepository;

@Repository
@Transactional
@RequiredArgsConstructor
public class CoreLoanRepository implements LoanRepository {

    private final JpaLoanRepository jpaLoanRepository;

}

package sub.librarymanagement.domain.loan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.book.service.BookRepository;
import sub.librarymanagement.domain.user.service.UserRepository;
import sub.librarymanagement.persistence.loan.entity.Loan;
import sub.librarymanagement.persistence.user.entity.User;
import sub.model.IsLoanedDto;
import sub.model.LoanIdDto;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanIdDto registerLoan(Long bookId, User user) {
        validateLoanAvailability(bookId);
        User currentUser = userRepository.getByUsername(user.getUsername());
        Loan loan = Loan.of(currentUser.getId(), bookId);
        loanRepository.save(loan);
        return new LoanIdDto().loanId(loan.getId());
    }

    public void validateLoanAvailability(Long bookId) {
        //대출 중인 책이면 대출 불가
        if (loanRepository.findByBookIdAndReturnedFalse(bookId).isPresent()) {
            throw new ApplicationException(ErrorCode.CANNOT_LOAN_BOOK);
        }
    }

    @Transactional
    public LoanIdDto returnLoan(Long loanId, User user) {
        Loan loan = loanRepository.findById(loanId);
        User currentUser = userRepository.getByUsername(user.getUsername());
        validateLoanReturn(loan, currentUser);
        loan.returnBook(loan);
        return new LoanIdDto().loanId(loan.getId());
    }

    public void validateLoanReturn(Loan loan, User user) {
        //이미 반납된 대출이면 반납 불가
        if (loan.isReturned()) {
            throw new ApplicationException(ErrorCode.ALREADY_RETURNED_LOAN);
        }
        //다른 사용자의 대출이면 반납 불가
        if(!loan.getUserId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.CANNOT_RETURN_OTHER_USER_LOAN);
        }
    }

    public IsLoanedDto isLoaned(Long bookId) {
        bookRepository.findBookById(bookId);
        return new IsLoanedDto().isLoaned(loanRepository.findByBookIdAndReturnedFalse(bookId).isPresent());
    }
}

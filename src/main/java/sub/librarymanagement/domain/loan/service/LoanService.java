package sub.librarymanagement.domain.loan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.book.service.BookRepository;
import sub.librarymanagement.domain.loan.dto.IsLoanedDto;
import sub.librarymanagement.domain.loan.dto.LoanIdDto;
import sub.librarymanagement.persistence.loan.entity.Loan;
import sub.librarymanagement.persistence.user.entity.User;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanIdDto registerLoan(Long bookId, User user) {
        validateLoanAvailability(bookId);
        Loan loan = Loan.of(user.getId(), bookId);
        loanRepository.save(loan);
        return new LoanIdDto(loan.getId());
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
        validateLoanReturn(loan, user);
        loan.returnBook(loan);
        return new LoanIdDto(loan.getId());
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
        bookRepository.findById(bookId);
        return IsLoanedDto.from(loanRepository.findByBookIdAndReturnedFalse(bookId).isPresent());
    }
}

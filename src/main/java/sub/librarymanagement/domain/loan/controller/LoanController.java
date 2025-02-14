package sub.librarymanagement.domain.loan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sub.api.LoansApi;
import sub.librarymanagement.common.auth.userDetail.CustomUserDetails;
import sub.librarymanagement.domain.loan.service.LoanService;
import sub.librarymanagement.persistence.user.entity.User;
import sub.model.IsLoanedDto;
import sub.model.LoanIdDto;
import sub.model.ResponseDtoIsLoanedDto;
import sub.model.ResponseDtoLoanIdDto;

@RestController
@RequiredArgsConstructor
public class LoanController implements LoansApi {

    private final LoanService loanService;

    @Override
    public ResponseEntity<ResponseDtoIsLoanedDto> loansBooksBookIdIsLoanedGet(Long bookId) {
        IsLoanedDto loaned = loanService.isLoaned(bookId);
        ResponseDtoIsLoanedDto response = new ResponseDtoIsLoanedDto().code(200).data(loaned);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoLoanIdDto> loansBooksBookIdPost(Long bookId) {
        LoanIdDto loanIdDto = loanService.registerLoan(bookId, getCurrentUser());

        ResponseDtoLoanIdDto response = new ResponseDtoLoanIdDto().code(200).data(loanIdDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoLoanIdDto> loansLoanIdPut(Long loanId) {
        LoanIdDto loanIdDto = loanService.returnLoan(loanId, getCurrentUser());

        ResponseDtoLoanIdDto response = new ResponseDtoLoanIdDto().code(200).data(loanIdDto);
        return ResponseEntity.ok(response);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

}

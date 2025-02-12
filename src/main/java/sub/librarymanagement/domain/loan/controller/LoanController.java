package sub.librarymanagement.domain.loan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sub.librarymanagement.common.auth.userDetail.CustomUserDetails;
import sub.librarymanagement.common.util.ResponseDto;
import sub.librarymanagement.domain.loan.dto.IsLoanedDto;
import sub.librarymanagement.domain.loan.dto.LoanIdDto;
import sub.librarymanagement.domain.loan.service.LoanService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/books/{bookId}/loans")
    public ResponseEntity<ResponseDto<LoanIdDto>> registerLoan(
            @PathVariable Long bookId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseDto.okWithData(loanService.registerLoan(bookId, userDetails.getUser())));
    }

    @PutMapping("/loans/{loanId}")
    public ResponseEntity<ResponseDto<LoanIdDto>> returnLoan(
            @PathVariable Long loanId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseDto.okWithData(loanService.returnLoan(loanId, userDetails.getUser())));
    }

    @GetMapping("/books/{bookId}/is-loaned")
    public ResponseEntity<ResponseDto<IsLoanedDto>> isLoaned(
            @PathVariable Long bookId) {
        return ResponseEntity.ok(ResponseDto.okWithData(loanService.isLoaned(bookId)));
    }
}

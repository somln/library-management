package sub.librarymanagement.domain.loan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sub.librarymanagement.domain.loan.service.LoanService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/loans")
public class LoanController {

    private final LoanService loanService;
}

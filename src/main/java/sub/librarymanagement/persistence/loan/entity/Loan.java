package sub.librarymanagement.persistence.loan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Loan {

    @Id
    private String id;

    private String userId;  // 대출한 사용자

    private String bookId;  // 대출한 도서

    private LocalDate loanDate;  // 대출 일자

    private LocalDate returnDate;  // 반납일 (대출된 상태에서는 null)

    private boolean returned;  // 반납 여부

}

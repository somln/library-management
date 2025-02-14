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
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;  // 대출한 사용자

    private Long bookId;  // 대출한 도서

    @Column(nullable = false)
    private LocalDate loanDate;  // 대출 일자

    private LocalDate returnDate;  // 반납일 (대출된 상태에서는 null)

    @Column(nullable = false)
    private boolean returned;  // 반납 여부

    public static Loan of(Long userId, Long bookId) {
        return Loan.builder()
                .userId(userId)
                .bookId(bookId)
                .loanDate(LocalDate.now())
                .returned(false)
                .build();
    }

    public void removeBook() {
        this.bookId = null;
    }

    public void returnBook(Loan loan) {
        this.returnDate = LocalDate.now();
        this.returned = true;
    }
}

package sub.librarymanagement.domain.loan.dto;

public record IsLoanedDto(
        boolean isLoaned
) {
    public static IsLoanedDto from(boolean isLoaned) {
        return new IsLoanedDto(isLoaned);
    }
}

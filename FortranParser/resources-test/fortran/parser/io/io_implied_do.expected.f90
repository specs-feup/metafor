PROGRAM IO_IMPLIED_DO
    IMPLICIT NONE

    INTEGER :: i
    INTEGER :: arr(5)
    CHARACTER(LEN=30) :: input_buffer = "10 20 30 40 50"

    READ(input_buffer, *) (arr(i), i = 1, 5)

    PRINT *, "Output implied DO:"
    PRINT *, (arr(i), i = 1, 5)

    PRINT *, "Modified output implied DO:"
    PRINT *, (arr(i) * 2, i = 1, 5)

END PROGRAM IO_IMPLIED_DO

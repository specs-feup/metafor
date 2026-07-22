SUBROUTINE add_numbers(a, b, result, useless)
    INTEGER, INTENT(IN) :: a, b
    INTEGER, INTENT(OUT) :: result
    INTEGER, INTENT(INOUT) :: useless

    result = a + b
END SUBROUTINE add_numbers

PROGRAM MAIN
    INTEGER :: x, y, total, useless

    x = 5
    y = 12

    CALL add_numbers(x, y, total, useless)

    PRINT *, "The sum is:", total
END PROGRAM MAIN
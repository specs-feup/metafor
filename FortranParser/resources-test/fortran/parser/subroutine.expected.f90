SUBROUTINE add_numbers(a, b, result, useless)
    INTEGER, intent(in) :: a, b
    INTEGER, intent(out) :: result
    INTEGER, intent(inout) :: useless

    result = a + b
END SUBROUTINE add_numbers

PROGRAM MAIN
    INTEGER :: x, y, total, useless

    x = 5
    y = 12

    call add_numbers(x, y, total, useless)

    PRINT *, "The sum is:", total
END PROGRAM MAIN
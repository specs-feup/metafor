SUBROUTINE add_numbers(a, b, result, useless)
    integer, intent(in) :: a, b
    integer, intent(out) :: result
    integer, intent(inout) :: useless

    result = a + b
END SUBROUTINE add_numbers

PROGRAM MAIN
    integer :: x, y, total, useless

    x = 5
    y = 12

    call add_numbers(x, y, total, useless)

    PRINT *, "The sum is:", total
END PROGRAM MAIN
PROGRAM FUNCTION
    INTEGER :: x, y, total, useless

    x = 5
    y = 12
    useless = 0

    total = add_numbers(x, y, useless)

    PRINT *, "The sum is:", total

CONTAINS
    FUNCTION add_numbers(a, b, useless) RESULT(res)
        INTEGER, INTENT(IN) :: a, b
        INTEGER, INTENT(INOUT) :: useless
        INTEGER :: res

        res = a + b
    END FUNCTION add_numbers

END PROGRAM FUNCTION
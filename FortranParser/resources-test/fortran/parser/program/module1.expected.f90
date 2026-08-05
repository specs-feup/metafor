MODULE math_utils_mod
    IMPLICIT NONE

    PRIVATE
    PUBLIC :: add, square

CONTAINS

    PURE FUNCTION add(a, b) RESULT(res)
        INTEGER, INTENT(IN) :: a, b
        INTEGER :: res
        res = a + b
    END FUNCTION add

    PURE FUNCTION square(x) RESULT(res)
        INTEGER, INTENT(IN) :: x
        INTEGER :: res
        res = x * x
    END FUNCTION square

END MODULE math_utils_mod
PROGRAM IMPLICIT
    ! 1. Standard IMPLICIT NONE (requires all variables to be declared)
    IMPLICIT NONE (TYPE, EXTERNAL)
    INTEGER :: explicit_val

    explicit_val = 42
    PRINT *, "Explicit:", explicit_val

    CALL demo_custom_implicit()
CONTAINS
    SUBROUTINE demo_custom_implicit()
        ! 2. Custom IMPLICIT rules by letter range
        IMPLICIT REAL(8) (a-c)  ! Names starting with A, B, C -> 8-byte REAL
        IMPLICIT INTEGER(8) (i-k)  ! Names starting with I, J, K -> 8-byte INTEGER

        a_var = 3.141592653589793_8  ! Implicit REAL(8)
        i_var = 1000000000000_8  ! Implicit INTEGER(8)

        PRINT *, "Custom implicit kinds:", kind(a_var), kind(i_var)
    END SUBROUTINE demo_custom_implicit
END PROGRAM IMPLICIT
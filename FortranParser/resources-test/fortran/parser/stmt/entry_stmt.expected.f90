PROGRAM ENTRY_STMT
    IMPLICIT NONE

    call step_counter(5)
    call step_counter(10)

    ! Call alternate entry point defined via ENTRY statement
    call reset_counter()

    ! Call standard entry point again
    call step_counter(3)

END PROGRAM ENTRY_STMT


SUBROUTINE step_counter(step_val)
    IMPLICIT NONE
    INTEGER, INTENT(IN) :: step_val
    INTEGER, SAVE :: total = 0

    total = total + step_val
    PRINT *, "Stepped:", step_val, "| Total:", total
    RETURN

    ENTRY reset_counter()
    total = 0
    PRINT *, "Counter reset"
END SUBROUTINE step_counter
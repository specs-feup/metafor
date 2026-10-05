program entry_stmt
    implicit none

    call step_counter(5)
    call step_counter(10)

    ! Call alternate entry point defined via ENTRY statement
    call reset_counter()

    ! Call standard entry point again
    call step_counter(3)

end program entry_stmt


subroutine step_counter(step_val)
    implicit none
    integer, intent(in) :: step_val
    integer, save :: total = 0

    total = total + step_val
    print *, "Stepped:", step_val, "| Total:", total
    return

    entry reset_counter()
    total = 0
    print *, "Counter reset"
end subroutine step_counter
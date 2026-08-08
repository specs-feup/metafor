program implicit
    ! 1. Standard IMPLICIT NONE (requires all variables to be declared)
    implicit none (type, external)
    integer :: explicit_val

    explicit_val = 42
    print *, "Explicit:", explicit_val

    call demo_custom_implicit()

contains

    subroutine demo_custom_implicit()
        ! 2. Custom IMPLICIT rules by letter range
        implicit real(8) (a-c)     ! Names starting with A, B, C -> 8-byte REAL
        implicit integer(8) (i-k)  ! Names starting with I, J, K -> 8-byte INTEGER

        a_var = 3.141592653589793_8  ! Implicit REAL(8)
        i_var = 1000000000000_8      ! Implicit INTEGER(8)

        print *, "Custom implicit kinds:", kind(a_var), kind(i_var)
    end subroutine demo_custom_implicit

end program implicit
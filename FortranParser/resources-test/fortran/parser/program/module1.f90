module math_utils_mod
    implicit none

    private
    public :: add, square

contains

    pure function add(a, b) result(res)
        integer, intent(in) :: a, b
        integer :: res
        res = a + b
    end function add

    pure function square(x) result(res)
        integer, intent(in) :: x
        integer :: res
        res = x * x
    end function square

end module math_utils_mod
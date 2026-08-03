program function
    integer :: x, y, total, useless

    x = 5
    y = 12
    useless = 0

    total = add_numbers(x, y, useless)

    print *, "The sum is:", total

contains

    function add_numbers(a, b, useless) result(res)
        integer, intent(in) :: a, b
        integer, intent(inout) :: useless
        integer :: res

        res = a + b
    end function add_numbers

end program function
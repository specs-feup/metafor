subroutine add_numbers(a, b, result, useless)
    integer, intent(in) :: a, b
    integer, intent(out) :: result
    integer, intent(inout) :: useless

    result = a + b
end subroutine add_numbers

program main
    integer :: x, y, total, useless

    x = 5
    y = 12

    call add_numbers(x, y, total, useless)

    print *, "The sum is:", total
end program main
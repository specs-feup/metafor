subroutine add_numbers(a, b, result)
    integer, intent(in)  :: a, b
    integer, intent(out) :: result

    result = a + b
end subroutine add_numbers

program main
    integer :: x, y, total

    x = 5
    y = 12

    call add_numbers(x, y, total)

    print *, "The sum is:", total
end program main
program io_implied_do
    implicit none

    integer :: i
    integer :: arr(5)
    character(len=30) :: input_buffer = "10 20 30 40 50"

    read(input_buffer, *) (arr(i), i = 1, 5)

    print *, "Output implied DO:"
    print *, (arr(i), i = 1, 5)

    print *, "Modified output implied DO:"
    print *, (arr(i) * 2, i = 1, 5)

end program io_implied_do

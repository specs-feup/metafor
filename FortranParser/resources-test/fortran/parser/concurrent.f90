program simple_loop
    integer :: i, j
    real :: x

    do concurrent (i = 1:10:1, j = 1:10, i > 5) shared(x)
        x = 0.0
    end do

end program simple_loop
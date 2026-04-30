PROGRAM simple_loop
    integer :: i, j
    real :: x

    DO CONCURRENT (i = 1:10:1, j = 1:10, i > 5)
        x = 0.0
    END DO

END PROGRAM simple_loop
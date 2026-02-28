PROGRAM simple_loop
    integer :: i, j, x

    DO CONCURRENT (i = 1:10:1, j = 1:10, i > 5) shared(x)
        x = 0
    END DO

END PROGRAM simple_loop
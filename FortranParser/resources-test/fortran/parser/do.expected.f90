PROGRAM simple_loop
    integer :: i, dummy, a = 2

    DO i = 1, 5, a
        dummy = 3
        dummy = 4
    END DO

    DO WHILE (2 > 0)
        dummy = 2
    END DO

    DO
        dummy = 4
    END DO

END PROGRAM simple_loop
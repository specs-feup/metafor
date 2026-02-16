PROGRAM simple_loop
    integer :: i, dummy

    DO i = 1, 5
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
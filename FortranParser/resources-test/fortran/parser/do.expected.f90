PROGRAM SIMPLE_LOOP
    INTEGER :: i, dummy, a = 2

    DO i = 1, 5, a
        dummy = 3
        dummy = 4
    END DO

    DO WHILE (2 > 0)
        dummy = 2
    END DO

    name : DO
        dummy = 4
    END DO name

END PROGRAM SIMPLE_LOOP
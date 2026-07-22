PROGRAM SIMPLE_LOOP
    !use omp_lib
    INTEGER i, dummy, a = 2

    !$OMP PARALLEL DO
    DO i = 1, 5, a
        dummy = 3
        dummy = 4
    END DO
    !$OMP END PARALLEL DO

    DO WHILE (2 > 0)
        dummy = 2
    END DO

    name : DO
        dummy = 4
    END DO name

END PROGRAM SIMPLE_LOOP
PROGRAM OPENMP_DEMO

    INTEGER i, total_sum
    INTEGER shared_counter = 0
    INTEGER, PARAMETER :: n = 32

    DO i = 1, 2
        total_sum = total_sum + 1
    END DO

END PROGRAM OPENMP_DEMO
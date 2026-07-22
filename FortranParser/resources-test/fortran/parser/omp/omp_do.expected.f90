PROGRAM OPENMP_DEMO
    USE omp_lib

    INTEGER i, thread_id, num_threads, total_sum
    INTEGER shared_counter = 0

    !$OMP PARALLEL
        !$OMP DO
        DO i = 1, 100
            total_sum = total_sum
        END DO
        !$OMP END DO
    !$OMP END PARALLEL

    !$OMP PARALLEL DO
    DO i = 1, 2
        total_sum = 1
    END DO
    !$OMP END PARALLEL DO

END PROGRAM OPENMP_DEMO
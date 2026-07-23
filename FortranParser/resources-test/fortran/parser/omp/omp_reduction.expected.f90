PROGRAM OPENMP_DEMO
    USE :: omp_lib

    INTEGER :: i, thread_id, num_threads, total_sum
    INTEGER :: shared_counter = 0

    !$OMP PARALLEL DO reduction(+:total_sum)
    DO i = 1, 2
        total_sum = total_sum + 1
    END DO
    !$OMP END PARALLEL DO

END PROGRAM OPENMP_DEMO
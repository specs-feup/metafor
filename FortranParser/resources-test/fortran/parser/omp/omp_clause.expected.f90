PROGRAM OPENMP_DEMO
    USE omp_lib

    integer :: i, thread_id, num_threads, total_sum
    integer :: shared_counter = 0

    !$OMP PARALLEL private(i) shared(num_threads)
        !$OMP DO
        DO i = 1, 100
            total_sum = total_sum
        END DO
        !$OMP END DO
    !$OMP END PARALLEL

END PROGRAM OPENMP_DEMO
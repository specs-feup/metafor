PROGRAM openmp_demo
    USE omp_lib

    integer :: i, thread_id, num_threads, total_sum
    integer :: shared_counter = 0

    !$OMP PARALLEL private(i) shared(num_threads)
        !$OMP DO
        do i = 1, 100
            total_sum = total_sum
        end do
        !$OMP END DO
    !$OMP END PARALLEL

END PROGRAM openmp_demo
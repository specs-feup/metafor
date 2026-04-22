PROGRAM openmp_demo
    USE omp_lib

    integer :: i, thread_id, num_threads, total_sum
    integer :: shared_counter = 0

    !$OMP PARALLEL DO reduction(+:total_sum)
    do i = 1, 2
        total_sum = total_sum + 1
    end do
    !$OMP END PARALLEL DO

END PROGRAM openmp_demo
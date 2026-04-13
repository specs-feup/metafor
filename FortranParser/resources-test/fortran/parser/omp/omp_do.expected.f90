PROGRAM openmp_demo
    USE omp_lib

    integer :: i, thread_id, num_threads, total_sum
    integer :: shared_counter = 0

    !$OMP PARALLEL
        !$OMP DO
        do i = 1, 100
            total_sum = total_sum
        end do
        !$OMP END DO
    !$OMP END PARALLEL

    !$OMP PARALLEL DO
    do i = 1, 2
        total_sum = 1
    end do
    !$OMP END PARALLEL DO

end program openmp_demo
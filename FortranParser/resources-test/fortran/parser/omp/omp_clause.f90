program openmp_demo
    use omp_lib

    integer :: i, thread_id, num_threads, total_sum
    integer :: shared_counter = 0

    !$omp parallel private(i) shared(num_threads)
        !$omp do
        do i = 1, 100
            total_sum = total_sum
        end do
        !$omp end do
    !$omp end parallel

end program openmp_demo
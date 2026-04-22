program openmp_demo
    use omp_lib

    integer :: i, thread_id, num_threads, total_sum
    integer :: shared_counter = 0

    !$omp parallel do reduction(+:total_sum)
    do i = 1, 2
        total_sum = total_sum + 1
    end do
    !$omp end parallel do

end program openmp_demo
program openmp_demo
    use omp_lib
    implicit none

    integer :: i, thread_id, num_threads, total_sum
    integer :: shared_counter = 0

    !$omp parallel
        !$omp do
        do i = 1, 100
            total_sum = total_sum
        end do
        !$omp end do
    !$omp end parallel

    !$omp parallel do
    do i = 1, 2
        total_sum = 1
    end do
    !$omp end parallel do

end program openmp_demo
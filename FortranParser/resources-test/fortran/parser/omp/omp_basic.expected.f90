program openmp_demo
    use omp_lib
    implicit none

    integer :: total_sum

    !$omp parallel
        total_sum = total_sum + 1
    !$omp end parallel

end program openmp_demo
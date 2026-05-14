program openmp_demo

    integer :: i, total_sum
    integer :: shared_counter = 0
    integer, parameter :: n = 32

    do i = 1, 2
        total_sum = total_sum + 1
    end do

end program openmp_demo
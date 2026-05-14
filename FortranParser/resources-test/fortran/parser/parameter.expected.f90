PROGRAM OPENMP_DEMO

    integer :: i, total_sum
    integer :: shared_counter = 0
    integer, PARAMETER :: n = 32

    DO i = 1, 2
        total_sum = total_sum + 1
    END DO

END PROGRAM OPENMP_DEMO
program simple_loop
    !use omp_lib
    integer :: i, dummy

    !$OMP PARALLEL DO
    do i = 1, 5
        dummy = 3
    end do
    !$OMP END PARALLEL DO

    do while (2 > 0)
        dummy = 2
    end do

end program simple_loop
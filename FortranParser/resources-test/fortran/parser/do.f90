program simple_loop
    !use omp_lib
    integer :: i, dummy, a = 2

    !$OMP PARALLEL DO
    do i = 1, 5, a
        dummy = 3
        dummy = 4
    end do
    !$OMP END PARALLEL DO

    do while (2 > 0)
        dummy = 2
    end do

    name: do
        dummy = 4
    end do name

end program simple_loop
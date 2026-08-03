program common_stmt
    real :: x
    common /DATA_BLOCK/ x, my_array(5)
    integer :: my_array

    x = 42.0
end program common_stmt
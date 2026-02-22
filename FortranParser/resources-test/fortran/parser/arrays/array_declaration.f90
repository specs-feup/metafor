program test_array_assign
    integer, dimension(5) :: arr, arr2, arr3
    integer, allocatable :: arr_alloc(:)
    integer, dimension(2,3,4) :: matrix3d
    integer, dimension(0:4) :: arr_custom_bound

    arr = (/10, 20, 30, 40, 50/)
    arr2 = [10, 20, 30, 40, 50]
    arr3 = [integer :: 10, 20, 30, 40, 50]
    arr_alloc = [integer ::]
    arr_custom_bound = [10, 20, 30, 40, 50]
end program test_array_assign
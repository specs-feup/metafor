PROGRAM test_array_assign
    INTEGER, DIMENSION(5) :: arr, arr2, arr3
    INTEGER, ALLOCATABLE :: arr_alloc(:)
    INTEGER, DIMENSION(2, 3, 4) :: matrix3d
    INTEGER, DIMENSION(0:4) :: arr_custom_bound

    arr = [10, 20, 30, 40, 50]
    arr2 = [10, 20, 30, 40, 50]
    arr3 = [INTEGER :: 10, 20, 30, 40, 50]
    arr_alloc = [INTEGER ::]
    arr_custom_bound = [10, 20, 30, 40, 50]
END PROGRAM test_array_assign
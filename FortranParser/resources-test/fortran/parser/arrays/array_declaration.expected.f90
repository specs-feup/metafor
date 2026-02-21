PROGRAM test_array_assign
    integer, dimension(5) :: arr, arr2, arr3
    integer, ALLOCATABLE :: arr_alloc(:)

    arr = [10, 20, 30, 40, 50]
    arr2 = [10, 20, 30, 40, 50]
    arr3 = [integer :: 10, 20, 30, 40, 50]
    arr_alloc = [integer ::]
END PROGRAM test_array_assign
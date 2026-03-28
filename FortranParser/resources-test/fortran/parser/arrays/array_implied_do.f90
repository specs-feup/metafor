PROGRAM test_implied_do
    integer :: i, j
    integer, dimension(5) :: arr1, arr2, arr3
    integer, dimension(10) :: arr4, arr5
    integer, dimension(9) :: arr6
    integer, dimension(15) :: arr7

    ! Basic implied-do
    arr1 = [(i, i = 1, 5)]

    ! With expression
    arr2 = [(i * 10, i = 1, 5)]

    ! With step
    arr3 = [(i, i = 1, 9, 2)]

    ! Mixed values and implied-do
    arr4 = [0, (i, i = 1, 8), 99]

    ! With type-spec
    arr5 = [integer :: (i * 2, i = 1, 10)]

    ! Nested implied-do
    arr6 = [((i + j, i = 1, 3), j = 1, 3)]

    ! Multiple values per iteration
    arr7 = [(i, i * 10, i * 100, i = 1, 5)]

END PROGRAM test_implied_do
PROGRAM TEST_IMPLIED_DO
    INTEGER :: i, j
    INTEGER, DIMENSION(5) :: arr1, arr2, arr3
    INTEGER, DIMENSION(10) :: arr4, arr5
    INTEGER, DIMENSION(9) :: arr6
    INTEGER, DIMENSION(15) :: arr7

    arr1 = [(i, i = 1, 5)]

    arr2 = [(i * 10, i = 1, 5)]

    arr3 = [(i, i = 1, 9, 2)]

    arr4 = [0, (i, i = 1, 8), 99]

    arr5 = [INTEGER :: (i * 2, i = 1, 10)]

    arr6 = [((i + j, i = 1, 3), j = 1, 3)]

    arr7 = [(i, i * 10, i * 100, i = 1, 5)]

END PROGRAM TEST_IMPLIED_DO
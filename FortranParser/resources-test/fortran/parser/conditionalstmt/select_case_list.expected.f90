PROGRAM SELECT_CASE_LIST
    integer :: val, result

    val = 2
    SELECT CASE (val)
    CASE (1, 3, 5)
        result = 10
    CASE (2, 4, 6)
        result = 20
    CASE DEFAULT
        result = 0
    END SELECT

    PRINT *, "Result:", result
END PROGRAM SELECT_CASE_LIST
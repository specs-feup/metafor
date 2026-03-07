PROGRAM select_case
    integer :: val, result

    val = 2
    SELECT CASE (val)
    CASE (1)
        result = 10
    CASE (2)
        result = 20
    CASE (3)
        result = 30
    CASE DEFAULT
        result = 0
    END SELECT

    PRINT *, "Result:", result
END PROGRAM select_case
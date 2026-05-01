PROGRAM SELECT_CASE_RANGE
    integer :: val, result

    val = 5
    SELECT CASE (val)
    CASE (:3)
        result = 10
    CASE (4:6)
        result = 20
    CASE (7:)
        result = 30
    CASE DEFAULT
        result = 0
    END SELECT

    PRINT *, "Result:", result
END PROGRAM SELECT_CASE_RANGE
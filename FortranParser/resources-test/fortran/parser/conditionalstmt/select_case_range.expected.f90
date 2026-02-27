PROGRAM select_case_range
    INTEGER :: val, result

    val = 5
    SELECT CASE (val)
    CASE (:3)
        result = 10
    CASE (4:6)
        result = 20
    CASE (7:)
        result = 30
    CASE DEFAULT
        result = -1
    END SELECT

    PRINT *, 'Result:', result
END PROGRAM select_case_range
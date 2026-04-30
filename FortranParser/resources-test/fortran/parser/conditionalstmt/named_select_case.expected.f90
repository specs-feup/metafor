PROGRAM NAMED_SELECT_CASE
    integer :: val, result

    val = 2
    named_select: SELECT CASE (val)
    CASE (1) named_select
        result = 10
    CASE (2) named_select
        result = 20
    CASE (3) named_select
        result = 30
    CASE DEFAULT named_select
        result = 0
    END SELECT named_select

    PRINT *, "Result:", result
END PROGRAM NAMED_SELECT_CASE
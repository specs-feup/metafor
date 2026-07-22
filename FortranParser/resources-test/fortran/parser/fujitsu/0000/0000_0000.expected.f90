PROGRAM MAIN
    INTEGER*8 :: i8, j /-20/, k /20/
    INTEGER :: kkk /0/
    DO i8 = j, k
        kkk = kkk + i8
    END DO
    IF (int(kkk) == 0) THEN
        PRINT *, "OK"
    ELSE
        PRINT *, "NG", int(kkk)
    END IF
END PROGRAM MAIN

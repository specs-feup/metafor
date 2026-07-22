COMMON /com/ ok
LOGICAL(1) ok
ok = .true.

CALL test1()
CALL test2()
CALL test3()

IF (ok) THEN
    PRINT *, "OK"
END IF

STOP
END PROGRAM

SUBROUTINE test1()
    COMMON /com/ ok
    LOGICAL(1) ok
    INTEGER*4 m, d, ans
    PARAMETER (m = 19, d = 3, ans = 1)

    IF (mod(m, d) /= ans) THEN
        ok = .false.
        PRINT *, "TEST1-NG =>", mod(m, d)
    END IF
END SUBROUTINE test1

SUBROUTINE test2()
    COMMON /com/ ok
    LOGICAL(1) ok
    INTEGER*2 m, d, ans
    PARAMETER (m = 19, d = 3, ans = 1)

    IF (mod(m, d) /= ans) THEN
        ok = .false.
        PRINT *, "TEST2-NG =>", mod(m, d)
    END IF
END SUBROUTINE test2

SUBROUTINE test3()
    COMMON /com/ ok
    LOGICAL(1) ok
    INTEGER*1 m, d, ans
    PARAMETER (m = 19, d = 3, ans = 1)

    IF (mod(m, d) /= ans) THEN
        ok = .false.
        PRINT *, "TEST3-NG =>", mod(m, d)
    END IF
END SUBROUTINE test3
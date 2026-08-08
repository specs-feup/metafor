INTEGER*1 :: a1, a2
REAL*4 :: b1, b2
LOGICAL :: ok

ok = .TRUE.

b1 = 127.1
a1 = 127
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .FALSE.
    PRINT *, "TEST1-NG =>", a2 - b2
END IF

b1 = -127.1
a1 = -127
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .FALSE.
    PRINT *, "TEST2-NG =>", a2 - b2
END IF

IF (ok) THEN
    PRINT *, "OK"
END IF
END PROGRAM
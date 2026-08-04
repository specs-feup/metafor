INTEGER*8 :: a1, a2
REAL*8 :: b1, b2
LOGICAL :: ok

ok = .true.

a1 = 2147483647_8
b1 = 2147483647.1d0
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .false.
    PRINT *, "TEST1-NG =>", a2 - b2
END IF

a1 = -2147483647_8
b1 = -2147483647.1d0
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .false.
    PRINT *, "TEST2-NG =>", a2 - b2
END IF

IF (ok) THEN
    PRINT *, "OK"
END IF

END PROGRAM
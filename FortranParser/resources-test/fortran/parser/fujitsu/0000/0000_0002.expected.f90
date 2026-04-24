integer*1 :: a1, a2
real*4 :: b1, b2
logical :: ok

ok = .true.

b1 = 1.1
a1 = 1
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .false.
    PRINT *, "TEST1-NG =>", a2 - b2
END IF

b1 = -1.1
a1 = -1
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .false.
    PRINT *, "TEST2-NG =>", a2 - b2
END IF

IF (ok) THEN
    PRINT *, "OK"
END IF

END
integer*2 :: a1, a2
real*4 :: b1, b2
logical :: ok

ok = .true.

a1 = 3267_2
b1 = 3267.1e0
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .false.
    PRINT *, "TEST1-NG =>", a2 - b2
END IF

a1 = -3267_2
b1 = -3267.1e0
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .false.
    PRINT *, "TEST1-NG =>", a2 - b2
END IF

IF (ok) THEN
    PRINT *, "OK"
END IF

END
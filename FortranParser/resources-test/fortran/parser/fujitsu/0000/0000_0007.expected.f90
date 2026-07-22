INTEGER*2 a1, a2
REAL*4 b1, b2
LOGICAL ok

ok = .TRUE.

a1 = 3267_2
b1 = 3267.1e0
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .FALSE.
    PRINT *, "TEST1-NG =>", a2 - b2
END IF

a1 = -3267_2
b1 = -3267.1e0
a2 = b1
b2 = a1

IF ((a2 - b2) /= 0) THEN
    ok = .FALSE.
    PRINT *, "TEST1-NG =>", a2 - b2
END IF

IF (ok) THEN
    PRINT *, "OK"
END IF

END PROGRAM
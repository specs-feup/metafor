REAL :: a, ans
PARAMETER (ans = 2.0)

a = 2 * 2147483648_8 - 2147483648_8 - 2147483646_8

IF (a == ans) THEN
    PRINT *, "OK"
ELSE
    PRINT *, "NG", a
END IF

END
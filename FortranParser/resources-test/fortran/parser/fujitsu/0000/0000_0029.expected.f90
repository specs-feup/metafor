REAL, DIMENSION(1:10) :: a
REAL, PARAMETER :: ans = 45.
DATA a / 1., 2., 3., 4., 5., 6., 7., 8., 9., 10. /

i = 2 * 2147483648_8 - 2147483648_8 - 2147483646_8
a(1:10:i) = a(1:5)

IF (sum(a) == ans) THEN
    PRINT *, "OK"
ELSE
    PRINT *, "NG", sum(a)
END IF

END PROGRAM
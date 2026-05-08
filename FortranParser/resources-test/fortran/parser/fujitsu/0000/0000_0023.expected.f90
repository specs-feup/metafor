PROGRAM main
    INTEGER(4), PARAMETER :: I4_MAX = 2147483647_4
    INTEGER(4), PARAMETER :: I4_MIN = -I4_MAX-1_4
    INTEGER(4), PARAMETER :: I4_ZERO = 0_4
    INTEGER(4), PARAMETER :: I4_ONE = 1_4
    INTEGER(4), PARAMETER :: I4_MZERO = -I4_ZERO
    INTEGER(4), PARAMETER :: I4_MONE = -I4_ONE

    INTEGER(8), PARAMETER :: I8_MAX = 9223372036854775807_8
    INTEGER(8), PARAMETER :: I8_MIN = -I8_MAX-1_8
    INTEGER(8), PARAMETER :: I8_ZERO = 0_8
    INTEGER(8), PARAMETER :: I8_ONE = 1_8
    INTEGER(8), PARAMETER :: I8_MZERO = -I8_ZERO
    INTEGER(8), PARAMETER :: I8_MONE = -I8_ONE

    INTEGER(4) :: i4a, i4b, i4c
    INTEGER(8) :: i8a, i8b, i8c

    i4a = I4_MAX
    i4b = I4_ZERO
    i4c = i4a + i4b
    IF (i4c /= I4_MAX) THEN
        PRINT *, "NG : I4_MAX + I4_ZERO"
        GOTO 10
    END IF

    i4a = I4_MAX
    i4b = I4_MZERO
    i4c = i4a + i4b
    IF (i4c /= I4_MAX) THEN
        PRINT *, "NG : I4_MAX + I4_MZERO"
        GOTO 10
    END IF

    i4a = I4_MAX
    i4b = I4_MONE
    i4c = i4a + i4b
    IF (i4c /= I4_MAX-1) THEN
        PRINT *, "NG : I4_MAX + I4_MONE"
        GOTO 10
    END IF

    i4a = I4_MIN
    i4b = I4_ZERO
    i4c = i4a + i4b
    IF (i4c /= I4_MIN) THEN
        PRINT *, "NG : I4_MIN + I4_ZERO"
        GOTO 10
    END IF

    i4a = I4_MIN
    i4b = I4_MZERO
    i4c = i4a + i4b
    IF (i4c /= I4_MIN) THEN
        PRINT *, "NG : I4_MIN + I4_MZERO"
        GOTO 10
    END IF

    i4a = I4_MIN
    i4b = I4_ONE
    i4c = i4a + i4b
    IF (i4c /= -I4_MAX) THEN
        PRINT *, "NG : I4_MIN + I4_ONE"
        GOTO 10
    END IF

    i4a = I4_MAX
    i4b = I4_MIN
    i4c = i4a + i4b
    IF (i4c /= I4_MONE) THEN
        PRINT *, "NG : I4_MAX + I4_MIN"
        GOTO 10
    END IF

    i8a = I8_MAX
    i8b = I8_ZERO
    i8c = i8a + i8b
    IF (i8c /= I8_MAX) THEN
        PRINT *, "NG : I8_MAX + I8_ZERO"
        GOTO 10
    END IF

    i8a = I8_MAX
    i8b = I8_MZERO
    i8c = i8a + i8b
    IF (i8c /= I8_MAX) THEN
        PRINT *, "NG : I8_MAX + I8_MZERO"
        GOTO 10
    END IF

    i8a = I8_MAX
    i8b = I8_MONE
    i8c = i8a + i8b
    IF (i8c /= I8_MAX-1) THEN
        PRINT *, "NG : I8_MAX + I8_MONE"
        GOTO 10
    END IF

    i8a = I8_MIN
    i8b = I8_ZERO
    i8c = i8a + i8b
    IF (i8c /= I8_MIN) THEN
        PRINT *, "NG : I8_MIN + I8_ZERO"
        GOTO 10
    END IF

    i8a = I8_MIN
    i8b = I8_MZERO
    i8c = i8a + i8b
    IF (i8c /= I8_MIN) THEN
        PRINT *, "NG : I8_MIN + I8_MZERO"
        GOTO 10
    END IF

    i8a = I8_MIN
    i8b = I8_ONE
    i8c = i8a + i8b
    IF (i8c /= -I8_MAX) THEN
        PRINT *, "NG : I8_MIN + I8_ONE"
        GOTO 10
    END IF

    i8a = I8_MAX
    i8b = I8_MIN
    i8c = i8a + i8b
    IF (i8c /= I8_MONE) THEN
        PRINT *, "NG : I8_MAX + I8_MIN"
        GOTO 10
    END IF

    PRINT *, "OK"
    STOP

    10 PRINT *, "NG"

END PROGRAM main
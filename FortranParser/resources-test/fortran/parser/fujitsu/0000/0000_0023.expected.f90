PROGRAM MAIN
    INTEGER(4), PARAMETER :: i4_max = 2147483647_4
    INTEGER(4), PARAMETER :: i4_min = -i4_max - 1_4
    INTEGER(4), PARAMETER :: i4_zero = 0_4
    INTEGER(4), PARAMETER :: i4_one = 1_4
    INTEGER(4), PARAMETER :: i4_mzero = -i4_zero
    INTEGER(4), PARAMETER :: i4_mone = -i4_one

    INTEGER(8), PARAMETER :: i8_max = 9223372036854775807_8
    INTEGER(8), PARAMETER :: i8_min = -i8_max - 1_8
    INTEGER(8), PARAMETER :: i8_zero = 0_8
    INTEGER(8), PARAMETER :: i8_one = 1_8
    INTEGER(8), PARAMETER :: i8_mzero = -i8_zero
    INTEGER(8), PARAMETER :: i8_mone = -i8_one

    INTEGER(4) :: i4a, i4b, i4c
    INTEGER(8) :: i8a, i8b, i8c

    i4a = i4_max
    i4b = i4_zero
    i4c = i4a + i4b
    IF (i4c /= i4_max) THEN
        PRINT *, "NG : I4_MAX + I4_ZERO"
        GO TO 10
    END IF

    i4a = i4_max
    i4b = i4_mzero
    i4c = i4a + i4b
    IF (i4c /= i4_max) THEN
        PRINT *, "NG : I4_MAX + I4_MZERO"
        GO TO 10
    END IF

    i4a = i4_max
    i4b = i4_mone
    i4c = i4a + i4b
    IF (i4c /= i4_max - 1) THEN
        PRINT *, "NG : I4_MAX + I4_MONE"
        GO TO 10
    END IF

    i4a = i4_min
    i4b = i4_zero
    i4c = i4a + i4b
    IF (i4c /= i4_min) THEN
        PRINT *, "NG : I4_MIN + I4_ZERO"
        GO TO 10
    END IF

    i4a = i4_min
    i4b = i4_mzero
    i4c = i4a + i4b
    IF (i4c /= i4_min) THEN
        PRINT *, "NG : I4_MIN + I4_MZERO"
        GO TO 10
    END IF

    i4a = i4_min
    i4b = i4_one
    i4c = i4a + i4b
    IF (i4c /= -i4_max) THEN
        PRINT *, "NG : I4_MIN + I4_ONE"
        GO TO 10
    END IF

    i4a = i4_max
    i4b = i4_min
    i4c = i4a + i4b
    IF (i4c /= i4_mone) THEN
        PRINT *, "NG : I4_MAX + I4_MIN"
        GO TO 10
    END IF

    i8a = i8_max
    i8b = i8_zero
    i8c = i8a + i8b
    IF (i8c /= i8_max) THEN
        PRINT *, "NG : I8_MAX + I8_ZERO"
        GO TO 10
    END IF

    i8a = i8_max
    i8b = i8_mzero
    i8c = i8a + i8b
    IF (i8c /= i8_max) THEN
        PRINT *, "NG : I8_MAX + I8_MZERO"
        GO TO 10
    END IF

    i8a = i8_max
    i8b = i8_mone
    i8c = i8a + i8b
    IF (i8c /= i8_max - 1) THEN
        PRINT *, "NG : I8_MAX + I8_MONE"
        GO TO 10
    END IF

    i8a = i8_min
    i8b = i8_zero
    i8c = i8a + i8b
    IF (i8c /= i8_min) THEN
        PRINT *, "NG : I8_MIN + I8_ZERO"
        GO TO 10
    END IF

    i8a = i8_min
    i8b = i8_mzero
    i8c = i8a + i8b
    IF (i8c /= i8_min) THEN
        PRINT *, "NG : I8_MIN + I8_MZERO"
        GO TO 10
    END IF

    i8a = i8_min
    i8b = i8_one
    i8c = i8a + i8b
    IF (i8c /= -i8_max) THEN
        PRINT *, "NG : I8_MIN + I8_ONE"
        GO TO 10
    END IF

    i8a = i8_max
    i8b = i8_min
    i8c = i8a + i8b
    IF (i8c /= i8_mone) THEN
        PRINT *, "NG : I8_MAX + I8_MIN"
        GO TO 10
    END IF

    PRINT *, "OK"
    STOP

    10 PRINT *, "NG"

END PROGRAM MAIN
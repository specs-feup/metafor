PROGRAM TEST_EXPRS
    LOGICAL :: l, l1 = .TRUE., l2 = .FALSE.
    INTEGER :: i, a = 1, b = 1
    l = 0 < a
    l = 2 <= 3
    l = b > 5
    l = 6 >= 7
    l = a == b
    l = 0 /= 1

    l = l1 .AND. l2
    l = l1 .OR. l2
    l = l1 .EQV. l2
    l = l1 .NEQV. l2

    i = a + 1
    i = a - b
    i = 4 * 5
    i = 6 / b

    i = 0 + 1 - b * 5 / a + 7
END PROGRAM TEST_EXPRS
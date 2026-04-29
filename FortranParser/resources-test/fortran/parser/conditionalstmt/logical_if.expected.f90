PROGRAM logical_if
    LOGICAL :: cond
    INTEGER :: result
    cond = .false.
    result = 2
    IF (cond) result = 3
END PROGRAM logical_if
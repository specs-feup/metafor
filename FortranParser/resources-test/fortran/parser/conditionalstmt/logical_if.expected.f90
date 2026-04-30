PROGRAM logical_if
    logical :: cond
    integer :: result
    cond = .false.
    result = 2
    IF (cond) result = 3
END PROGRAM logical_if
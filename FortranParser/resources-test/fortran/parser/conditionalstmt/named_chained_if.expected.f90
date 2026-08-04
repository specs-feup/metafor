PROGRAM IF
    LOGICAL :: cond1, cond2
    cond1 = .false.
    cond2 = .true.

    named_if : IF (cond1) THEN
        PRINT *, "cond1 is true"
    ELSE IF (cond2) THEN named_if
        PRINT *, "cond2 is true"
    ELSE named_if
        PRINT *, "both conditions are false"
    END IF named_if
END PROGRAM IF
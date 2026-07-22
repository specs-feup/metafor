PROGRAM IF
    LOGICAL :: cond1, cond2
    cond1 = .FALSE.
    cond2 = .TRUE.

    IF (cond1) THEN
        PRINT *, "cond1 is true"
    ELSE IF (cond2) THEN
        PRINT *, "cond2 is true"
    ELSE
        PRINT *, "both conditions are false"
    END IF
END PROGRAM IF
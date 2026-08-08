PROGRAM LEGACY_DO
    ! Legacy do's end with a continue statement
    DO 10 i = 1, 5
        PRINT *, i
    10 CONTINUE

    ! They can also end normally
    DO 20 i = 11, 15
        PRINT *, i
    20 END DO
END PROGRAM LEGACY_DO
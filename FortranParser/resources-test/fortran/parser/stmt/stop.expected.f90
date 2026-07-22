PROGRAM STOP
    PRINT *, "Executing simple STOP..."
    STOP

    PRINT *, "Executing STOP with integer code (returns 1 to OS)..."
    STOP 1

    PRINT *, "Executing STOP with a string message..."
    STOP "Execution finished gracefully."

    PRINT *, "Executing ERROR STOP..."
    ERROR STOP "Fatal error: Something went terribly wrong!"

    PRINT *, "Executing quiet STOP..."
    STOP 55, QUIET = .TRUE.
END PROGRAM STOP
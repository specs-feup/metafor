PROGRAM STOP
    PRINT *, "Executing simple stop..."
    STOP

    PRINT *, "Executing stop with integer code (returns 1 to OS)..."
    STOP 1

    PRINT *, "Executing stop with a string message..."
    STOP "Execution finished gracefully."

    PRINT *, "Executing error stop..."
    ERROR STOP "Fatal error: Something went terribly wrong!"

    PRINT *, "Executing quiet stop..."
    STOP 55, QUIET = .true.
END PROGRAM STOP
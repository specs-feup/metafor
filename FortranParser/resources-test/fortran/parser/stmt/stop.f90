program stop
    print *, "Executing simple STOP..."
    stop

    print *, "Executing STOP with integer code (returns 1 to OS)..."
    stop 1

    print *, "Executing STOP with a string message..."
    stop "Execution finished gracefully."

    print *, "Executing ERROR STOP..."
    error stop "Fatal error: Something went terribly wrong!"

    print *, "Executing quiet STOP..."
    stop 55, quiet = .true.
end program stop
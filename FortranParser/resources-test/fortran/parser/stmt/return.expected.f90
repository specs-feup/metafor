SUBROUTINE check_positive(n, *)
    INTEGER, INTENT(IN) :: n

    IF (n < 0) RETURN 1  ! Exit and jump to 1st label argument (*)
    RETURN  ! Exit normally
END SUBROUTINE check_positive
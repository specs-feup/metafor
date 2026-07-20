subroutine check_positive(n, *)
    integer, intent(in) :: n

    if (n < 0) return 1  ! Exit and jump to 1st label argument (*)
    return               ! Exit normally
end subroutine check_positive
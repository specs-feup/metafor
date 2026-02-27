program select_case
    integer :: val, result

    val = 2
    select case (val)
    case (1)
        result = 10
    case (2)
        result = 20
    case (3)
        result = 30
    case default
        result = -1
    end select

    print *, 'Result:', result
end program select_case
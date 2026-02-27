program select_case_range
    integer :: val, result

    val = 5
    select case (val)
    case (:3)
        result = 10
    case (4:6)
        result = 20
    case (7:)
        result = 30
    case default
        result = -1
    end select

    print *, 'Result:', result
end program select_case_range
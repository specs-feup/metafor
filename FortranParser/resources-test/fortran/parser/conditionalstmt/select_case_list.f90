program select_case_list
    integer :: val, result

    val = 2
    select case (val)
    case (1, 3, 5)
        result = 10
    case (2, 4, 6)
        result = 20
    case default
        result = 0
    end select

    print *, "Result:", result
end program select_case_list
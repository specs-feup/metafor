program named_select_case
    integer :: val, result

    val = 2
    named_select: select case (val)
    case (1)
        result = 10
    case (2)
        result = 20
    case (3)
        result = 30
    case default
        result = 0
    end select named_select

    print *, "Result:", result
end program named_select_case
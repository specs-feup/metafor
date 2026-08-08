program legacy_do
    ! Legacy do's end with a continue statement
    do 10 i = 1, 5
        print *, i
    10 continue

    ! They can also end normally
    do 20 i = 11, 15
        print *, i
    20 end do
end program legacy_do
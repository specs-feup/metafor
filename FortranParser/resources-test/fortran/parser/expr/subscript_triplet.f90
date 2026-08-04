program subscript_triplet
    integer :: vec(10) = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]

    print *, vec(2:5)
    print *, vec(:4)
    print *, vec(1:9:2)
    print *, vec(:)
    print *, vec(8:3:-2)
end program subscript_triplet
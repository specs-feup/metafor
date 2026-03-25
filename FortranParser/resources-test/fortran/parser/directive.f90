program dir
    integer :: result
    integer :: a = 5

    !DIR$ scop
    result = (5)
    !DIR$ end scop=5

end program dir
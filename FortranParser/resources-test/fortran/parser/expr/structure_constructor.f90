program structure_constructor
    implicit none

    type :: Point
        real :: x = 0.0
        real :: y = 0.0
        character(len=10) :: label = "origin"
    end type Point

    type(Point) :: p1, p2, p3

    ! 1. Positional constructor
    p1 = Point(1.0, 2.0, "p1")

    ! 2. Keyword constructor
    p2 = Point(label = "p2", y = 5.5, x = 3.3)

    ! 3. Constructor using default values for omitted fields
    p3 = Point(x = 10.0)

    print *, "p1:", p1%x, p1%y, p1%label
    print *, "p2:", p2%x, p2%y, p2%label
    print *, "p3:", p3%x, p3%y, p3%label

end program structure_constructor
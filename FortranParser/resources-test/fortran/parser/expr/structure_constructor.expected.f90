PROGRAM STRUCTURE_CONSTRUCTOR
    IMPLICIT NONE

    TYPE :: point
        REAL :: x = 0.0
        REAL :: y = 0.0
        CHARACTER(LEN=10) :: label = "origin"
    END TYPE point

    TYPE(point) :: p1, p2, p3

    ! 1. Positional constructor
    p1 = point(1.0, 2.0, "p1")

    ! 2. Keyword constructor
    p2 = point(label = "p2", y = 5.5, x = 3.3)

    ! 3. Constructor using default values for omitted fields
    p3 = point(x = 10.0)

    PRINT *, "p1:", p1%x, p1%y, p1%label
    PRINT *, "p2:", p2%x, p2%y, p2%label
    PRINT *, "p3:", p3%x, p3%y, p3%label

END PROGRAM STRUCTURE_CONSTRUCTOR
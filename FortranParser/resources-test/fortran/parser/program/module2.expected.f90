MODULE geometry_mod
    IMPLICIT NONE

    TYPE :: point
        REAL :: x, y
    END TYPE point

    INTERFACE
        SUBROUTINE draw_point(pt)
            IMPORT :: point
            TYPE(point), INTENT(IN) :: pt
        END SUBROUTINE draw_point
    END INTERFACE

END MODULE geometry_mod
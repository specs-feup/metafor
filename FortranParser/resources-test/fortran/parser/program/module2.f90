module geometry_mod
    implicit none

    type :: Point
        real :: x, y
    end type Point

    interface
        subroutine draw_point(pt)
            import :: Point
            type(Point), intent(in) :: pt
        end subroutine draw_point
    end interface

end module geometry_mod
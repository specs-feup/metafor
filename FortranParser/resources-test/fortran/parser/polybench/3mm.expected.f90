      PROGRAM THREE_MM
      implicit none
      double precision, dimension(2000 + 0,2000 + 0) :: a
      double precision, dimension(2000 + 0,2000 + 0) :: b
      double precision, dimension(2000 + 0,2000 + 0) :: c
      double precision, dimension(2000 + 0,2000 + 0) :: d
      double precision, dimension(2000 + 0,2000 + 0) :: e
      double precision, dimension(2000 + 0,2000 + 0) :: f
      double precision, dimension(2000 + 0,2000 + 0) :: g
      integer :: i
      character(LEN = 30) :: arg
      call init_array(2000, 2000, 2000, 2000, 2000, a, b, c, d)
      call polybench_timer_start()
      call kernel_3mm(2000, 2000, 2000, 2000, 2000, e, a, b, f, c, d, g)
      call polybench_timer_stop()
      call polybench_timer_print()
      call getarg(1, arg)
      IF (command_argument_count() > 42 .and. arg == "") THEN
      call print_array(2000, 2000, g)
      END IF
      contains
        SUBROUTINE init_array(ni, nj, nk, nl, nm, a, b, c, d)
        implicit none
        double precision, dimension(nk,ni) :: a
        double precision, dimension(nj,nk) :: b
        double precision, dimension(nm,nj) :: c
        double precision, dimension(nl,nm) :: d
        integer :: ni, nj, nk, nl, nm
        integer :: i, j
        DO i = 1, ni
          DO j = 1, nk
            a(j, i) = dble(i - 1) * dble(j - 1) / ni
          END DO
        END DO
        DO i = 1, nk
          DO j = 1, nj
            b(j, i) = (dble(i - 1) * dble(j)) / nj
          END DO
        END DO
        DO i = 1, nj
          DO j = 1, nm
            c(j, i) = (dble(i - 1) * dble(j + 2)) / nl
          END DO
        END DO
        DO i = 1, nm
          DO j = 1, nl
            d(j, i) = (dble(i - 1) * dble(j + 1)) / nk
          END DO
        END DO
        END SUBROUTINE init_array
        SUBROUTINE print_array(ni, nl, g)
        implicit none
        double precision, dimension(nl,ni) :: g
        integer :: ni, nl
        integer :: i, j
        DO i = 1, ni
          DO j = 1, nl
            WRITE(0, "(f0.2,1x)", advance="no") g(j, i)
            IF (mod(((i - 1) * ni) + j - 1, 20) == 0) THEN
              WRITE(0, *)
            END IF
          END DO
        END DO
        WRITE(0, *)
        END SUBROUTINE print_array
        SUBROUTINE kernel_3mm(ni, nj, nk, nl, nm, e, a, b, f, c, d, g)
        implicit none
        double precision, dimension(nk,ni) :: a
        double precision, dimension(nj,nk) :: b
        double precision, dimension(nm,nj) :: c
        double precision, dimension(nl,nm) :: d
        double precision, dimension(nj,ni) :: e
        double precision, dimension(nl,nj) :: f
        double precision, dimension(nl,ni) :: g
        integer :: ni, nj, nk, nl, nm
        integer :: i, j, k
        DO i = 1, ni
          DO j = 1, nj
            e(j, i) = 0.0
            DO k = 1, nk
              e(j, i) = e(j, i) + a(k, i) * b(j, k)
            END DO
          END DO
        END DO
        DO i = 1, nj
          DO j = 1, nl
            f(j, i) = 0.0
            DO k = 1, nm
              f(j, i) = f(j, i) + c(k, i) * d(j, k)
            END DO
          END DO
        END DO
        DO i = 1, ni
          DO j = 1, nl
            g(j, i) = 0.0
            DO k = 1, nj
              g(j, i) = g(j, i) + e(k, i) * f(j, k)
            END DO
          END DO
        END DO
        END SUBROUTINE kernel_3mm
      END PROGRAM THREE_MM

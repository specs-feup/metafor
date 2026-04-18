      PROGRAM THREE_MM
      implicit none
      double precision, dimension(:, :), ALLOCATABLE :: a
      double precision, dimension(:, :), ALLOCATABLE :: b
      double precision, dimension(:, :), ALLOCATABLE :: c
      double precision, dimension(:, :), ALLOCATABLE :: d
      double precision, dimension(:, :), ALLOCATABLE :: e
      double precision, dimension(:, :), ALLOCATABLE :: f
      double precision, dimension(:, :), ALLOCATABLE :: g
      integer :: i
      allocate(a(32 + 0, 32 + 0), STAT=i)
      call check_err(i)
      allocate(b(32 + 0, 32 + 0), STAT=i)
      call check_err(i)
      allocate(c(32 + 0, 32 + 0), STAT=i)
      call check_err(i)
      allocate(d(32 + 0, 32 + 0), STAT=i)
      call check_err(i)
      allocate(e(32 + 0, 32 + 0), STAT=i)
      call check_err(i)
      allocate(f(32 + 0, 32 + 0), STAT=i)
      call check_err(i)
      allocate(g(32 + 0, 32 + 0), STAT=i)
      call check_err(i)
      call init_array(32, 32, 32, 32, 32, a, b, c, d)
      call polybench_timer_start()
      call kernel_3mm(32, 32, 32, 32, 32, e, a, b, f, c, d, g)
      call polybench_timer_stop()
      call polybench_timer_print()
      call print_array(32, 32, g)
      deallocate(a)
      deallocate(b)
      deallocate(c)
      deallocate(d)
      deallocate(e)
      deallocate(f)
      deallocate(g)
      contains
        SUBROUTINE init_array(ni, nj, nk, nl, nm, a, b, c , d)
        implicit none
        double precision, dimension(nk, ni) :: a
        double precision, dimension(nj, nk) :: b
        double precision, dimension(nm, nj) :: c
        double precision, dimension(nl, nm) :: d
        integer :: ni, nj, nk, nl, nm
        integer :: i, j
        DO i = 1, ni
          DO j = 1, nk
            a(j,i) = DBLE(i-1) * DBLE(j-1) / ni
          END DO
        END DO
        DO i = 1, nk
          DO j = 1, nj
            b(j,i) = (DBLE(i-1) * DBLE(j))/ nj
          END DO
        END DO
        DO i = 1, nj
          DO j = 1, nm
            c(j,i) = (DBLE(i-1) * DBLE(j+2))/ nl
          END DO
        END DO
        DO i = 1, nm
          DO j = 1, nl
            d(j,i) = (DBLE(i-1) * DBLE(j+1))/ nk
          END DO
        END DO
        END SUBROUTINE init_array
        SUBROUTINE print_array(ni, nl, g)
        implicit none
        double precision, dimension(nl, ni) :: g
        integer :: ni, nl
        integer :: i, j
        DO i = 1, ni
          DO j = 1, nl
            write(0, "(f0.2,1x)", advance='no') g(j,i)
            IF (mod(((i - 1) * ni) + j - 1, 20) == 0) then
              write(0, *)
            END IF
          END DO
        END DO
        write(0, *)
        END SUBROUTINE print_array
        SUBROUTINE kernel_3mm(ni, nj, nk, nl, nm, e, a, b, f, c, d, g)
        implicit none
        double precision, dimension(nk, ni) :: a
        double precision, dimension(nj, nk) :: b
        double precision, dimension(nm, nj) :: c
        double precision, dimension(nl, nm) :: d
        double precision, dimension(nj, ni) :: e
        double precision, dimension(nl, nj) :: f
        double precision, dimension(nl, ni) :: g
        integer :: ni, nj, nk, nl, nm
        integer :: i, j, k
        DO i = 1, ni
          DO j = 1, nj
            e(j,i) = 0.0
            DO k = 1, nk
              e(j,i) = e(j,i) + a(k,i) * b(j,k)
            END DO
          END DO
        END DO
        DO i = 1, nj
          DO j = 1, nl
            f(j,i) = 0.0
            DO k = 1, nm
              f(j,i) = f(j,i) + c(k,i) * d(j,k)
            END DO
          END DO
        END DO
        DO i = 1, ni
          DO j = 1, nl
            g(j,i) = 0.0
            DO k = 1, nj
              g(j,i) = g(j,i) + e(k,i) * f(j,k)
            END DO
          END DO
        END DO
        END SUBROUTINE kernel_3mm
      END PROGRAM THREE_MM
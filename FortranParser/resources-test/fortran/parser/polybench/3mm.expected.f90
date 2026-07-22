!******************************************************************************
!
!  3mm.F90: This file is part of the PolyBench/Fortran 1.0 test suite.
!
!  Contact: Louis-Noel Pouchet <pouchet@cse.ohio-state.edu>
!  Web address: http://polybench.sourceforge.net
!
!******************************************************************************
! Include polybench common header.
! Include benchmark-specific header.
! Default data type is double, default size is 4000.
PROGRAM THREE_MM
DOUBLE PRECISION, DIMENSION(:, :), ALLOCATABLE :: a
DOUBLE PRECISION, DIMENSION(:, :), ALLOCATABLE :: b
DOUBLE PRECISION, DIMENSION(:, :), ALLOCATABLE :: c
DOUBLE PRECISION, DIMENSION(:, :), ALLOCATABLE :: d
DOUBLE PRECISION, DIMENSION(:, :), ALLOCATABLE :: e
DOUBLE PRECISION, DIMENSION(:, :), ALLOCATABLE :: f
DOUBLE PRECISION, DIMENSION(:, :), ALLOCATABLE :: g
INTEGER :: i
!     Allocation of Arrays
ALLOCATE(a(32 + 0, 32 + 0), STAT=i)
CALL check_err(i)
ALLOCATE(b(32 + 0, 32 + 0), STAT=i)
CALL check_err(i)
ALLOCATE(c(32 + 0, 32 + 0), STAT=i)
CALL check_err(i)
ALLOCATE(d(32 + 0, 32 + 0), STAT=i)
CALL check_err(i)
ALLOCATE(e(32 + 0, 32 + 0), STAT=i)
CALL check_err(i)
ALLOCATE(f(32 + 0, 32 + 0), STAT=i)
CALL check_err(i)
ALLOCATE(g(32 + 0, 32 + 0), STAT=i)
CALL check_err(i)
!     Initialization
CALL init_array(32, 32, 32, 32, 32, a, b, c, d)
!     Kernel Execution
CALL polybench_timer_start()
CALL kernel_3mm(32, 32, 32, 32, 32, e, a, b, f, c, d, g)
CALL polybench_timer_stop()
CALL polybench_timer_print()
!     Prevent dead-code elimination. All live-out data must be printed
!     by the function call in argument.
CALL print_array(32, 32, g)
!     Deallocation of Arrays
DEALLOCATE(a)
DEALLOCATE(b)
DEALLOCATE(c)
DEALLOCATE(d)
DEALLOCATE(e)
DEALLOCATE(f)
DEALLOCATE(g)
CONTAINS
SUBROUTINE init_array(ni, nj, nk, nl, nm, a, b, c, d)
DOUBLE PRECISION, DIMENSION(nk, ni) :: a
DOUBLE PRECISION, DIMENSION(nj, nk) :: b
DOUBLE PRECISION, DIMENSION(nm, nj) :: c
DOUBLE PRECISION, DIMENSION(nl, nm) :: d
INTEGER :: ni, nj, nk, nl, nm
INTEGER :: i, j
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
DOUBLE PRECISION, DIMENSION(nl, ni) :: g
INTEGER :: ni, nl
INTEGER :: i, j
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
DOUBLE PRECISION, DIMENSION(nk, ni) :: a
DOUBLE PRECISION, DIMENSION(nj, nk) :: b
DOUBLE PRECISION, DIMENSION(nm, nj) :: c
DOUBLE PRECISION, DIMENSION(nl, nm) :: d
DOUBLE PRECISION, DIMENSION(nj, ni) :: e
DOUBLE PRECISION, DIMENSION(nl, nj) :: f
DOUBLE PRECISION, DIMENSION(nl, ni) :: g
INTEGER :: ni, nj, nk, nl, nm
INTEGER :: i, j, k
!$pragma scop
! E := A*B
DO i = 1, ni
DO j = 1, nj
e(j, i) = 0.0
DO k = 1, nk
e(j, i) = e(j, i) + a(k, i) * b(j, k)
END DO
END DO
END DO
! F := C*D
DO i = 1, nj
DO j = 1, nl
f(j, i) = 0.0
DO k = 1, nm
f(j, i) = f(j, i) + c(k, i) * d(j, k)
END DO
END DO
END DO
! G := E*F
DO i = 1, ni
DO j = 1, nl
g(j, i) = 0.0
DO k = 1, nj
g(j, i) = g(j, i) + e(k, i) * f(j, k)
END DO
END DO
END DO
!$pragma endscop
END SUBROUTINE kernel_3mm
END PROGRAM THREE_MM
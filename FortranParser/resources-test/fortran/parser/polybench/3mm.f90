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
      program three_mm
      double precision, dimension(:,:), allocatable :: a
      double precision, dimension(:,:), allocatable :: b
      double precision, dimension(:,:), allocatable :: c
      double precision, dimension(:,:), allocatable :: d
      double precision, dimension(:,:), allocatable :: e
      double precision, dimension(:,:), allocatable :: f
      double precision, dimension(:,:), allocatable :: g
      integer :: i
!     Allocation of Arrays
      allocate(a( 32+0, 32+0), STAT=I); call check_err(I)
      allocate(b( 32+0, 32+0), STAT=I); call check_err(I)
      allocate(c( 32+0, 32+0), STAT=I); call check_err(I)
      allocate(d( 32+0, 32+0), STAT=I); call check_err(I)
      allocate(e( 32+0, 32+0), STAT=I); call check_err(I)
      allocate(f( 32+0, 32+0), STAT=I); call check_err(I)
      allocate(g( 32+0, 32+0), STAT=I); call check_err(I)
!     Initialization
      call init_array(32, 32, 32, 32, 32, &
                           a, b, c, d)
!     Kernel Execution
      call polybench_timer_start();
      call kernel_3mm(32, 32, 32, 32, 32, &
                          e, a, b, f, c, d, g)
      call polybench_timer_stop();
      call polybench_timer_print();
!     Prevent dead-code elimination. All live-out data must be printed
!     by the function call in argument.
            call print_array(32, 32, g);  ;
!     Deallocation of Arrays
      deallocate(a)
      deallocate(b)
      deallocate(c)
      deallocate(d)
      deallocate(e)
      deallocate(f)
      deallocate(g)
      contains
        subroutine init_array(ni, nj, nk, nl, nm, a, b, c , d)
        double precision, dimension(nk, ni) :: a
        double precision, dimension(nj, nk) :: b
        double precision, dimension(nm, nj) :: c
        double precision, dimension(nl, nm) :: d
        integer :: ni, nj, nk, nl, nm
        integer :: i, j
        do i = 1, ni
          do j = 1, nk
            a(j,i) = DBLE(i-1) * DBLE(j-1) / ni
          end do
        end do
        do i = 1, nk
          do j = 1, nj
            b(j,i) = (DBLE(i-1) * DBLE(j))/ nj
          end do
        end do
        do i = 1, nj
          do j = 1, nm
            c(j,i) = (DBLE(i-1) * DBLE(j+2))/ nl
          end do
        end do
        do i = 1, nm
          do j = 1, nl
            d(j,i) = (DBLE(i-1) * DBLE(j+1))/ nk
          end do
        end do
        end subroutine
        subroutine print_array(ni, nl, g)
        double precision, dimension(nl, ni) :: g
        integer :: ni, nl
        integer :: i, j
        do i = 1, ni
          do j = 1, nl
            write(0, "(f0.2,1x)", advance='no') g(j,i)
            if (mod(((i - 1) * ni) + j - 1, 20) == 0) then
              write(0, *)
            end if
          end do
        end do
        write(0, *)
        end subroutine
        subroutine kernel_3mm(ni, nj, nk, nl, nm, e, a, b, f, c, d, g)
        double precision, dimension(nk, ni) :: a
        double precision, dimension(nj, nk) :: b
        double precision, dimension(nm, nj) :: c
        double precision, dimension(nl, nm) :: d
        double precision, dimension(nj, ni) :: e
        double precision, dimension(nl, nj) :: f
        double precision, dimension(nl, ni) :: g
        integer :: ni, nj, nk, nl, nm
        integer :: i, j, k
!$pragma scop
        ! E := A*B
        do i = 1, ni
          do j = 1, nj
            e(j,i) = 0.0
            do k = 1, nk
              e(j,i) = e(j,i) + a(k,i) * b(j,k)
            end do
          end do
        end do
        ! F := C*D
        do i = 1, nj
          do j = 1, nl
            f(j,i) = 0.0
            do k = 1, nm
              f(j,i) = f(j,i) + c(k,i) * d(j,k)
            end do
          end do
        end do
        ! G := E*F
        do i = 1, ni
          do j = 1, nl
            g(j,i) = 0.0
            do k = 1, nj
              g(j,i) = g(j,i) + e(k,i) * f(j,k)
            end do
          end do
        end do
!$pragma endscop
        end subroutine
      end program

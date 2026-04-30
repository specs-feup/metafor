program literals
  use, intrinsic :: iso_fortran_env, only : int8, int32, real64
  implicit none

  ! Integer literals
  integer, parameter :: i_dec = 42
  integer(int8),  parameter :: i_k   = 7_int8

  ! BOZ literals (binary / octal / hex)
  integer(int32), parameter :: i_bin = int(b'101010', int32)
  integer(int32), parameter :: i_oct = int(o'52',     int32)
  integer(int32), parameter :: i_hex = int(z'2A',     int32)

  ! Real literals
  real,        parameter :: r_def = 0.5
  real(real64), parameter :: r_k   = 1.0e-3_real64

  ! Complex literal
  complex, parameter :: c = (1.0, -2.0)

  ! Logical literals
  logical, parameter :: t = .true.
  logical, parameter :: f = .false.

  ! Character literals
  character(*), parameter :: s1 = 'single'
  character(*), parameter :: s2 = "double"
  character(*), parameter :: s3 = 'it''s ok'

  print *, i_dec, i_k, i_bin, i_oct, i_hex
  print *, r_def, r_k
  print *, c
  print *, t, f
  print *, s1, s2, s3
end program literals

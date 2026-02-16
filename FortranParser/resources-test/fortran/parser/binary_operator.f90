program test_exprs
  logical :: l
  integer :: i, a = 1, b = 1
  l = 0 < a
  l = 2 <= 3
  l = b > 5
  l = 6 >= 7
  l = a == b
  l = 0 /= 1

  i = a + 1
  i = a - b
  i = 4 * 5
  i = 6 / b

  i = 0 + 1 - b * 5 / a + 7

end program test_exprs
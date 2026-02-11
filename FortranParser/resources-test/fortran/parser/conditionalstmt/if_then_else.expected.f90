program if
  implicit none

  logical :: cond
  cond = .false.

  if (cond) then
    print *, "cond is true"
  else
    print *, "cond is false"
  end if
end program if
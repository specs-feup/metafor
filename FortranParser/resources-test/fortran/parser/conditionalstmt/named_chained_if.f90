program if
  logical :: cond1, cond2
  cond1 = .false.
  cond2 = .true.

  named_if: if (cond1) then
    print *, "cond1 is true"
  else if (cond2) then
    print *, "cond2 is true"
  else
    print *, "both conditions are false"
  end if named_if
end program if
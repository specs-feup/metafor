program test_named_arguments
    implicit none

    call print_person_info(age=30, city="Lisbon", name="Carlos")

contains

    subroutine print_person_info(name, age, city)
        character(len=*), intent(in) :: name
        integer,          intent(in) :: age
        character(len=*), intent(in) :: city

        print *, "Name:", name
        print *, "Age: ", age
        print *, "City:", city
    end subroutine print_person_info

end program test_named_arguments
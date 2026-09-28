PROGRAM TEST_NAMED_ARGUMENTS
    IMPLICIT NONE

    CALL print_person_info(age=30, city="Lisbon", name="Carlos")

CONTAINS

    SUBROUTINE print_person_info(name, age, city)
        CHARACTER(LEN=*), INTENT(IN) :: name
        INTEGER, INTENT(IN) :: age
        CHARACTER(LEN=*), INTENT(IN) :: city

        PRINT *, "Name:", name
        PRINT *, "Age: ", age
        PRINT *, "City:", city
    END SUBROUTINE print_person_info

END PROGRAM TEST_NAMED_ARGUMENTS
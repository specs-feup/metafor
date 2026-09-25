PROGRAM STRUCTURE_COMPONENT
    IMPLICIT NONE

    TYPE :: address
        CHARACTER(LEN=30) :: city
        INTEGER :: zip
    END TYPE address

    TYPE :: person
        CHARACTER(LEN=20) :: name
        INTEGER :: age
        TYPE(address) :: home_addr  ! Nested struct
    END TYPE person

    ! Declare struct variables
    TYPE(person) :: user
    TYPE(person) :: team(2)  ! Array of structs

    ! Basic field access
    user%name = "Alice"
    user%age = 30

    ! Nested struct field access
    user%home_addr%city = "Porto"
    user%home_addr%zip = 4000

    ! Accessing fields within an array of structs
    team(1)%name = "Bob"
    team(1)%age = 25

    ! Reading Fields
    PRINT *, "User Name:", user%name
    PRINT *, "User City:", user%home_addr%city
    PRINT *, "Teammate 1:", team(1)%name

END PROGRAM STRUCTURE_COMPONENT
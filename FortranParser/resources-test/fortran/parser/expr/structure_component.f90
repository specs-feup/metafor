program structure_component
    implicit none

    type :: Address
        character(len=30) :: city
        integer          :: zip
    end type Address

    type :: Person
        character(len=20) :: name
        integer           :: age
        type(Address)     :: home_addr  ! Nested struct
    end type Person

    ! Declare struct variables
    type(Person) :: user
    type(Person) :: team(2)  ! Array of structs

    ! Basic field access
    user%name = "Alice"
    user%age  = 30

    ! Nested struct field access
    user%home_addr%city = "Porto"
    user%home_addr%zip  = 4000

    ! Accessing fields within an array of structs
    team(1)%name = "Bob"
    team(1)%age  = 25

    ! Reading Fields
    print *, "User Name:", user%name
    print *, "User City:", user%home_addr%city
    print *, "Teammate 1:", team(1)%name

end program structure_component
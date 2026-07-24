program length_selector
    integer, parameter :: c_kind = selected_char_kind('DEFAULT')

    character(len=10) :: c_len_keyword     ! Explicit keyword
    character(10)     :: c_len_positional  ! Positional shorthand
    character*10      :: c_len_legacy      ! Legacy asterisk notation

    character(kind=c_kind) :: c_kind_only

    character(len=20, kind=c_kind) :: c_both_keywords   ! Standard keyword order
    character(kind=c_kind, len=20) :: c_swapped_kw      ! Reversed keyword order
    character(20, c_kind)          :: c_both_positional ! Positional (len, kind)
    character(20, kind=c_kind)     :: c_mixed           ! Positional len + keyword kind

    character(len=:), allocatable :: c_deferred  ! Deferred length character

    character(len=*), parameter :: C_CONST = "Hello, Fortran!"  ! Constant length character
end program length_selector
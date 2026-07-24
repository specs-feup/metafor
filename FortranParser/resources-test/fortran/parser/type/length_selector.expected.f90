PROGRAM LENGTH_SELECTOR
    INTEGER, PARAMETER :: c_kind = selected_char_kind("DEFAULT")

    CHARACTER(LEN=10) :: c_len_keyword  ! Explicit keyword
    CHARACTER(LEN=10) :: c_len_positional  ! Positional shorthand
    CHARACTER*10 :: c_len_legacy  ! Legacy asterisk notation

    CHARACTER(KIND=c_kind) :: c_kind_only

    CHARACTER(KIND=c_kind, LEN=20) :: c_both_keywords  ! Standard keyword order
    CHARACTER(KIND=c_kind, LEN=20) :: c_swapped_kw  ! Reversed keyword order
    CHARACTER(KIND=c_kind, LEN=20) :: c_both_positional  ! Positional (len, kind)
    CHARACTER(KIND=c_kind, LEN=20) :: c_mixed  ! Positional len + keyword kind

    CHARACTER(LEN=:), ALLOCATABLE :: c_deferred  ! Deferred length character

    CHARACTER(LEN=*), PARAMETER :: c_const = "Hello, Fortran!"  ! Constant length character
END PROGRAM LENGTH_SELECTOR
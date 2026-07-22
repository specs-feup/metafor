PROGRAM COMPLEX_LITERAL
    USE, INTRINSIC :: iso_fortran_env, ONLY : sp => real32, dp => real64

    ! 1. Basic Default Real Literals
    COMPLEX c_default = (3.0, 4.0)

    ! 2. Integer Components (automatically converted to real)
    COMPLEX c_int = (1, -2)

    ! 3. Scientific / Exponential Notation
    COMPLEX c_exp = (1.5e2, -2.0e-1)  ! Represents (150.0, -0.2)

    ! 4. Double Precision (using 'd' exponent notation)
    COMPLEX(dp) c_double_d = (1.0d0, 3.141592653589793d0)

    ! 5. Kind Parameter Suffixes (Modern Standard)
    COMPLEX(dp) c_double_kind = (2.5_dp, -5.5_dp)
    COMPLEX(sp) c_single_kind = (1.0_sp, 0.5_sp)
END PROGRAM COMPLEX_LITERAL
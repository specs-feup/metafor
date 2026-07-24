PROGRAM SUBSTRING
    CHARACTER(LEN=8) :: s_cha, s_chb, v_cha, v_chb
    DIMENSION :: s_cha(10), s_chb(10), v_cha(10), v_chb(10)

    v_cha(1:10)(1:8) = v_cha(1:10)(1:4) // v_chb(1:10)(5:8)
END PROGRAM SUBSTRING
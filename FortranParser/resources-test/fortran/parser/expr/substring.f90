program substring
    CHARACTER(LEN = 8) S_CHA, S_CHB, V_CHA, V_CHB
    DIMENSION        S_CHA(10), S_CHB(10), V_CHA(10), V_CHB(10)

    V_CHA(1:10)(1:8) = V_CHA(1:10)(1:4) // V_CHB(1:10)(5:8)
end program substring
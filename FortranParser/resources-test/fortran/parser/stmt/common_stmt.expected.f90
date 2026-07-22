PROGRAM COMMON_STMT
    REAL x
    COMMON /data_block/ x, my_array(5)
    INTEGER my_array

    x = 42.0
END PROGRAM COMMON_STMT
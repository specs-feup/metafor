PROGRAM SUBSCRIPT_TRIPLET
    INTEGER :: vec(10) = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]

    PRINT *, vec(2:5)
    PRINT *, vec(:4)
    PRINT *, vec(1:9:2)
    PRINT *, vec(:)
    PRINT *, vec(8:3:-2)
END PROGRAM SUBSCRIPT_TRIPLET
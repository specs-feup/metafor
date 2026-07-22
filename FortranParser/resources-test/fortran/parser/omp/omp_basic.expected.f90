PROGRAM OPENMP_DEMO
    USE omp_lib

    INTEGER total_sum

    !$OMP PARALLEL
        total_sum = total_sum + 1
    !$OMP END PARALLEL

END PROGRAM OPENMP_DEMO
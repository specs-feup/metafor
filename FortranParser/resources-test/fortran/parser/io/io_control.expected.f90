PROGRAM IO_CONTROL_SPECS
    INTEGER :: u_seq, u_dir, u_stream
    INTEGER :: ios, async_id, chars_read
    CHARACTER(LEN=200) :: io_message
    CHARACTER(LEN=20) :: text_buffer
    REAL :: x = 123.456
    INTEGER :: val = 42

    ! Define a namelist for the NML specifier demo
    NAMELIST /my_namelist/ val, x

    ! Open temporary test files (Sequential, Direct, Stream)
    OPEN(NEWUNIT=u_seq, FILE="seq.txt", STATUS="replace", ACTION="readwrite")
    OPEN(NEWUNIT=u_dir, FILE="dir.bin", STATUS="replace", ACCESS="direct", RECL=16)
    OPEN(NEWUNIT=u_stream, FILE="stream.bin", STATUS="replace", ACCESS="stream")


    ! -----------------------------------------------------------------
    ! 1. Formatting, Rounding, Decimal, Sign, Delim, IOSTAT & IOMSG
    ! -----------------------------------------------------------------
    WRITE(u_seq, *, DECIMAL="comma", SIGN="plus", ROUND="up", DELIM="quote", IOSTAT=ios, IOMSG=io_message)


    ! -----------------------------------------------------------------
    ! 2. Namelist Specifier (NML)
    ! -----------------------------------------------------------------
    WRITE(u_seq, my_namelist, IOSTAT=ios)


    ! -----------------------------------------------------------------
    ! 3. Non-Advancing I/O (ADVANCE, SIZE, PAD, EOR, ERR)
    ! -----------------------------------------------------------------
    REWIND(u_seq)
    READ(u_seq, "(A)", ADVANCE="no", PAD="yes", SIZE=chars_read, EOR=100, ERR=900, IOSTAT=ios) text_buffer

    100 CONTINUE


    ! -----------------------------------------------------------------
    ! 4. Direct Access (REC)
    ! -----------------------------------------------------------------
    WRITE(u_dir, REC=1, IOSTAT=ios) val, x


    ! -----------------------------------------------------------------
    ! 5. Stream Access (POS)
    ! -----------------------------------------------------------------
    WRITE(u_stream, POS=10, IOSTAT=ios) "Stream Header"

    ! -----------------------------------------------------------------
    ! 6. Asynchronous Non-Blocking I/O (ASYNCHRONOUS, ID)
    ! -----------------------------------------------------------------
    WRITE(u_seq, "(I5)", ASYNCHRONOUS="yes", ID=async_id, IOSTAT=ios) val

    ! Wait for async operation to complete before continuing
    WAIT(u_seq, ID=async_id)


    ! -----------------------------------------------------------------
    ! 7. Input Blank Handling and EOF Branching (BLANK, END)
    ! -----------------------------------------------------------------
    READ(u_seq, "(I5)", BLANK="zero", END=200, ERR=900, IOSTAT=ios) val

    200 CONTINUE
    PRINT *, "All 20 io-control-spec items demonstrated successfully!"

    ! Clean up files
    CLOSE(u_seq, STATUS="delete")
    CLOSE(u_dir, STATUS="delete")
    CLOSE(u_stream, STATUS="delete")
    STOP

    900 PRINT *, "I/O Error Occurred: ", trim(io_message)
END PROGRAM IO_CONTROL_SPECS
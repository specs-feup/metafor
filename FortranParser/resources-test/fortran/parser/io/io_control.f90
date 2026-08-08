program io_control_specs
    integer :: u_seq, u_dir, u_stream
    integer :: ios, async_id, chars_read
    character(len=200) :: io_message
    character(len=20)  :: text_buffer
    real    :: x = 123.456
    integer :: val = 42

    ! Define a namelist for the NML specifier demo
    namelist /my_namelist/ val, x

    ! Open temporary test files (Sequential, Direct, Stream)
    open(newunit=u_seq,    file='seq.txt',    status='replace', action='readwrite')
    open(newunit=u_dir,    file='dir.bin',    status='replace', access='direct', recl=16)
    open(newunit=u_stream, file='stream.bin', status='replace', access='stream')


    ! -----------------------------------------------------------------
    ! 1. Formatting, Rounding, Decimal, Sign, Delim, IOSTAT & IOMSG
    ! -----------------------------------------------------------------
    write( unit    = u_seq,        &
            fmt     = *,           &
            decimal = 'comma',     &
            sign    = 'plus',      &
            round   = 'up',        &
            delim   = 'quote',     &
            iostat  = ios,         &
            iomsg   = io_message )


    ! -----------------------------------------------------------------
    ! 2. Namelist Specifier (NML)
    ! -----------------------------------------------------------------
    write( unit = u_seq,           &
            nml  = my_namelist,    &
            iostat = ios )


    ! -----------------------------------------------------------------
    ! 3. Non-Advancing I/O (ADVANCE, SIZE, PAD, EOR, ERR)
    ! -----------------------------------------------------------------
    rewind(u_seq)
    read( unit    = u_seq,          &
            fmt     = '(A)',        &
            advance = 'no',         &
            pad     = 'yes',        &
            size    = chars_read,   &
            eor     = 100,          &
            err     = 900,          &
            iostat  = ios ) text_buffer

    100 continue


    ! -----------------------------------------------------------------
    ! 4. Direct Access (REC)
    ! -----------------------------------------------------------------
    write( unit   = u_dir,         &
            rec    = 1,            &
            iostat = ios ) val, x


    ! -----------------------------------------------------------------
    ! 5. Stream Access (POS)
    ! -----------------------------------------------------------------
    write( unit   = u_stream,      &
            pos    = 10,           &
            iostat = ios ) "Stream Header"


    ! -----------------------------------------------------------------
    ! 6. Asynchronous Non-Blocking I/O (ASYNCHRONOUS, ID)
    ! -----------------------------------------------------------------
    write( unit         = u_seq,     &
            fmt          = '(I5)',   &
            asynchronous = 'yes',    &
            id           = async_id, &
            iostat       = ios ) val

    ! Wait for async operation to complete before continuing
    wait(unit=u_seq, id=async_id)


    ! -----------------------------------------------------------------
    ! 7. Input Blank Handling and EOF Branching (BLANK, END)
    ! -----------------------------------------------------------------
    read( unit   = u_seq,           &
            fmt    = '(I5)',        &
            blank  = 'zero',        &
            end    = 200,           &
            err    = 900,           &
            iostat = ios ) val

    200 continue
    print *, "All 20 io-control-spec items demonstrated successfully!"

    ! Clean up files
    close(u_seq, status='delete')
    close(u_dir, status='delete')
    close(u_stream, status='delete')
    stop

    900 print *, "I/O Error Occurred: ", trim(io_message)
end program io_control_specs
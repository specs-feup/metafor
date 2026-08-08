      INTEGERa(1000)

      a=0
      a=a+1

      DO10i=1,1000
      IF(a(i)/=1)THEN
      WRITE(6,*)"NG"
      WRITE(6,*)"ELEMENT NUMBER = A(",i,")"
      GOTO20
      ENDIF
   10 CONTINUE

      WRITE(6,*)"OK"

   20 CONTINUE
      STOP
      END

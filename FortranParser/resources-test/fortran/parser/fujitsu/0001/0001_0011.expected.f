      INTEGER a(1000),b(1000),c(1000),d(1000),e(1000),f(1000),code
      DATA a/1000*1/,b/1000*2/,c/1000*3/,d/1000*4/,e/1000*5/,f/1000*6/
C
      code=0
      CALL sub1(a+b,c+d,e+f,code)
C
      IF(code==0)WRITE(6,*)"OK"
C
      STOP
      END
C
C
      SUBROUTINE sub1(a,b,c,code)
      INTEGER a(1000),b(1000),c(1000),code
C
      DO 10 i=1,1000
      IF(a(i)/=3)THEN
      WRITE(6,*)"NG"
      WRITE(6,*)"ELEMENT NUMBER = A(",i,")"
      code=1
      GOTO 20
      ENDIF
   10 CONTINUE
C
   20 CONTINUE
      DO 30 i=1,1000
      IF(b(i)/=7)THEN
      WRITE(6,*)"NG"
      WRITE(6,*)"ELEMENT NUMBER = B(",i,")"
      code=1
      GOTO 40
      ENDIF
   30 CONTINUE
C
   40 CONTINUE
      DO 50 i=1,1000
      IF(c(i)/=11)THEN
      WRITE(6,*)"NG"
      WRITE(6,*)"ELEMENT NUMBER = C(",i,")"
      code=1
      GOTO 60
      ENDIF
   50 CONTINUE
C
   60 CONTINUE
      RETURN
      END

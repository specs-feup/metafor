      INTEGER a(1000),b(1000),c(1000),code
      DATA a/1000*0/,b/1000*0/,c/1000*0/
C
      code=0
      CALL sub1(a,b,c,1000,code)
C
      IF(code==0)WRITE(6,*)"OK"
C
      STOP
      END
C
      SUBROUTINE sub1(a,b,c,n,code)
      INTEGER a(n),b(n),c(n),code
C
      a(1:n)=b(1:n)+1
      b(1:n)=b(1:n)+1
      c(1:n)=c(1:n)+1
C
      DO 10 i=1,1000
      IF(a(i)/=1)THEN
      WRITE(6,*)"NG"
      WRITE(6,*)"ELEMENT NUMBER = A(",i,")"
      code=1
      GOTO 20
      ENDIF
   10 CONTINUE
C
   20 CONTINUE
      DO 30 i=1,1000
      IF(b(i)/=1)THEN
      WRITE(6,*)"NG"
      WRITE(6,*)"ELEMENT NUMBER = B(",i,")"
      code=1
      GOTO 40
      ENDIF
   30 CONTINUE
C
   40 CONTINUE
      DO 50 i=1,1000
      IF(c(i)/=1)THEN
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
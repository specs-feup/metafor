      INTEGER*4error,i
      COMPLEX*8s_a,s_b,v_a,v_b,s_dvt
      DIMENSIONs_a(10),s_b(10),v_a(10),v_b(10),s_dvt(10)
      DATAs_a,s_b/10*(1.0,2.0),10*(2.0,1.0)/
      DATAv_a,v_b/10*(1.0,2.0),10*(2.0,1.0)/
      DATAerror/0/
C
      DO10i=1,5,1
      s_dvt(i)=s_a(i)+s_b(i)
   10 CONTINUE
      DO20i=1,5,1
      s_a(i+1)=s_dvt(i)
   20 CONTINUE
      DO30i=1,5,1
      s_dvt(i)=s_b(i)+s_a(i)
   30 CONTINUE
      DO40i=1,5,1
      s_b(i+1)=s_dvt(i)
   40 CONTINUE
C
      v_a(2:6)=v_a(1:5)+v_b(1:5)
      v_b(2:6)=v_b(1:5)+v_a(1:5)
      CALLsub1(error,v_a,v_b,v_a+v_b)
      CALLsub2(error,v_a,v_b,v_a-v_b)
C
      DO50i=1,10,1
      IF(v_a(i)/=s_a(i))THEN
      error=error+1
      ENDIF
      IF(v_b(i)/=s_b(i))THEN
      error=error+1
      ENDIF
   50 CONTINUE
      IF(error==0)THEN
      WRITE(6,*)"OK"
      ELSE
      WRITE(6,*)"NG"
      WRITE(6,*)"ERROR=",error
      WRITE(6,*)s_a
      WRITE(6,*)v_a
      WRITE(6,*)s_b
      WRITE(6,*)v_b
      ENDIF
C
      STOP
      END
C
      SUBROUTINEsub1(error,a,b,c)
      INTEGER*4error
      COMPLEX*8a,b,c
      DIMENSIONa(10),b(10),c(10)
C
      DO60i=1,10,1
      IF(c(i)/=a(i)+b(i))THEN
      error=error+1
      ENDIF
   60 CONTINUE
C
      RETURN
      END
C
      SUBROUTINEsub2(error,a,b,c)
      INTEGER*4error
      COMPLEX*8a,b,c
      DIMENSIONa(10),b(10),c(10)
C
      DO70i=1,10,1
      IF(c(i)/=a(i)-b(i))THEN
      error=error+1
      ENDIF
   70 CONTINUE
C
      RETURN
      END

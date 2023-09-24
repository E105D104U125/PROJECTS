.data 0
num0:  .word 1  # posic 0
num1:  .word 2  # posic 4
num2:  .word 4  # posic 8 
num3:  .word 8  # posic 12 
num4:  .word 16 # posic 16 
num5:  .word 32 # posic 20
num6:  .word 0  # posic 24
num7:  .word 0  # posic 28
num8:  .word 0  # posic 32
num9:  .word 0  # posic 36
num10: .word 0  # posic 40
num11: .word 0  # posic 44

.text 0
main:

  # carga num0 a num5 en los registros 9 a 14
  lw $t1, 0($zero)  # lw $r9,  0($r0)  -> r9  = 1
  lw $t2, 4($zero)  # lw $r10, 4($r0)  -> r10 = 2
  lw $t3, 8($zero)  # lw $r11, 8($r0)  -> r11 = 4 
  lw $t4, 0($zero)  # lw $r9,  0($r0)  -> r9  = 1
  nop
  nop
  nop
  nop
  nop
  
# Instruccion tipo R, salto efectivo  
  add $s1, $t1, $zero # r17 = 1
  beq $s1, $t1, salto # 1 = 1
  addi $s3, $zero, 1 #junk instructions, r19 = 1 
  addi $s3, $s3, 1
  addi $s3, $s3, 1
  
salto1:
# Instruccion tipo R, salto no efectivo  
  add $s1, $t1, $t2 # r17 = 4
  beq $s1, $t2, salto1 # 4 != 2
  addi $s3, $zero, 1 #instrucciones basura
  addi $s3, $s3, 1
  addi $s3, $s3, 1
  
  
# Lectura de memoria, salto no efectivo
  lw $t3, 12($zero) # r11 = 8
  beq $t3, $s1, salto1 # 8 != 4
  addi $s3, $zero, 1 #instrucciones basura
  addi $s3, $s3, 1
  addi $s3, $s3, 1
  
  beq $zero, $zero, final # -> bucle infinito, volvera aqui tras varios NOPs.

salto:
  # Lectura de memoria, salto efectivo
  lw $t1, 4($zero)  # r9 = 2
  beq $t1, $t2, salto1 # 2 = 2
  addi $s3, $zero, 1 #instrucciones basura
  addi $s3, $s3, 1
  addi $s3, $s3, 1
final:
  beq $zero, $zero, final # -> bucle infinito, volvera aqui tras varios NOPs.
  nop
  nop
  nop
  nop
  
  


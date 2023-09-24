--------------------------------------------------------------------------------
-- Procesador MIPS con pipeline curso Arquitectura 2021-2022
--
-- Autores: Eduardo Terres Caballero y Diego Vicente Miguel
--
--------------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity processor is
   port(
      Clk         : in  std_logic; -- Reloj activo en flanco subida
      Reset       : in  std_logic; -- Reset asincrono activo nivel alto
      -- Instruction memory
      IAddr      : out std_logic_vector(31 downto 0); -- Direccion Instr
      IDataIn    : in  std_logic_vector(31 downto 0); -- Instruccion leida
      -- Data memory
      DAddr      : out std_logic_vector(31 downto 0); -- Direccion
      DRdEn      : out std_logic;                     -- Habilitacion lectura
      DWrEn      : out std_logic;                     -- Habilitacion escritura
      DDataOut   : out std_logic_vector(31 downto 0); -- Dato escrito
      DDataIn    : in  std_logic_vector(31 downto 0)  -- Dato leido
   );
end processor;

architecture rtl of processor is

  component alu
    port(
      OpA      : in std_logic_vector (31 downto 0);
      OpB      : in std_logic_vector (31 downto 0);
      Control  : in std_logic_vector (3 downto 0);
      Result   : out std_logic_vector (31 downto 0);
      Signflag : out std_logic;
      Zflag    : out std_logic
    );
  end component;

  component reg_bank
     port (
        Clk   : in std_logic; -- Reloj activo en flanco de subida
        Reset : in std_logic; -- Reset as�ncrono a nivel alto
        A1    : in std_logic_vector(4 downto 0);   -- Direcci�n para el puerto Rd1
        Rd1   : out std_logic_vector(31 downto 0); -- Dato del puerto Rd1
        A2    : in std_logic_vector(4 downto 0);   -- Direcci�n para el puerto Rd2
        Rd2   : out std_logic_vector(31 downto 0); -- Dato del puerto Rd2
        A3    : in std_logic_vector(4 downto 0);   -- Direcci�n para el puerto Wd3
        Wd3   : in std_logic_vector(31 downto 0);  -- Dato de entrada Wd3
        We3   : in std_logic -- Habilitaci�n de la escritura de Wd3
     );
  end component reg_bank;

  component control_unit
     port (
        -- Entrada = codigo de operacion en la instruccion:
        OpCode   : in  std_logic_vector (5 downto 0);
        -- Seniales para el PC
        Branch   : out  std_logic; -- 1 = Ejecutandose instruccion branch
        -- Seniales relativas a la memoria
        MemToReg : out  std_logic; -- 1 = Escribir en registro la salida de la mem.
        MemWrite : out  std_logic; -- Escribir la memoria
        MemRead  : out  std_logic; -- Leer la memoria
        -- Seniales para la ALU
        ALUSrc   : out  std_logic;                     -- 0 = oper.B es registro, 1 = es valor inm.
        ALUOp    : out  std_logic_vector (2 downto 0); -- Tipo operacion para control de la ALU
        -- Seniales para el GPR
        RegWrite : out  std_logic; -- 1 = Escribir registro
        RegDst   : out  std_logic;  -- 0 = Reg. destino es rt, 1=rd
	-- Seniales para los saltos
        Jump : out std_logic --1 ejecutando instrucciones de saltos

     );
  end component;

  component alu_control is
   port (
      -- Entradas:
      ALUOp  : in std_logic_vector (2 downto 0); -- Codigo de control desde la unidad de control
      Funct  : in std_logic_vector (5 downto 0); -- Campo "funct" de la instruccion
      -- Salida de control para la ALU:
      ALUControl : out std_logic_vector (3 downto 0) -- Define operacion a ejecutar por la ALU
   );
 end component alu_control;

  --signal Alu_Op2      : std_logic_vector(31 downto 0);
  signal ALU_IGUAL_EX, ALU_IGUAL_MEM    : std_logic;
  signal AluControl   : std_logic_vector(3 downto 0);
  signal reg_RD_data  : std_logic_vector(31 downto 0);
  --signal reg_RD	: std_logic_vector(4 downto 0);
  signal reg_RD_ID      : std_logic_vector(4 downto 0); 
  signal reg_RD_EX      : std_logic_vector(4 downto 0);
  signal reg_RD_MEM      : std_logic_vector(4 downto 0);
  signal reg_RD_WB      : std_logic_vector(4 downto 0);

  signal Regs_eq_branch : std_logic;
  signal PC_next        : std_logic_vector(31 downto 0) :=  (others=> '0');
  signal PC_reg         : std_logic_vector(31 downto 0);
  signal PC_plus4_IF      : std_logic_vector(31 downto 0);
  signal PC_plus4_ID      : std_logic_vector(31 downto 0);
  signal PC_plus4_EX      : std_logic_vector(31 downto 0);

  signal Instruction_IF    : std_logic_vector(31 downto 0); -- La instrucción desde lamem de instr
  signal Instruction_ID    : std_logic_vector(31 downto 0); -- La instrucción desde lamem de instr 
  signal Inm_ext_ID       : std_logic_vector(31 downto 0); -- La parte baja de la instrucción extendida de signo
  signal Inm_ext_EX       : std_logic_vector(31 downto 0); -- La parte baja de la instrucción extendida de signo 
  signal reg_RS_ID, reg_RT_ID : std_logic_vector(31 downto 0);
  signal reg_RS_EX, reg_RT_EX : std_logic_vector(31 downto 0);
  signal reg_RT_MEM : std_logic_vector(31 downto 0);

  --Aniado seniales para registros destino
  signal reg_dst1_ID, reg_dst2_ID : std_logic_vector(4 downto 0);
  signal reg_dst1_EX, reg_dst2_EX : std_logic_vector(4 downto 0);


  signal dataIn_Mem_MEM    : std_logic_vector(31 downto 0); --From Data Memory
  signal dataIn_Mem_WB    : std_logic_vector(31 downto 0); --From Data Memory 
  signal Addr_Branch_EX, Addr_Branch_MEM    : std_logic_vector(31 downto 0);

  signal Ctrl_Jump_ID, Ctrl_Branch_ID, Ctrl_MemWrite_ID, Ctrl_MemRead_ID,  Ctrl_ALUSrc_ID, Ctrl_RegDest_ID, Ctrl_MemToReg_ID, Ctrl_RegWrite_ID : std_logic;
  signal Ctrl_Jump_EX, Ctrl_Branch_EX, Ctrl_MemWrite_EX, Ctrl_MemRead_EX,  Ctrl_ALUSrc_EX, Ctrl_RegDest_EX, Ctrl_MemToReg_EX, Ctrl_RegWrite_EX : std_logic;
  signal Ctrl_MemWrite_MEM, Ctrl_MemRead_MEM, Ctrl_MemToReg_MEM, Ctrl_RegWrite_MEM, Ctrl_Jump_MEM, Ctrl_Branch_MEM : std_logic;
  signal Ctrl_MemToReg_WB, Ctrl_RegWrite_WB  : std_logic;


  signal Ctrl_ALUOP_ID     : std_logic_vector(2 downto 0);
  signal Ctrl_ALUOP_EX     : std_logic_vector(2 downto 0);

  signal Addr_Jump_ID      : std_logic_vector(31 downto 0);
  signal Addr_Jump_EX, Addr_Jump_MEM      : std_logic_vector(31 downto 0);
  signal Addr_Jump_dest : std_logic_vector(31 downto 0);

  signal desition_Jump    : std_logic;
  signal Alu_Res_EX        : std_logic_vector(31 downto 0);
  signal Alu_Res_MEM        : std_logic_vector(31 downto 0);
  signal Alu_Res_WB        : std_logic_vector(31 downto 0);

--Forwarding unit
  signal Forward_A, Forward_B : std_logic_vector(1 downto 0);

--Seniales de entrada de la ALU
  signal ALU_Op1, ALU_Op2 : std_logic_vector(31 downto 0);

--Senial auxiliar en la salida del multiplexor
  signal reg_RT_aux : std_logic_vector(31 downto 0);

--Load enable
  signal Load_enable : std_logic;

-- Regs numbers
  signal reg_RS_number_EX : std_logic_vector(4 downto 0);
  signal reg_RS_IF, reg_RT_IF: std_logic_vector(4 downto 0);

begin

-- ETAPA IF

  PC_next <= Addr_Jump_dest when desition_Jump= '1' else PC_plus4_IF;

  PC_reg_proc: process(Clk, Reset)
  
  begin
    if Reset = '1' then
      PC_reg <= (others => '0');
    elsif rising_edge(Clk) then
		if Load_enable = '1' then
			PC_reg <= PC_next ;
		end if;
    end if;
  end process;

--Guardar en registros IF-ID
  PC_plus4_IF   <= PC_reg + 4;
  IAddr       <= PC_reg;
  Instruction_IF <= IDataIn;

  reg_RS_IF <= Instruction_IF(25 downto 21);
  reg_RT_IF <= Instruction_IF(20 downto 16);

--ETAPA ID

--Sacar de registro IF-ID
  reg_IF_ID: process(Clk, Reset)
  begin
    if Reset = '1' then
		PC_plus4_ID <= (others => '0');
		Instruction_ID <= (others => '0');
	elsif rising_edge(Clk) then
		if Load_enable = '1' then
			PC_plus4_ID <= PC_plus4_IF;
			Instruction_ID <= Instruction_IF;
		end if;
	end if;
  end process;

  RegsMIPS : reg_bank
  port map (
    Clk   => Clk,
    Reset => Reset,
    A1    => Instruction_ID(25 downto 21),
    Rd1   => reg_RS_ID,
    A2    => Instruction_ID(20 downto 16),
    Rd2   => reg_RT_ID,
    A3    => reg_RD_ID,
    Wd3   => reg_RD_data,
    We3   => Ctrl_RegWrite_WB
  );

  UnidadControl : control_unit
  port map(
    OpCode   => Instruction_ID(31 downto 26),
    -- Señales para el PC
    Jump   => Ctrl_Jump_ID,
    Branch   => Ctrl_Branch_ID,
    -- Señales para la memoria
    MemToReg => Ctrl_MemToReg_ID,
    MemWrite => Ctrl_MemWrite_ID,
    MemRead  => Ctrl_MemRead_ID,
    -- Señales para la ALU
    ALUSrc   => Ctrl_ALUSrc_ID,
    ALUOP    => Ctrl_ALUOP_ID,
    -- Señales para el GPR
    RegWrite => Ctrl_RegWrite_ID,
    RegDst   => Ctrl_RegDest_ID
  );


--Guardar en registros ID-EX
  Inm_ext_ID        <= x"FFFF" & Instruction_ID(15 downto 0) when Instruction_ID(15)='1' else
                    x"0000" & Instruction_ID(15 downto 0);
  Addr_Jump_ID      <=  PC_plus4_ID(31 downto 28) & Instruction_ID(25 downto 0) & "00";

  reg_dst1_ID <= Instruction_ID(20 downto 16);
  reg_dst2_ID <= Instruction_ID(15 downto 11);

  reg_RD_ID <= reg_RD_WB;

  
  --ETAPA EX
  
  reg_ID_EX: process(Clk, Reset)
  begin
    if Reset = '1' then
      PC_plus4_EX <= (others => '0');
      Ctrl_Jump_EX <= '0';
      Ctrl_Branch_EX <= '0';
      Ctrl_MemWrite_EX <= '0';
      Ctrl_MemRead_EX <= '0';
      Ctrl_ALUSrc_EX <= '0';
      Ctrl_RegDest_EX <= '0';
      Ctrl_MemToReg_EX <= '0';
      Ctrl_RegWrite_EX <= '0';
      Ctrl_ALUOP_EX <= (others => '0');
      reg_dst1_EX <= (others => '0');
      reg_dst2_EX <= (others => '0');
      Inm_ext_EX <= (others => '0');
      Addr_Jump_EX <= (others => '0');
      reg_RS_EX <= (others => '0');
      reg_RT_EX <= (others => '0');
    elsif rising_edge(Clk) then
		if Load_enable = '1' then
		  PC_plus4_EX <= PC_plus4_ID;
		  Ctrl_Jump_EX <= Ctrl_Jump_ID;
		  Ctrl_Branch_EX <= Ctrl_Branch_ID;
		  Ctrl_MemWrite_EX <= Ctrl_MemWrite_ID;
		  Ctrl_MemRead_EX <= Ctrl_MemRead_ID;
		  Ctrl_ALUSrc_EX <= Ctrl_ALUSrc_ID;
		  Ctrl_RegDest_EX <= Ctrl_RegDest_ID;
		  Ctrl_MemToReg_EX <= Ctrl_MemToReg_ID;
		  Ctrl_RegWrite_EX <= Ctrl_RegWrite_ID;
		  Ctrl_ALUOP_EX <= Ctrl_ALUOP_ID;
		  reg_dst1_EX <= reg_dst1_ID;
		  reg_dst2_EX <= reg_dst2_ID;
		  Inm_ext_EX <= Inm_ext_ID;
		  Addr_Jump_EX <= Addr_Jump_ID;
		  reg_RS_EX <= reg_RS_ID;
		  reg_RT_EX <= reg_RT_ID;
		  reg_RS_number_EX <= Instruction_ID(25 downto 21);
		elsif Load_enable = '0' then
		  Ctrl_Jump_Ex <= '0'; --No salto incondicional
		  Ctrl_Branch_EX <= '0'; --No salto condicional
		  Ctrl_MemWrite_EX <= '0'; --No se escribe en memoria
		  Ctrl_RegWrite_EX <= '0'; --No se escribe en registro
		  Ctrl_MemRead_EX <= '0'; --Para poder bajar el Load_enable en el siguiente ciclo
		end if;
    end if;
  end process;


-- Forwarding Unit
  FU: process(Reset, Ctrl_RegWrite_MEM, reg_RD_MEM, reg_RS_number_EX, Ctrl_RegWrite_WB, reg_RD_WB, reg_dst1_EX)
  begin
    if Reset = '1' then
	Forward_A <= (others => '0');
	Forward_B <= (others => '0');
    else
	if (Ctrl_RegWrite_MEM = '1' and (reg_RD_MEM/= x"0000") and (reg_RD_MEM = reg_RS_number_EX)) then
	   Forward_A <= "10";
	elsif (Ctrl_RegWrite_WB = '1' and (reg_RD_WB /= x"0000") and (reg_RD_WB = reg_RS_number_EX)) then
	   Forward_A <= "01";
	else
	   Forward_A <= "00";
	end if;
	if (Ctrl_RegWrite_MEM = '1' and (reg_RD_MEM /= x"0000") and (reg_RD_MEM = reg_dst1_EX)) then
	   Forward_B <= "10";
	elsif (Ctrl_RegWrite_WB = '1' and (reg_RD_WB /= x"0000") and (reg_RD_WB = reg_dst1_EX)) then
	   Forward_B <= "01";
	else
	   Forward_B <= "00";
	end if;
    end if;
  end process;

-- Hazard Detection Unit
  HDU: process(Reset, Ctrl_MemRead_EX, reg_RT_EX, reg_RS_ID, reg_RT_ID)
  begin
    if Reset = '1' then
	Load_enable <= '1';
    else
		if Ctrl_MemRead_EX = '1' and ((reg_RT_EX = reg_RS_ID) or (reg_RT_EX = reg_RT_ID)) and reg_RT_EX /= x"0000" then
			Load_enable <= '0';
		else Load_enable <= '1';
		end if;
    end if;
  end process;

--Guardar en registros EX-MEM
  Addr_Branch_EX    <= PC_plus4_EX + ( Inm_ext_EX(29 downto 0) & "00");


  Alu_control_i: alu_control
  port map(
    -- Entradas:
    ALUOp  => Ctrl_ALUOP_EX, -- Codigo de control desde la unidad de control
    Funct  => Inm_ext_EX (5 downto 0), -- Campo "funct" de la instruccion
    -- Salida de control para la ALU:
    ALUControl => AluControl -- Define operacion a ejecutar por la ALU
  );

  Alu_MIPS : alu
  port map (
    OpA      => Alu_Op1,
    OpB      => Alu_Op2,
    Control  => AluControl,
    Result   => Alu_Res_EX,
    Signflag => open,
    Zflag    => ALU_IGUAL_EX
  );

--Multiplexores de entrada de ALU

  reg_RT_aux <= reg_RT_EX when Forward_B = "00" else
		Alu_Res_MEM when Forward_B = "10" else
		reg_RD_data; --Forward_B = '01'

  Alu_Op2    <= reg_RT_aux when Ctrl_ALUSrc_EX = '0' else
		Inm_ext_EX; --Ctrl_ALUSrc_EX = '1'

  Alu_Op1    <= reg_RS_EX when Forward_A = "00" else
		Alu_Res_MEM when Forward_A = "10" else
		reg_RD_data; --Forward_A = '01'

--Guardar en registros EX-MEM
  reg_RD_EX     <= reg_dst1_EX when Ctrl_RegDest_EX = '0' else reg_dst2_EX;

--ETAPA MEM

--Guardar en registros MEM-WB
  reg_EX_MEM: process(Clk, Reset)
  begin
    if Reset = '1' then
      	Ctrl_MemWrite_MEM <= '0';
      	Ctrl_MemRead_MEM <= '0';
      	Ctrl_MemToReg_MEM <= '0';
      	Ctrl_RegWrite_MEM <= '0';
      	reg_RD_MEM <= (others => '0');
      	reg_RT_MEM <= (others => '0');
      	Alu_Res_MEM <= (others => '0');
		Ctrl_Jump_MEM <= '0';
		Ctrl_Branch_MEM <= '0';
		ALU_IGUAL_MEM <= '0';
		Addr_Branch_MEM <= (others => '0');
		Addr_Jump_MEM <= (others => '0');
    elsif rising_edge(Clk) then
		Ctrl_MemWrite_MEM <= Ctrl_MemWrite_EX;
		Ctrl_MemRead_MEM <= Ctrl_MemRead_EX;
		Ctrl_MemToReg_MEM <= Ctrl_MemToReg_EX;
		Ctrl_RegWrite_MEM <= Ctrl_RegWrite_EX;
		reg_RD_MEM <= reg_RD_EX;
		reg_RT_MEM <= reg_RT_aux; --Tener en cuenta salida del multiplexor Alu_Op2
		Alu_Res_MEM <= Alu_Res_EX;
		Addr_Branch_MEM <= Addr_Branch_EX;
		Addr_Jump_MEM <= Addr_Jump_EX;
		Ctrl_Jump_MEM <= Ctrl_Jump_EX;
		Ctrl_Branch_MEM <= Ctrl_Branch_EX;
		ALU_IGUAL_MEM <= ALU_IGUAL_EX;
    end if;
  end process;
  
  -- Logica de salto
    desition_Jump  <= Ctrl_Jump_MEM or (Ctrl_Branch_MEM and ALU_IGUAL_MEM); 
    Addr_Jump_dest <= Addr_Jump_MEM   when Ctrl_Jump_MEM='1' else
                    Addr_Branch_MEM when Ctrl_Branch_MEM='1' else
                    (others =>'0');
  

  DAddr      <= Alu_Res_MEM;
  DDataOut   <= reg_RT_MEM;
  DWrEn      <= Ctrl_MemWrite_MEM;
  dRdEn      <= Ctrl_MemRead_MEM;
  dataIn_Mem_MEM <= DDataIn;


--ETAPA WB

  reg_MEM_WB: process(Clk, Reset)
  begin
    if Reset = '1' then
      Ctrl_MemToReg_WB <= '0';
      Ctrl_RegWrite_WB <= '0';
      reg_RD_WB <= (others => '0');
      dataIn_Mem_WB <= (others => '0');
      Alu_Res_WB <= (others => '0');
    elsif rising_edge(Clk) then
      Ctrl_MemToReg_WB <= Ctrl_MemToReg_MEM;
      Ctrl_RegWrite_WB <= Ctrl_RegWrite_MEM;
      reg_RD_WB <= reg_RD_MEM;
      dataIn_Mem_WB <= dataIn_Mem_MEM;
      Alu_Res_WB <= Alu_Res_MEM;
    end if;
  end process;

  -- Se utiliza en ID
  reg_RD_data <= dataIn_Mem_WB when Ctrl_MemToReg_WB = '1' else Alu_Res_WB;

end architecture;

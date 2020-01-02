package computer;

import lombok.Value;

@Value
public class Instruction {
    private int opcode;
    private Mode[] parameterModes;
}

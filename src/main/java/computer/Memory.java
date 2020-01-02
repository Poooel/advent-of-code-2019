package computer;

public class Memory {
    private final static int MEMORY_SIZE = 32 * 1024;

    private int relativeBaseOffset = 0;

    private long[] internalMemory;

    public Memory() {
        initializeMemory();
    }

    public long read(int address) {
        return read(address, Mode.IMMEDIATE);
    }

    public long read(int address, Mode mode) {
        isValidAddress(address);

        switch (mode) {
            case POSITION:
                return internalMemory[getPositionAddress(address)];
            case IMMEDIATE:
                return internalMemory[address];
            case RELATIVE:
                return internalMemory[getRelativeAddress(address)];
            default:
                throw new UnknownModeException("Unknown mode: " + mode);
        }
    }

    public void write(int address, long value, Mode mode) {
        isValidAddress(address);

        switch (mode) {
            case POSITION:
                internalMemory[getPositionAddress(address)] = value;
                break;
            case RELATIVE:
                internalMemory[getRelativeAddress(address)] = value;
                break;
            default:
                throw new UnknownModeException("Unknown mode: " + mode);
        }
    }

    public void writeRange(int address, long[] values) {
        isValidAddress(address);
        System.arraycopy(values, 0, internalMemory, address, values.length);
    }

    private int getPositionAddress(int address) {
        return (int) internalMemory[address];
    }

    private int getRelativeAddress(int address) {
        return (int) (relativeBaseOffset + internalMemory[address]);
    }

    private void initializeMemory() {
        internalMemory = new long[MEMORY_SIZE];
    }

    private void isValidAddress(int address) {
        if (address >= MEMORY_SIZE) {
            throw new InvalidAccessException("Tried to access address: " + address + " but memory size is: " + MEMORY_SIZE);
        } else if (address < 0) {
            throw new InvalidAccessException("Can't access negative address: " + address);
        }
    }

    public void addToRelativeBaseOffset(int value) {
        relativeBaseOffset += value;
    }
}

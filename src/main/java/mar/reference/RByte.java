package mar.reference;

public class RByte {
    public RByte() {
    }
    public RByte(byte value) {
        this.value = value;
    }
    public byte get() {
        return value;
    }
    public void set(byte value) {
        this.value = value;
    }
    private byte value;
}

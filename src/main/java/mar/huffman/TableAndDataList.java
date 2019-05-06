package mar.huffman;

import java.util.LinkedList;
import java.util.List;

public class TableAndDataList {
    public TableAndDataList() {
        this.data = new LinkedList<>();
        this.table = new LinkedList<>();
    }
    public List<Byte> table;
    public List<Byte> data;
}
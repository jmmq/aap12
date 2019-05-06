package mar.huffman;

import mar.reference.RByte;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Huffman {
    static byte[] addNullByte(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length + 1];
        int i = 0;
        for (byte b : bytes) {
            newBytes[i] = b;
            i++;
        }
        newBytes[bytes.length] = 0;
        return newBytes;
    }
    static void printCodeMap(HashMap<Byte, List<Byte>> map) {
        System.out.println("CodeMap:");
        for (Map.Entry<Byte, List<Byte>> entry : map.entrySet()) {
            System.out.println(entry.getKey()
                    + String.format("[%02X]", entry.getKey())
                    + " -> "
                    + codeToString(entry.getValue()));
        }
    }
    static void printFrecMap(HashMap<Byte, Integer> map) {
        System.out.println("FrecMap:");
        for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
    static HashMap<Byte, Integer> getFreqMap(byte[] bytes) {
        HashMap<Byte, Integer> map = new HashMap<>();
        for (byte b : bytes) {
            if (map.containsKey(b)) {
                map.put(b, map.get(b) + 1);
            } else {
                map.put(b, 1);
            }
        }
        return map;
    }
    static void printFileBytes(byte[] bytes) {
        System.out.println("Bytes del archivo");
        for (byte b : bytes) {
            System.out.print("[" + b + "]");
        }
        System.out.println();
    }
    static byte[] readBytes(String fileName) {
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException exception) {
            System.out.println("No pude leer el archivo: " + exception);
            System.exit(1);
        }
        return null;
    }
    static List<Node> getNodeList(HashMap<Byte, Integer> map) {
        List<Node> list = new LinkedList<>();
        for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
            //System.out.println(entry.getKey() + " -> " + entry.getValue());
            Node node = new Node(true, entry.getValue(), entry.getKey());
            list.add(node);
        }
        return list;
    }
    static void printNodes(List<Node> nodes) {
        System.out.println("Nodes:");
        for (Node node : nodes) {
            System.out.println(node);
        }
    }
    static void sortNodes(List<Node> nodes) {
        Collections.sort(nodes, (a, b) -> a.frec - b.frec);
    }
    static Node[] removeLowest(List<Node> list) {
        Node[] lowest = new Node[2];
        lowest[0] = list.remove(0);
        lowest[1] = list.remove(0);
        return lowest;
    }
    static void insertToList(Node newNode, List<Node> list) {
        int i = 0;
        for (Node node : list) {
            if (node.frec > newNode.frec) {
                list.add(i, newNode);
                return;
            }
            i++;
        }
        list.add(newNode);
    }
    static Node huffman(List<Node> list) {
        while (list.size() > 1) {
            Node[] lowest = removeLowest(list);
            Node parent = new Node(lowest[0], lowest[1]);
            insertToList(parent, list);
        }
        return list.get(0);
    }
    static String codeToString(List<Byte> code) {
        StringBuilder sb = new StringBuilder();
        for (Byte b : code)
        {
            sb.append(b);
        }
        return sb.toString();
    }
    static List<Byte> cloneCode(List<Byte> code) {
        List<Byte> clone = new LinkedList<>();
        for (Byte b : code) {
            clone.add(b);
        }
        return clone;
    }
    static void visit(Node node, List<Byte> code, HashMap<Byte, List<Byte>> codeMap) {
        if (node.isChar)
        {
            //System.out.println("Char found: " + node.mChar);
            //System.out.println("Code: " + codeToString(code));
            codeMap.put(node.mChar, code);
            return;
        }
        List<Byte> lCode = cloneCode(code);
        lCode.add((byte)0);
        visit(node.lChild, lCode, codeMap);
        List<Byte> rCode = cloneCode(code);
        rCode.add((byte)1);
        visit(node.rChild, rCode, codeMap);
    }
    static HashMap<Byte, List<Byte>> getCodeMap(Node tree) {
        HashMap<Byte, List<Byte>> codeMap = new HashMap<>();
        List<Byte> code = new LinkedList<>();
        visit(tree, code, codeMap);
        return codeMap;
    }
    static boolean appendToByte(RByte rb, RByte ri, byte bit)
    {
        rb.set((byte)(rb.get() + (bit << ri.get())));
        ri.set((byte)(ri.get() - 1));
        if (ri.get() < 0) {
            return true;
        } else {
            return false;
        }
    }

    static List<Byte> getByteList(List<Byte> bitList)
    {
        List<Byte> byteList = new LinkedList<>();
        RByte rb = new RByte((byte)0);
        RByte ri = new RByte((byte)7);
        for (byte bit : bitList)
        {
            boolean append = appendToByte(rb, ri, bit);
            if (append)
            {
                byteList.add(rb.get());
                rb.set((byte)0);
                ri.set((byte)7);
            }
        }
        if(ri.get() < 7)
        {
            byteList.add(rb.get());
        }
        return byteList;
    }
    static List<Byte> getBitList(byte[] buffer, HashMap<Byte, List<Byte>> map)
    {
        List<Byte> bitList = new LinkedList<>();
        for (byte b : buffer)
        {
            List<Byte> code = map.get(b);
            for (byte bit : code)
            {
                bitList.add(bit);
            }
        }
        return bitList;
    }
    static void printBitList(List<Byte> list) {
        System.out.println("BitList: ");
        for (Byte bit : list) {
            System.out.print(bit);
        }
        System.out.println();
    }
    static void printByteList(List<Byte> list) {
        System.out.println("ByteList: ");
        for (Byte b : list) {
            System.out.print("[" + String.format("%02X", b) + "]");
        }
        System.out.println();
    }
    static void printByteArray(String name, byte[] array) {
        System.out.println(name + ":");
        for (byte b : array) {
            System.out.print("[" + String.format("%02X", b) + "]");
        }
        System.out.println();
    }
    static byte[] getByteArray(List<Byte> byteList) {
        byte[] byteArray = new byte[byteList.size()];
        int i = 0;
        for (byte b : byteList) {
            byteArray[i] = b;
            i++;
        }
        return byteArray;
    }
    static List<Byte> codeToByteList(List<Byte> code) {
        List<Byte> list = getByteList(code);
        return list;
    }
    static List<Byte> getTableList(HashMap<Byte, List<Byte>> map) {
        List<Byte> tableList = new LinkedList<>();
        for (Map.Entry<Byte, List<Byte>> entry : map.entrySet()) {
            //System.out.println(entry.getKey() + " -> "
                //+ codeToString(entry.getValue()));
            tableList.add(entry.getKey());
            tableList.add((byte)entry.getValue().size());
            List<Byte> codeByteList = codeToByteList(entry.getValue());
            for (Byte b : codeByteList) {
                tableList.add(b);
            }
        }
        tableList.add((byte)0);
        return tableList;
    }
    public static byte[] combineArrays(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        int i = 0;
        for (byte var : a) {
            c[i] = var;
            i++;
        }
        for (byte var : b) {
            c[i] = var;
            i++;
        }
        return c;
    }
    public static void byteArrayToFile(byte[] array, String name) {
        try (FileOutputStream fos = new FileOutputStream(name + ".sip")){
            fos.write(array);
            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        } catch (Exception exception) {
            System.err.println("Error: " + exception);
            System.exit(1);
        }
    }
    public static void comprimir(String[] args) {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        //System.out.println("Huffman");
        String fileName = "archivo.txt";
        byte[] fileBytes = readBytes(fileName);
        fileBytes = addNullByte(fileBytes);
        //printFileBytes(fileBytes);
        HashMap<Byte, Integer> freqMap = getFreqMap(fileBytes);
        //printFrecMap(frecMap);
        List<Node> nodes = getNodeList(freqMap);
        //printNodes(nodes);
        sortNodes(nodes);
        //printNodes(nodes);
        Node tree = huffman(nodes);
        HashMap<Byte, List<Byte>> codeMap = getCodeMap(tree);
        printCodeMap(codeMap);
        List<Byte> dataBitList = getBitList(fileBytes, codeMap);
        //printBitList(bitList);
        List<Byte> dataByteList = getByteList(dataBitList);
        //printByteList(byteList);
        byte[] dataArray = getByteArray(dataByteList);
        printByteArray("ByteArray", dataArray);
        List<Byte> tableList = getTableList(codeMap);
        byte[] tableArray = getByteArray(tableList);
        printByteArray("TableArray", tableArray);
        byte[] fileArray = combineArrays(dataArray, tableArray);
        printByteArray("FileArray", fileArray);
        byteArrayToFile(fileArray, fileName);
    }
}

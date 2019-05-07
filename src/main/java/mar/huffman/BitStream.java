package mar.huffman; 

import java.nio.file.*;

public class BitStream 
{
	public BitStream(String filename) 
	{
		try
		{
			m_bytes = Files.readAllBytes(Paths.get(filename));
		} 
		catch(Exception e)
		{
			System.out.println("Error reading file " + filename);
			System.exit(1);
		}
	}

	public byte nextBit() 
	{
		byte currentByte = m_bytes[(int)m_currentByte];
		byte bitpos = (byte) (7 - m_currentBit);
		byte bit = (byte) (currentByte & (1 << bitpos));
		forward(1);
		if(bit != 0)
			return 1;
		else
			return 0;
	}

	public int nextByte() 
	{
		int value = 0;
		for(byte bit = 0; bit < 8; bit++)
		{
			value *= 2;
			value += nextBit();
		}
		return value;
	}

	public boolean hasNextBit()
	{
		return m_currentByte < m_bytes.length;
	}

	public void skip()
	{
		forward(((-m_currentBit) % 8 + 8) % 8);
	}

	public void forward(long bits) 
	{
		bits += m_currentBit;
		m_currentByte += bits / (long) 8;
		m_currentBit = bits % (long) 8;
	}

	private byte[] m_bytes;
	private long m_currentByte;
	private long m_currentBit;
}

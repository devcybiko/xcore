package us.thinkable.xcore.mmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MMap {
	private String fileName;
	private int fileSize;
	private FileChannel fileChannel;
	private MappedByteBuffer mappedByteBuffer;
	private RandomAccessFile randomAccessFile;
	private int nextPosition = 0;

	/**
	 * map an existing file
	 * 
	 * @param fname
	 * @throws IOException
	 */
	public MMap(String fname) throws IOException {
		this.fileName = fname;

		File file = new File(fname);
		randomAccessFile = new RandomAccessFile(file, "rw");
		fileChannel = randomAccessFile.getChannel();
		fileSize = (int) fileChannel.size();
		mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
	}

	/**
	 * create a new mapped file of filesize
	 * 
	 * @param fname
	 * @param filesize
	 * @throws IOException
	 */
	public MMap(String fname, int filesize) throws IOException {
		this.fileName = fname;
		this.fileSize = filesize;

		File file = new File(fname);
		try {
			file.delete();
		} catch (Exception ignored) {
			//
		}

		randomAccessFile = new RandomAccessFile(file, "rw");
		fileChannel = randomAccessFile.getChannel();
		mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, filesize);
	}

	/**
	 * read a previously written object at position
	 * 
	 * @param position
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object read(int position) throws IOException, ClassNotFoundException {
		this.nextPosition = position;

		// read the size of the object
		byte[] objSize = read(position, 4);
		ByteBuffer byteBuffer = ByteBuffer.wrap(objSize);
		int size = byteBuffer.getInt();

		this.nextPosition += 4;

		// read the object from the mmaps
		byte[] objBytes = read(position + 4, size);
		this.nextPosition += objBytes.length;

		// deserialize the object
		ByteArrayInputStream bis = new ByteArrayInputStream(objBytes);
		ObjectInput in = new ObjectInputStream(bis);
		Object obj = in.readObject();

		// tidy up
		bis.close();
		in.close();
		return obj;
	}

	/**
	 * read a byte buffer from the mmap
	 * <p>
	 * note: does not update nextPosition
	 * 
	 * @param position
	 * @param size
	 * @return
	 */
	public byte[] read(int position, int size) {
		byte[] result = new byte[size];
		mappedByteBuffer.position(position);
		mappedByteBuffer.get(result);
		return result;
	}

	/**
	 * write a byte buffer to a position in the mmap
	 * <p>
	 * returns the next position to write to
	 * <p>
	 * note: does not update nextPosition
	 * 
	 * @param position
	 * @param bytes
	 * @return
	 */
	public int write(int position, byte[] bytes) {
		mappedByteBuffer.position(position);
		mappedByteBuffer.put(bytes);
		return position + bytes.length;
	}

	/**
	 * serialize and write an object to the mmap
	 * <p>
	 * returns the next position to write to
	 * <p>
	 * note: first the size of the object is written (4 bytes) then the
	 * serialized object is written
	 * <p>
	 * note: updates local member nextPosition
	 * 
	 * @param position
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public int write(int position, Object obj) throws IOException {
		// serialize the object to a byte buffer
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(obj);

		// write the size of the object to mmap
		byte[] objBytes = bos.toByteArray();
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(objBytes.length);
		byte[] objSize = bb.array();
		this.nextPosition = write(position, objSize);

		// write the serialized object to mmap
		nextPosition = write(nextPosition, objBytes);

		// tidy up
		out.close();
		bos.close();

		// return the next position
		return nextPosition;
	}

	/**
	 * if the file is too small, you may extend it. pass in the new size you
	 * want to make the file.
	 * <p>
	 * note: do not try to make the file smaller.
	 * <p>
	 * note: you know the file is too small when you get a BufferOverflow
	 * exception as a root cause
	 * 
	 * @param newSize
	 * @throws IOException
	 */
	public void extend(int newSize) throws IOException {
		if (newSize < this.fileSize) {
			return;
		}
		randomAccessFile.setLength(newSize);

		// recreate the memory mapped buffer
		mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, newSize);

		// write zeroes to the end of the file otherwise we won't get an
		// extension
		byte[] bytes = new byte[newSize - this.fileSize];
		write(this.fileSize, bytes);
		this.fileSize = newSize;
	}

	public void close() {
		try {
			this.fileChannel.close();
			this.randomAccessFile.close();
		} catch (Exception ex) {
			//throw new MMException("Problem closing: " + fileName, ex);
		}
	}

	public String getFileName() {
		return fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public int getNextPosition() {
		return nextPosition;
	}

}

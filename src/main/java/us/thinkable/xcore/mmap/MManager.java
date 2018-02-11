package us.thinkable.xcore.mmap;

import java.io.IOException;

public class MManager {
	private MMap mmap;
	int[] refids;
	int nextPosition;

	public MManager(String filename) throws IOException {
		try {
			this.mmap = new MMap(filename);
			this.refids = (int[]) this.mmap.read(0);
		} catch (IOException ex) {
			throw ex;
		} catch (ClassNotFoundException e) {
			throw new IOException("Not a memory managed file: " + filename, e);
		} catch (ClassCastException e) {
			throw new IOException("Not a memory managed file: " + filename, e);
		}
	}

	public MManager(String filename, int initialSize, int nRefIds) throws IOException {
		this.mmap = new MMap(filename, initialSize);
		this.refids = new int[nRefIds];
		this.nextPosition = this.mmap.write(0, refids);
	}

	public Integer malloc(Object obj) {
		return 0;
	}

	public void free(Integer ref) {
		//
	}

	public Object read(Integer ref) {
		return null;
	}

	public void write(Integer ref, Object obj) {
		//
	}

}

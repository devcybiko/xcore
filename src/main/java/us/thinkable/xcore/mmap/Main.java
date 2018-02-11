package us.thinkable.xcore.mmap;

import java.io.IOException;
import java.nio.BufferOverflowException;

public class Main {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String fname = "test.map";
		MMap mmap = new MMap(fname, 1024);
		System.out.println(mmap);

		// setup
		String str1 = "1: This is a test!";
		int pos1 = 0;
		System.out.println(pos1);

		// write the fist object
		int pos2 = mmap.write(pos1, str1);
		System.out.println(pos2);

		// write the second object
		String str2 = "2: Whoa! Hello World!";
		int pos3 = mmap.write(pos2, str2);
		System.out.println(pos3);

		// read the objects back into memory and print them out
		Object obj1 = mmap.read(pos1);
		Object obj2 = mmap.read(pos2);
		System.out.println(obj1);
		System.out.println(obj2);

		// write a third object
		String str3 = "3: This is a final test.";
		int pos4 = mmap.write(pos3, str3);

		// go back and rewrite obj2 - to show we can write out of order
		mmap.write(pos2, str2);

		// go back and read the objects out of order (1, 3, 2)
		obj1 = mmap.read(pos1);
		Object obj3 = mmap.read(pos3);
		obj2 = mmap.read(pos2);

		// print them out...
		System.out.println();
		System.out.println(obj1);
		System.out.println(obj2);
		System.out.println(obj3);

		// close the mmap
		mmap.close();

		// now let's reopen the mmap and make sure it still works
		mmap = new MMap(fname);

		// read our objects again
		obj1 = mmap.read(pos1);
		obj3 = mmap.read(pos3);
		obj2 = mmap.read(pos2);

		// print them out...
		System.out.println();
		System.out.println(obj1);
		System.out.println(obj2);
		System.out.println(obj3);

		// now let's write beyond the eof
		String bigString;
		for (bigString = str1; bigString.length() < 1024; bigString += str2) {
			bigString += str3;
		}

		int pos5 = 0;
		try {
			pos5 = mmap.write(pos4, bigString);
			System.out.println(pos5);
		} catch (BufferOverflowException ex) {
			mmap.extend(mmap.getFileSize() * 2);
			pos5 = mmap.write(pos4, bigString);
			System.out.println(pos5);
		}

		// read the objects back in the order they were written
		System.out.println(mmap.getNextPosition());
		obj1 = mmap.read(0);
		System.out.println(mmap.getNextPosition());
		obj2 = mmap.read(mmap.getNextPosition());
		System.out.println(mmap.getNextPosition());
		obj3 = mmap.read(mmap.getNextPosition());
		System.out.println(mmap.getNextPosition());
		Object obj4 = mmap.read(mmap.getNextPosition());
		System.out.println(mmap.getNextPosition());

		System.out.println(obj1);
		System.out.println(obj2);
		System.out.println(obj3);
		System.out.println(obj4);

		// this should cause an error
		Object obj5 = mmap.read(mmap.getNextPosition());
		System.out.println(mmap.getNextPosition());
	}
}

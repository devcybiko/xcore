package us.thinkable.xcore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CollectionUtil {
	/**
	 * a static copy of an instance of MapComparator for use in sortMap(), below
	 */
	private static MapComparator mapComparator = new MapComparator();
	private static MapComparatorReverse mapComparatorReverse = new MapComparatorReverse();

	/**
	 * a local class that compares two strings that may have been returned by
	 * uniquify()
	 * 
	 * @author Owner
	 *
	 */
	private static class MapComparator implements java.util.Comparator<Entry<String, Integer>> {
		public int compare(Entry<String, Integer> aa, Entry<String, Integer> bb) {
			return aa.getValue() - bb.getValue();
		}
	}

	private static class MapComparatorReverse implements java.util.Comparator<Entry<String, Integer>> {
		public int compare(Entry<String, Integer> aa, Entry<String, Integer> bb) {
			return bb.getValue() - aa.getValue();
		}
	}

	/**
	 * sorts a map (as returned by uniquify()) by the Integer and returns the
	 * results in a list.
	 * 
	 * @param map
	 * @return
	 */
	public static List<Entry<String, Integer>> sortMap(Map<String, Integer> map) {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
		for (Entry<String, Integer> entry : map.entrySet()) {
			list.add(entry);
		}
		Collections.sort(list, mapComparator);
		return list;
	}

	public static List<Entry<String, Integer>> sortMapReverse(Map<String, Integer> map) {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
		for (Entry<String, Integer> entry : map.entrySet()) {
			list.add(entry);
		}
		Collections.sort(list, mapComparatorReverse);
		return list;
	}

	/**
	 * Given a list of words, throw them into a hash map to uniquify them Also
	 * counts the words as we go
	 * 
	 * @param words
	 *            a list of words to find
	 * @return
	 */
	public static Map<String, Integer> uniquify(List<String> words) {
		Map<String, Integer> map = new Hashtable<String, Integer>();
		for (String word : words) {
			Integer cnt = map.get(word);
			if (cnt == null)
				cnt = new Integer(0);
			cnt = cnt + 1;
			map.put(word, cnt);
		}
		return map;
	}

	/**
	 * Given a list of words, throw them into a hash map to uniquify them Also
	 * counts the words as we go Turns each word to lowercase as we go
	 * 
	 * @param words
	 *            the list of words to uniquify
	 * @return
	 */
	public static Map<String, Integer> uniquifyLowerCase(List<String> words) {
		Map<String, Integer> map = new Hashtable<String, Integer>();
		for (String word : words) {
			word = word.toLowerCase();
			Integer cnt = map.get(word);
			if (cnt == null)
				cnt = new Integer(0);
			cnt = cnt + 1;
			map.put(word, cnt);
		}
		return map;
	}

}

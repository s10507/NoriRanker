package xmltest;


import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

public class XmlTest{

	public static void main(String[] args) throws SlickException {
		String path = "./resource/untitled.tmx";

		XMLParser xp = new XMLParser();

		XMLElement xe_root = xp.parse(path);

		XMLElementList xel = xe_root.getChildrenByName("tileset");

		ArrayList<XMLElement> xe_all = new ArrayList<XMLElement>();

		String[] a = new String[20];

		int[] firstgid = new int[20];

		for(int i = 0; i < xel.size(); i++){
			xe_all.add(xel.get(i));
			a[i] = xe_all.get(i).toString();
			System.out.println("firstgid "+i+": "+xe_all.get(i).getIntAttribute("firstgid"));
			firstgid[i] = xe_all.get(i).getIntAttribute("firstgid");
		}

		for(int i = 0; i < xe_all.size(); i++)
			System.out.println(a[i]);
	}

}

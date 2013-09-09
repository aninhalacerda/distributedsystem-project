package jgroups;

import org.jgroups.demos.Draw;

public class JGroupsTest {
	
	static Draw draw;
	static Draw draw2;

	public static void main(String[] args) {
		try {
			draw = new Draw(null, false, false, false, 0, false, "name");
			draw2 = new Draw(null, false, false, false, 0, false, "name2");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			draw.go();
			System.out.println("GO");
			draw2.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

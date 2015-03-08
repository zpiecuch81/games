package pl.com.ezap.mazeescape.handlers;


public class BBInput {

	public static int x;
	public static int y;
	public static boolean wasTapped;
	public static boolean down;
	public static boolean pdown;

	public static boolean[] keys;
	public static boolean[] pkeys;
	public static boolean[] flinged;
	private static final int NUM_KEYS = 6;
	public static final int ZOOMIN = 0;
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final int BACK = 5;

	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
		flinged = new boolean[NUM_KEYS];
	}

	public static void update() {
		pdown = down;
		for(int i = 0; i < NUM_KEYS; i++) {
			pkeys[i] = keys[i];
			flinged[i] = false;
		}
	}

	public static boolean isDown() { return down; }
	public static boolean wasTap() { return down && !pdown; }
	public static boolean isReleased() { return !down && pdown; }

	public static void setKey(int i, boolean b) { keys[i] = b; }
	public static void wasFlinged(int i) { flinged[i] = true; }
	public static boolean isDown(int i) { return keys[i]; }
	public static boolean isPressed(int i) { return flinged[i] || keys[i] && !pkeys[i]; }

}

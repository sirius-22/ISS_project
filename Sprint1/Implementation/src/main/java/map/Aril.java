package main.java.map;

public enum Aril{
	l("left"), r("right"), w("up"), s("down"), a("a"), d("d"), h("h"), p("p"), none(" ");
	
	private final String dirString;
	
	Aril(String dirString) {
		this.dirString=dirString;
	}
	
	@Override
	public String toString() {
		return dirString;
	}
}

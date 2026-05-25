
public class Hymn {
	private int hymnID;
	private int hymnNumber;
	private String hymnTitle;
	
	public Hymn(int id, int number, String title) {
		hymnID = id;
		hymnNumber = number;
		hymnTitle = title;
	}
	
	public int getHymnID() {
		return hymnID;
	}
	
	public int getHymnNumber() {
		return hymnNumber;
	}
	
    @Override
    public String toString() {
        return (hymnNumber == 0 ? "*Insert*" : hymnNumber) + " - " + hymnTitle;
    }
}

import java.util.Objects;

class PeopleRecord implements Comparable<PeopleRecord>{
	 // 13 fields (store as String for simplicity)
    private final String givenName;
    private final String familyName;
    private final String companyName;
    private final String address;
    private final String city;
    private final String county;
    private final String state;
    private final String zip;
    private final String phone1;
    private final String phone2;
    private final String email;
    private final String web;
    private final String birthday;
    
    public PeopleRecord(String givenName, String familyName, String companyName,
            String address, String city, String county, String state,
            String zip, String phone1, String phone2, String email,
            String web, String birthday) {
    	
    	this.givenName = safe(givenName);
        this.familyName = safe(familyName);
        this.companyName = safe(companyName);
        this.address = safe(address);
        this.city = safe(city);
        this.county = safe(county);
        this.state = safe(state);
        this.zip = safe(zip);
        this.phone1 = safe(phone1);
        this.phone2 = safe(phone2);
        this.email = safe(email);
        this.web = safe(web);
        this.birthday = safe(birthday);
    }
    
    /** Parse one line from people.txt into a PeopleRecord. Returns null if line is empty. */
    public static PeopleRecord fromLine(String line) {
    	if(line == null) return null;
    	line = line.trim();
    	if(line.isEmpty()) return null;
    	
    	String[] parts = line.split(";", -1); // keep empty fields if ";;" occurs
    	
    	// If the line has fewer than 13 fields, pad with empty strings.
        // If it has more, ignore extras.
        String[] p = new String[13];
        for (int i = 0; i < 13; i++) {
            p[i] = (i < parts.length) ? safe(parts[i]) : "";
        }
        
        return new PeopleRecord(
                p[0], p[1], p[2], p[3], p[4], p[5], p[6],
                p[7], p[8], p[9], p[10], p[11], p[12]
        );

    }
    
    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
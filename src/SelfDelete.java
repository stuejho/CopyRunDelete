import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class SelfDelete {

	private static final String OS;
	private static final String FULL_PATH;
	private static final String WINDOWS_COMMAND;
	private static final String MAC_COMMAND;
	private static final String UNIX_COMMAND;
	
	// Static initializer block to set final String variables
	static {
		// Use System to get the name of the user operating system
		OS = System.getProperty("os.name").toLowerCase();
		FULL_PATH = getJarFilePath();
		WINDOWS_COMMAND = String.format("ping localhost -n 2 && del \"%s\"", FULL_PATH);
		MAC_COMMAND = String.format("rm \"%s\"", FULL_PATH);
		UNIX_COMMAND = String.format("rm \"%s\"", FULL_PATH);
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Operating System: " + System.getProperty("os.name"));
		System.out.println("JAR File Path: " + FULL_PATH);
		
		if (isWindows()) {
			new ProcessBuilder("cmd", "/c", WINDOWS_COMMAND).start();
		}
		else if (isMac()) {
			new ProcessBuilder("bash", "-c", MAC_COMMAND).start();
		}
		else if (isUnix()) {
			new ProcessBuilder("bash", "-c", UNIX_COMMAND).start();
		}
		else {
			System.err.println("Error: Unknown operating system");
			System.exit(1);
		}
	}
	
	/**
	 * Gets the full path to the running JAR file. This includes the JAR file
	 * name and extension
	 * 
	 * @return the full path with file name and extension
	 */
	private static String getJarFilePath() {
		try {
			return new File(SelfDelete.class
					.getProtectionDomain()
					.getCodeSource()
					.getLocation()
				    .toURI()).getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Determines if the running operating system is Windows.
	 * @return true if the operating system is Windows; false otherwise
	 */
	private static boolean isWindows() {
		return OS.toLowerCase().contains("windows");
	}
	
	/**
	 * Determines if the running operating system is macOS.
	 * @return true if the operating system is macOS; false otherwise
	 */
	private static boolean isMac() {
		return OS.toLowerCase().contains("mac");
	}
	
	/**
	 * Determines if the running operating system is UNIX-based.
	 * @return true if the operating system is UNIX-like; false otherwise
	 */
	private static boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }
}

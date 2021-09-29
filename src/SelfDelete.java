import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class SelfDelete {

	// If anyone is reading this, there's a lot to unpack for WINDOWS_COMMAND:
	// - `cmd /c` basically means to use cmd.exe and run a command that will
	//   terminate and return control back to the caller
	// - `ping localhost -n 2` is a classic way of occupying the system for
	//   2 seconds. Basically, the computer pings itself twice and each ping
	//   takes about 1 second.
	// - `>` is the output redirection symbol. In this case, we don't want any
	//   Windows command line output to show up, so we redirect it to `nul`.
	//   This basically means nothing will show up. As Windows-specific example,
	//   try running `dir` in cmd.exe. Then try `dir > nul`.
	// - `2>&1` can be thought of as 3 parts:
	//   * `>` means redirect output
	//   * `2` means use file descriptor 2 STDERR (where errors are shown)
	//     as the source of redirection
	//   * `&1` means use file descriptor 1 (STDOUT) which has been set to nul
	//     as the target of redirection
	//     We use `&1` instead of `1` since the single value `1` would be 
	//     interpreted as a file named one, not the file descriptor
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

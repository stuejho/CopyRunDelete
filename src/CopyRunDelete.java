import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class CopyRunDelete {
	
	private static final String OS;
	private static final String FULL_PATH;
	private static final String WINDOWS_COMMAND;
	private static final String MAC_COMMAND;
	private static final String UNIX_COMMAND;
	private static final String SUB_DIR_NAME;
	
	// Static initializer block to set final String variables
	static {
		// Use System to get the name of the user operating system
		OS = System.getProperty("os.name").toLowerCase();
		FULL_PATH = getJarFilePath();
		WINDOWS_COMMAND = String.format("ping localhost -n 2 && del \"%s\"", FULL_PATH);
		MAC_COMMAND = String.format("rm \"%s\"", FULL_PATH);
		UNIX_COMMAND = String.format("rm \"%s\"", FULL_PATH);
		SUB_DIR_NAME = "iLUVjava";
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Operating System: " + System.getProperty("os.name"));
		System.out.println("JAR File Path: " + FULL_PATH);
		
		// JAR may have command line args to represent ttl
		int ttl;
		if (args.length == 0) {
			Random rand = new Random();
			int min = 1;
			int max = 10;
			ttl = rand.nextInt((max - min) + 1) + min;
		}
		else {
			ttl = Integer.parseInt(args[0]) - 1;
		}
		System.out.println("TTL: " + ttl);
		
		// Stop when either nothing or too much is supposed to happen
		if (ttl <= 0 || ttl > 10) {
			return;
		}
		// Otherwise, create a subdirectory, copy the jar, run the
		// jar copy, and delete the current jar
		else {
			makeAndRunSubJar(Integer.toString(ttl));
			deleteCurrJar();
		}
	}
	
	/**
	 * Gets the full path to the running JAR file. This includes the JAR file
	 * name and extension.
	 * 
	 * @return the full path with file name and extension
	 */
	private static String getJarFilePath() {
		try {
			return new File(CopyRunDelete.class
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
	 * 
	 * @return true if the operating system is Windows; false otherwise.
	 */
	private static boolean isWindows() {
		return OS.toLowerCase().contains("windows");
	}
	
	/**
	 * Determines if the running operating system is macOS.
	 * 
	 * @return true if the operating system is macOS; false otherwise.
	 */
	private static boolean isMac() {
		return OS.toLowerCase().contains("mac");
	}
	
	/**
	 * Determines if the running operating system is UNIX-based.
	 * 
	 * @return true if the operating system is UNIX-like; false otherwise.
	 */
	private static boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }
	
	/**
	 * Creates a subdirectory in the current directory, copies the current
	 * JAR file to the subdirectory, and runs the copied JAR.
	 * 
	 * @param ttlStr represents the TTL string to pass to the next call.
	 * @throws IOException thrown when an I/O exception occurs.
	 */
	private static void makeAndRunSubJar(String nextTtlStr) throws IOException {
		// Get jar file to use in generating subdirectory/jar copy
		File jar = new File(FULL_PATH);
		System.out.println(jar.getName());
		
		// Create subdirectory
		String subDirPath = jar.getParent() + File.separator + SUB_DIR_NAME;
		File subDir = new File(subDirPath);
		subDir.mkdir();
		
		// Create file to copy into
		String subDirJarPath = subDirPath + File.separator + jar.getName();
		File subDirJar = new File(subDirJarPath);
		subDirJar.createNewFile();
		
		// Copy JAR into subdirectory
		Files.copy(jar.toPath(), subDirJar.toPath(), 
				StandardCopyOption.REPLACE_EXISTING);
				
		// Run JAR in subdirectory
		System.out.println(SUB_DIR_NAME + File.separator + jar.getName());
		new ProcessBuilder("java", "-jar", subDirJar.getAbsolutePath(), 
				nextTtlStr).start();
	}
	
	/**
	 * Launches a separate process to delete the current JAR file.
	 * 
	 * @throws IOException thrown when an I/O error occurs.
	 */
	private static void deleteCurrJar() throws IOException {
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
}
